
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.Administrator;
import domain.Configuration;
import domain.Message;

@Service
@Transactional
public class MessageService {

	@Autowired
	private MessageRepository		messageRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private Validator				validator;


	public Message save(Message entity) {
		return this.messageRepository.save(entity);
	}

	public List<Message> findAll() {
		return this.messageRepository.findAll();
	}

	public Message findOne(Integer id) {
		return this.messageRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return this.messageRepository.exists(id);
	}

	public void delete(Integer id) {
		this.messageRepository.delete(id);
	}

	// BUSINESS METHODS

	public Message sendMessage(Actor dest, Actor sender, String subject, String body, String tags) {
		Assert.notNull(sender);
		Assert.notNull(dest);
		Assert.notNull(this.actorService.findOne(dest.getId()));
		Assert.notNull(this.actorService.findOne(sender.getId()));
		Message message = new Message();
		message.setMoment(new Date(System.currentTimeMillis() - 10));
		message.setSender(sender);
		message.setRecipient(dest);
		message.setSubject(subject);
		message.setBody(body);
		message.setTags(tags);
		return this.messageRepository.save(message);

	}

	public void deleteMessage(Message message) {
		Assert.notNull(message);
		Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(this.messageRepository.getMessagesForActorId(principal.getId()).contains(message));
		Set<String> tags = MessageService.getTagSet(message);
		if (tags.contains("DELETED"))
			this.delete(message.getId());
		else {
			tags.add("DELETED");
			String tagsFinal = MessageService.getTagsFromList(tags);
			message.setTags(tagsFinal);
			this.save(message);
		}
	}

	public Collection<Message> broadcastMessage(Message m, Administrator admin) {
		Collection<Message> ans = new ArrayList<Message>();
		if (admin == null) {
			admin = this.administratorService.findByPrincipal();
			Assert.notNull(admin);
		}
		m.setSender(admin);
		List<Actor> actors = this.actorService.findAll();
		for (Actor a : actors) {
			m.setRecipient(a);
			m.setMoment(new Date(System.currentTimeMillis() - 10));
			ans.add(this.messageRepository.save(m));
		}
		return ans;
	}

	public Collection<Message> sendNotification(String subject, String body, Collection<Actor> dest) {
		Collection<Message> ans = new ArrayList<Message>();
		for (Actor a : dest)
			ans.add(this.sendNotification(subject, body, a));
		return ans;
	}

	public Message sendNotification(String subject, String body, Actor dest) {
		Administrator admin = this.administratorService.findAll().get(0);
		Assert.notNull(admin);
		return this.sendMessage(dest, admin, subject, body, "SYSTEM");
	}
	// ANCILLARY METHODS

	public static HashSet<String> getTagSet(Message message) {
		HashSet<String> ans = new HashSet<String>();
		Collections.addAll(ans, message.getTags().split(","));
		return ans;
	}

	public static String getTagsFromList(Set<String> tags) {
		String ans = "";
		boolean isFirst = true;
		for (String s : tags)
			if (!s.equals(""))
				if (!isFirst)
					ans += "," + s;
				else {
					ans += s;
					isFirst = false;
				}

		return ans;
	}

	public Collection<Message> getMessagesForActor(Actor actor) {
		return this.messageRepository.getMessagesForActorId(actor.getId());
	}

	public Message reconstruct(Message message, BindingResult binding, boolean isBroadcast) {
		if (!isBroadcast)
			Assert.isTrue(message.getRecipient() != null, "message.error.recipients");
		Message msg = new Message();
		msg.setBody(message.getBody());
		msg.setId(message.getId());
		msg.setMoment(new Date(System.currentTimeMillis() - 1));
		msg.setRecipient(message.getRecipient());
		msg.setSubject(message.getSubject());
		msg.setTags(message.getTags());
		if (message.getId() == 0) {
			msg.setSender(this.actorService.findByPrincipal());
			msg.setVersion(0);
		} else {
			msg.setSender(message.getSender());
			msg.setVersion(this.findOne(message.getId()).getVersion());
		}
		if (msg.getRecipient() == null)
			msg.setRecipient(msg.getSender());
		this.validator.validate(msg, binding);
		if (binding.hasErrors())
			throw new ValidationException();
		return msg;
	}

	public Collection<Message> broadcastMessage(Message message) {
		Administrator prin = this.administratorService.findByPrincipal();
		Assert.isTrue(prin != null);
		return this.broadcastMessage(message, prin);
	}

	private Pattern getSpamPattern() {
		Configuration conf = this.configurationService.findConfiguration();
		Collection<String> words;
		words = conf.getSpamWords();
		String patternString = "^";
		for (String word : words)
			patternString += (word.toLowerCase() + "|");
		patternString = patternString.substring(0, patternString.length() - 1);
		patternString += "$";
		Pattern ans = Pattern.compile(patternString);
		return ans;
	}

	public void calculateSpammer() {
		Assert.notNull(this.administratorService.findByPrincipal());
		Pattern goodPattern = this.getSpamPattern();
		Matcher good = null;
		final Map<Actor, Integer> ans = new HashMap<Actor, Integer>();
		for (final Actor c : this.actorService.findAll()) {
			int spamMessages = 0;
			for (final Message e : this.findAll())
				if (e.getSender().equals(c)) {
					good = goodPattern.matcher(e.getBody().toLowerCase() + "," + e.getSubject().toLowerCase());
					while (good.find())
						spamMessages++;
				}
			ans.put(c, spamMessages);
		}
		final List<Integer> values = new ArrayList<Integer>(ans.values());
		Collections.sort(values);
		for (final Actor c : ans.keySet()) {
			boolean spammer = ans.get(c) > this.messageRepository.messagesFromActorId(c.getId()).size() * 0.1;
			if (c.getIsSpammer() != spammer)
				this.actorService.updateSpammer(c, spammer);
		}
	}

}

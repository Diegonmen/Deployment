
package controllers;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;
import domain.Actor;
import domain.Message;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ActorService	actorService;

	@Autowired
	private MessageService	messageService;


	// Constructors -----------------------------------------------------------

	public MessageController() {
		super();
	}

	// List -------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Actor a = this.actorService.findByPrincipal();

		final Collection<Message> messages = this.messageService.getMessagesForActor(a);
		result = new ModelAndView("message/list");

		result.addObject("requestURI", "message/list.do");
		result.addObject("messages", messages);

		return result;
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int messageId) {
		ModelAndView result;
		Message message;

		message = this.messageService.findOne(messageId);
		Assert.notNull(message);

		Actor loggedActor = this.actorService.findByPrincipal();
		if (!(message.getRecipient().equals(loggedActor))) {
			result = new ModelAndView("message/list");
			result.addObject("message", "message.not.yours");
			return result;
		}

		result = new ModelAndView("message/show");

		result.addObject("messageObject", message);

		return result;

	}

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Message message;

		message = new Message();

		result = this.createEditModelAndView(message, null);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int messageId) {
		ModelAndView result;
		result = new ModelAndView("redirect:/message/list.do");
		Actor logged = this.actorService.findByPrincipal();
		try {
			Message m = this.messageService.findOne(messageId);
			Assert.isTrue(this.messageService.getMessagesForActor(logged).contains(m));
			this.messageService.deleteMessage(m);
		} catch (Throwable oops) {
			result.addObject("message", "message.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute(value = "messageObject") Message messageObject, final BindingResult binding) {
		ModelAndView result;
		try {
			Message message = this.messageService.reconstruct(messageObject, binding, false);
			this.messageService.sendMessage(message.getRecipient(), message.getSender(), message.getSubject(), message.getBody(), message.getTags());
			result = new ModelAndView("redirect:/message/list.do");
		} catch (ValidationException oops) {
			result = this.createEditModelAndView(messageObject, null);
		} catch (final Throwable oops) {
			if (oops.getMessage() != null && oops.getMessage().contains("error"))
				result = this.createEditModelAndView(messageObject, oops.getMessage());
			else
				result = this.createEditModelAndView(messageObject, "message.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/administrator/broadcast", method = RequestMethod.GET)
	public ModelAndView broadcast() {
		ModelAndView result;
		Message message;

		message = new Message();
		message.setTags("SYSTEM");

		result = this.createBroadcastModelAndView(message, null);

		return result;
	}

	@RequestMapping(value = "/administrator/broadcast", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBroadcast(@ModelAttribute(value = "messageObject") Message messageObject, final BindingResult binding) {
		ModelAndView result;
		try {
			Message message = this.messageService.reconstruct(messageObject, binding, true);
			this.messageService.broadcastMessage(message);
			result = new ModelAndView("redirect:/message/list.do");
		} catch (ValidationException oops) {
			result = this.createBroadcastModelAndView(messageObject, null);
		} catch (final Throwable oops) {
			if (oops.getMessage() != null && oops.getMessage().contains("error"))
				result = this.createEditModelAndView(messageObject, oops.getMessage());
			else
				result = this.createEditModelAndView(messageObject, "message.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message message, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("message/send");

		result.addObject("messageObject", message);
		result.addObject("allActors", this.actorService.findAll());
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createBroadcastModelAndView(final Message message, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("message/broadcast");

		result.addObject("messageObject", message);
		result.addObject("message", messageCode);

		return result;
	}
}

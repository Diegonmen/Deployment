
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import utilities.AbstractTest;
import domain.Actor;
import domain.Message;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MessageServiceTest extends AbstractTest {

	//Test coverage: 89.8%  // Covered instructions: 574 // Missed instructions: 65 // Total Instructions: 639
	//MessageService coverage: 45.8%  // Covered instructions: 258 // Missed instructions: 305 // Total Instructions: 563

	@Autowired
	private MessageService		messageService;

	@Autowired
	private MessageRepository	messageRepository;

	@Autowired
	private ActorService		actorService;


	@Test
	public void sendMessageDriver() {
		Object testingData[][] = {

			{
				"company0", "admin", "title", "body", "TAG", null
			}, // Positive test case: send a message
			{
				"company0", "admin", "title", "body", "TAG!*&%", ConstraintViolationException.class
			}, // Negative test case:
				// Tags with illegal characters
			{
				"company0", "pablito", "title", "body", "TAG", IllegalArgumentException.class
			}
		// Negative Case: recipient does not exist

		};

		for (int i = 0; i < testingData.length; i++)
			this.sendMessageTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	public void sendMessageTemplate(String sender, String recipient, String subject, String body, String tags, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(sender);
			Assert.isTrue(super.existsEntity(recipient));
			Integer actorId = super.getEntityId(recipient);
			Actor dest = this.actorService.findOne(actorId);
			actorId = super.getEntityId(sender);
			Actor send = this.actorService.findOne(actorId);
			this.messageService.sendMessage(dest, send, subject, body, tags);

			this.messageRepository.flush();

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void broadcastMessageDriver() {
		Object testingData[][] = {

			{
				"admin", "Attention", "Something happened", null
			}, // Positive test case: send broadcast
			{
				"company1", "Attention", "Something happened", IllegalArgumentException.class
			}
		// Negative test case: not an admin

		};

		for (int i = 0; i < testingData.length; i++)
			this.broadcastMessageTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	public void broadcastMessageTemplate(String username, String head, String body, Class<?> expected) {

		Class<?> caught = null;

		try {
			int prev = this.messageService.findAll().size();
			int goal = prev + this.actorService.findAll().size();
			this.authenticate(username);
			Message m = new Message();
			m.setBody(body);
			m.setSubject(head);
			this.messageService.broadcastMessage(m);
			this.unauthenticate();
			Assert.isTrue(this.messageService.findAll().size() == goal);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void recalculateSpammersDriver() {
		Object testingData[][] = {

			{
				"company1", false, null
			// Positive test case: the actor has this value after process 
			}, {
				"company0", null, IllegalArgumentException.class
			// Negative test case: incorrect value for actor
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.recalculateSpammerTemplate((String) testingData[i][0], (Boolean) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	public void recalculateSpammerTemplate(String username, Boolean value, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate("admin");
			this.messageService.calculateSpammer();
			this.unauthenticate();
			Assert.isTrue(this.actorService.findOne(super.getEntityId(username)).getIsSpammer().equals(value));
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void banSpammersDriver() {
		Object testingData[][] = {

			{
				"company1", false, null
			// Positive test case: ban an actor 
			}, {
				"company0", true, IllegalArgumentException.class
			// Negative test case: ban an already banned actor
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.banSpammerTemplate((String) testingData[i][0], (Boolean) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	public void banSpammerTemplate(String username, Boolean value, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate("admin");
			this.actorService.updateBan(this.actorService.findOne(super.getEntityId(username)), true);
			if (value)
				this.actorService.updateBan(this.actorService.findOne(super.getEntityId(username)), true);
			this.unauthenticate();
			Assert.isTrue(this.actorService.findOne(super.getEntityId(username)).getIsSpammer().equals(value));
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void unbanSpammersDriver() {
		Object testingData[][] = {

			{
				"company1", false, null
			// Positive test case: the actor has this value after process 
			}, {
				"company0", true, IllegalArgumentException.class
			// Negative test case: unban an actor that was not banned
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.banSpammerTemplate((String) testingData[i][0], (Boolean) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	public void unbanSpammerTemplate(String username, Boolean value, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate("admin");
			this.actorService.updateBan(this.actorService.findOne(super.getEntityId(username)), false);
			if (value)
				this.actorService.updateBan(this.actorService.findOne(super.getEntityId(username)), false);
			this.unauthenticate();
			Assert.isTrue(this.actorService.findOne(super.getEntityId(username)).getIsSpammer().equals(value));
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}

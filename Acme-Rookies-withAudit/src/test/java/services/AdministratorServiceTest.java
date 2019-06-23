
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Message;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AdministratorServiceTest extends AbstractTest {
	
	//Test coverage: 92.2%  // Covered instructions: 530 // Missed instructions: 45 // Total Instructions: 575
	//AdministratorService coverage: 64.9%  // Covered instructions: 325 // Missed instructions: 176 // Total Instructions: 501

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MessageService			messageService;


	@Test
	public void notifyRebrandingDriver() {
		Object testingData[][] = {
			{
				"admin", "We have changed our name! - ¡Nos hemos cambiado el nombre!", null
			}, // Positive test case
			{
				"admin", "Great Firewall of China", IllegalArgumentException.class
			}, // Blank
		};

		for (int i = 0; i < testingData.length; i++)
			this.notifyRebrandingTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void recalcScoreDriver() {
		Object testingData[][] = {
			{
				"company0", 0d, null
			}, // Positive test case
			{
				"company1", 0d, IllegalArgumentException.class
			}, // Blank
		};

		for (int i = 0; i < testingData.length; i++)
			this.recalcScoreTemplate((String) testingData[i][0], (Double) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	public void notifyRebrandingTemplate(String userToCheck, String messageToCheck, Class<?> expected) {
		Class<?> caught = null;
		try {
			int numMessages = this.messageService.getMessagesForActor(this.actorService.findOne(super.getEntityId(userToCheck))).size();
			this.administratorService.announceRebranding();
			Assert.isTrue(this.messageService.getMessagesForActor(this.actorService.findOne(super.getEntityId(userToCheck))).size() == numMessages + 1);
			boolean hasIt = false;
			for (Message m : this.messageService.getMessagesForActor(this.actorService.findOne(super.getEntityId(userToCheck))))
				hasIt = hasIt || m.getSubject().equals(messageToCheck);
			Assert.isTrue(hasIt);

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void recalcScoreTemplate(String userToCheck, Double obj, Class<?> expected) {
		Class<?> caught = null;
		try {
			this.administratorService.computeAuditorScore();
			Assert.isTrue(this.companyService.findOne(super.getEntityId(userToCheck)).getAuditScore().equals(obj));

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}


package services;

import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Application;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	//Test coverage: 92.2%  // Covered instructions: 530 // Missed instructions: 45 // Total Instructions: 575
	//ApplicationService coverage: 64.9%  // Covered instructions: 325 // Missed instructions: 176 // Total Instructions: 501

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private CurriculaService	curriculaService;


	@Test
	public void createApplicationDriver() {
		Object testingData[][] = {
			{
				"rookie0", "curricula0", "position0", IllegalArgumentException.class
				// Existing applications have been made by this rookie to this position
			}, {
				"rookie0", "curricula2", "position1", IllegalArgumentException.class
				// Curricula doesn't belong to that rookie
			}, {
				"company0", "curricula0", "position0", IllegalArgumentException.class
				// Company can't create applications.
			}, {
				"rookie0", "curricula0", "position2", null
				// Positive test case, no business rule broken here
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createApplicationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	@Test
	public void submitApplicationDriver() {
		Object testingData[][] = {
			{
				"rookie0", "application0", "answer", "http://www.google.es", IllegalArgumentException.class
				// Submitting an already submitted application
			}, {
				"rookie1", "application1", "answer", "http://www.google.es", IllegalArgumentException.class
				// Not correct rookie
			}, {
				"rookie0", "application1", "answer", "http://www.google.es", null
				// Positive test case, no business rule broken here
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.submitApplicationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	@Test
	public void acceptApplicationDriver() {
		Object testingData[][] = {
			{
				"company1", "application2", IllegalArgumentException.class
				// Wrong company accepting application
			},
			{
				"company0", "application1", IllegalArgumentException.class
				// Wrong status of the application to be accepted
			}, {
				"company0", "application2", null
				// Positive test case, no business rule broken here
			},
		};

		for (int i=0; i< testingData.length; i++)
			this.acceptApplicationTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@Test
	public void rejectApplicationDriver() {
		Object testingData[][] = {
			{
				"company1", "application2", IllegalArgumentException.class
				// Wrong company accepting application
			},
			{
				"company0", "application1", IllegalArgumentException.class
				// Wrong status of the application to be accepted
			}, {
				"company0", "application2", null
				// Positive test case, no business rule broken here
			},
		};

		for (int i=0; i< testingData.length; i++)
			this.rejectApplicationTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	public void createApplicationTemplate(String username, String curricula, String position, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Application application = this.applicationService.create();
			application.setCurricula(this.curriculaService.findOne(super.getEntityId(curricula)));
			Application saved = this.applicationService.save(application, super.getEntityId(position));
			Assert.notNull(saved);
			Assert.notNull(saved.getProblem());
			Assert.isTrue(saved.getStatus().equals("PENDING"));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void submitApplicationTemplate(String username, String application, String answer, String answerLink, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Application app = this.applicationService.findOneToEditRookie(super.getEntityId(application));
			app.setAnswer(answer);
			app.setAnswerLink(answerLink);
			Application submitted = this.applicationService.submit(app);
			Assert.notNull(submitted);
			Assert.isTrue(submitted.getStatus().equals("SUBMITTED"));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void acceptApplicationTemplate(String username, String application, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Application toEdit = this.applicationService.findOne(super.getEntityId(application));
			Application app = this.applicationService.findOneToEditCompany(toEdit.getId(), toEdit.getProblem().getPosition().getId());
			this.applicationService.accept(app);
			Assert.isTrue(app.getStatus().equals("ACCEPTED"));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void rejectApplicationTemplate(String username, String application, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Application toEdit = this.applicationService.findOne(super.getEntityId(application));
			Application app = this.applicationService.findOneToEditCompany(toEdit.getId(), toEdit.getProblem().getPosition().getId());
			this.applicationService.reject(app);
			Assert.isTrue(app.getStatus().equals("REJECTED"));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}

package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.ProblemRepository;
import utilities.AbstractTest;
import domain.Position;
import domain.Problem;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProblemServiceTest extends AbstractTest {

	//Test coverage: 100%  // Covered instructions: 335 // Missed instructions: 0 // Total Instructions: 335
	//ProblemService coverage: 76%  // Covered instructions: 98 // Missed instructions: 31 // Total Instructions: 129

	@Autowired
	private ProblemService problemService;
	
	@Autowired
	private PositionService positionService;

	@Autowired
	private ProblemRepository problemRepository;

	@Test
	public void createProblemDriver() {
		Object testingData[][] = {

			{
				"company0", "titulo", "statement", "https://www.google.es", "pista" ,"position0", true,  null
			},				// Positive test case: create a problem
			{
				"company0", "", "statement", "http://www.google.es", "pista" ,"position0", true, ConstraintViolationException.class
			},				// Negative test case: Create a problem with empty title	
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.createProblemTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], 
					(String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (boolean) testingData[i][6],(Class<?>) testingData[i][7]);
	}


	public void createProblemTemplate(String user, String title, String statement, String attachments, String hint, String position, boolean draftMode, Class<?> expected) {

		Class<?> caught = null;

		try {
			
			Collection<String> attachment = new ArrayList<>();
			attachment.add(attachments);
			int positionId = super.getEntityId(position);
			Position positionToSet = positionService.findOne(positionId);
			
			this.authenticate(user);
			Problem problem = this.problemService.create();
			problem.setTitle(title);
			problem.setAttachments(attachment);
			problem.setStatement(statement);
			problem.setHint(hint);
			problem.setDraftMode(draftMode);
			problem.setPosition(positionToSet);
			
			Problem saved = this.problemService.save(problem);
			
			this.problemRepository.flush();
			
			Assert.notNull(saved);
			
			this.unauthenticate();
			
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void deleteProblemDriver() {
		Object testingData[][] = {

			{
				"company0", "problem5", null
				//POsitive test case: delete a problem in draft mode not assigned to any application
			},	
			{
				"company0", "problem2", IllegalArgumentException.class
			},				// Negative test case: delete a problem which has an application
			{
				"company0","problem0", IllegalArgumentException.class
			},			// Negative test case: delete a problem in final mode
			{
				"company0","problem6", IllegalArgumentException.class
						//Negative test case : delete a problem which is not mine
			},	

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteProblemTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]),(Class<?>) testingData[i][2]);
	}
	
	public void deleteProblemTemplate(String username, int problemId, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Problem problem = problemService.findOneToEdit(problemId);
			this.problemService.delete(problem.getId());
			problemRepository.flush();
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	


}

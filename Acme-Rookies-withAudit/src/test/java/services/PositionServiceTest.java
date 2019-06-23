package services;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.PositionRepository;
import utilities.AbstractTest;
import domain.Position;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PositionServiceTest extends AbstractTest {

	//Test coverage: 99.5%  // Covered instructions: 592 // Missed instructions: 3 // Total Instructions: 595
	//PositionService coverage: 65.9%  // Covered instructions: 375 // Missed instructions: 194 // Total Instructions: 569

	@Autowired
	private PositionService positionService;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private ProblemService problemService;

	@Test
	public void createPositionDriver() {
		Object testingData[][] = {

				{ "company0", "titulo", "descripcion", "02/02/2020 22:00",
						true, "profile", "skills", "technologies", 1000.0, null }, // Positive
																					// test
																					// case:
																					// create
																					// a
																					// position
				{ "company0", "titulo", "descripcion", "02/02/2020 22:00",
						false, "profile", "skills", "technologies", 1000.0,
						IllegalArgumentException.class }, // Negative test case:
															// Create a position
															// in final mode
															// with less than 2
															// problems assigned
				{ "company0", "titulo", "descripcion", "02/02/2010 22:00",
						true, "profile", "skills", "technologies", 1000.0,
						IllegalArgumentException.class }, // Negative Case: the
															// deadline is not
															// in future
				{ "company0", "titulo", "descripcion", "02/02/2020 22:00",
						true, "profile", "skills", "technologies", -100.0,
						ConstraintViolationException.class }, // Negative case:
																// input the
																// negative
																// salary

		};

		for (int i = 0; i < testingData.length; i++)
			this.createPositionTemplate((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (boolean) testingData[i][4],
					(String) testingData[i][5], (String) testingData[i][6],
					(String) testingData[i][7], (double) testingData[i][8],
					(Class<?>) testingData[i][9]);
	}

	public void createPositionTemplate(String user, String title,
			String description, String deadline, boolean draftMode,
			String profile, String skills, String technologies, double salary,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			Date date = new Date(System.currentTimeMillis() - 1);
			Date deadlineDate = new SimpleDateFormat("dd/MM/yyyy HH:mm")
					.parse(deadline);
			this.authenticate(user);
			Position position = this.positionService.create();
			position.setTitle(title);
			position.setDescription(description);
			position.setDeadline(deadlineDate);
			position.setDraftMode(draftMode);
			position.setProfile(profile);
			position.setSkills(skills);
			position.setTechnologies(technologies);
			position.setSalary(salary);

			// This checks are in the reconstruct
			if (!position.isDraftMode()) {
				Assert.isTrue(problemService.findProblemsOfPosition(
						position.getId()).size() >= 2);
			}

			Assert.isTrue(position.getDeadline().after(date));

			Position saved = this.positionService.save(position);

			this.positionRepository.flush();

			Assert.notNull(saved);

			this.unauthenticate();

		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void deletePositionDriver() {
		Object testingData[][] = {

		{ "company0", "position1", null }, // Positive test case: delete a
											// position in draft mode
		{ "company1", "position2", IllegalArgumentException.class }, // Negative test case: delete a position in final mode
		
		{ "company0", "position2", IllegalArgumentException.class	},// Negative test case : delete a position which is not mine

		};

		for (int i = 0; i < testingData.length; i++)
			this.deletePositionTemplate((String) testingData[i][0],
					super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);
	}

	public void deletePositionTemplate(String username, int positionId,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Position position = positionService.findOneToEdit(positionId);
			this.positionService.delete(position.getId());
			positionRepository.flush();
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void cancelPositionDriver() {
		Object testingData[][] = {
				
				{ "company1", "position5", null
				// Positive test case: cancel a position in final mode without references 
				}, { "company0", "position0", IllegalArgumentException.class
				// Negative test case: cancel a position in final mode but assigned to applications
				}, { "company0", "position1", IllegalArgumentException.class
				// Negative test case: cancel a position in draft mode
				}, { "company0", "position2", IllegalArgumentException.class
				// Negative test case: cancel a position which is not mine
				},

		};

		for (int i = 0; i < testingData.length; i++)
			this.cancelPositionTemplate((String) testingData[i][0],
					super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);
	}

	public void cancelPositionTemplate(String username, int positionId,
			Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Position position = positionService.findOne(positionId);
			this.positionService.cancel(position.getId());
			positionRepository.flush();
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}

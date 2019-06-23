package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Curricula;
import domain.PersonalData;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
	@RunWith(SpringJUnit4ClassRunner.class)
	@Transactional
public class PersonalDataServiceTest extends AbstractTest {

	//Test coverage: 100%  // Covered instructions: 144 // Missed instructions: 0 // Total Instructions: 144
	//PersonalDataService coverage: 75.2%  // Covered instructions: 82 // Missed instructions: 27 // Total Instructions: 109
	
	@Autowired
	private PersonalDataService personalDataService;
	
	@Autowired
	private CurriculaService curriculaService;
	
	@Test
	public void editPersonalDataDriver() {
		Object testingData[][] = {
			{
				"rookie0", "curricula0", "fullNameTest", "statementTest", null
			},
			{
				"rookie0", "curricula0", "", "statementTest", ConstraintViolationException.class
			}
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.editPersonalDataTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	
	public void editPersonalDataTemplate(String username, int curriculaId, String fullName, String statement, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Curricula curricula = curriculaService.findOne(curriculaId);
			PersonalData personalData = curricula.getPersonalData();
			personalData.setFullName(fullName);
			personalData.setStatement(statement);
			this.personalDataService.save(personalData, curricula);
			personalDataService.flush();
			curriculaService.flush();
			Assert.isTrue(curricula.getPersonalData().equals(personalData));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	
	
}

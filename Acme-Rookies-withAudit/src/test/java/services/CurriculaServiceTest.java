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

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
	@RunWith(SpringJUnit4ClassRunner.class)
	@Transactional
public class CurriculaServiceTest extends AbstractTest {
	
	//Test coverage: 98.5%  // Covered instructions: 321 // Missed instructions: 5 // Total Instructions: 326
	//CurriculaService coverage: 52.2%  // Covered instructions: 316 // Missed instructions: 289 // Total Instructions: 605
	
	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private RookieService rookieService;
	
	
	@Test
	public void copyCurriculaDriver() {
		Object testingData[][] = {
			{
				"rookie0", "curricula0", null
			}
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.copyCurriculaTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	
	@Test
	public void editCurriculaDriver() {
		Object testingData[][] = {
			{
				"rookie0", "curricula0", "Curricula0Modified", null
			},
			{
				"rookie0", "curricula0", "", ConstraintViolationException.class
			}
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.editCurriculaTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	
	@Test
	public void deleteCurriculaDriver() {
		Object testingData[][] = {
			{
				"rookie0", "curricula0", null
			},
			{
				"rookie1", "curricula0", IllegalArgumentException.class
			}
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteCurriculaTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	public void copyCurriculaTemplate(String username, int curriculaId, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Curricula curricula = curriculaService.findOne(curriculaId);
			Curricula copy = curriculaService.copyCurricula(curricula);
			curriculaService.flush();
			rookieService.flush();
			Assert.isTrue(rookieService.findByPrincipal().getCurricula().contains(copy));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void editCurriculaTemplate(String username, int curriculaId, String title, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Curricula curricula = curriculaService.findOne(curriculaId);
			curricula.setTitle(title);
			Curricula saved = curriculaService.save(curricula);
			curriculaService.flush();
			rookieService.flush();
			Assert.isTrue(saved.getTitle().equals(title));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	
	public void deleteCurriculaTemplate(String username, int curriculaId, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Curricula curricula = curriculaService.findOne(curriculaId);
			curriculaService.delete(curriculaId);
			curriculaService.flush();
			rookieService.flush();
			Assert.isTrue(!curriculaService.exists(curriculaId) && !rookieService.findByPrincipal().getCurricula().contains(curricula));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	
}

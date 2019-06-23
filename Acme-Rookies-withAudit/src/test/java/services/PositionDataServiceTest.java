package services;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Curricula;
import domain.PositionData;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
	@RunWith(SpringJUnit4ClassRunner.class)
	@Transactional
public class PositionDataServiceTest extends AbstractTest {

	//Test coverage: 98.6%  // Covered instructions: 438 // Missed instructions: 6 // Total Instructions: 444
	//PositionDataService coverage: 75.3%  // Covered instructions: 73 // Missed instructions: 24 // Total Instructions: 97
	
	@Autowired
	private PositionDataService positionDataService;
	
	@Autowired
	private CurriculaService curriculaService;
	
	@Test
	public void createPositionDataDriver() {
		Object testingData[][] = {
			{
				"rookie0", "curricula0", "titleTest", "descriptionTest", "08/03/2010 12:00", "09/03/2015 13:00", null
			},
			{
				"rookie0", "curricula0", "", "descriptionTest", "08/03/2020 12:00", "09/03/2015 13:00", ConstraintViolationException.class
			},
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.createPositionDataTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	
	@Test
	public void editPositionDataDriver() {
		Object testingData[][] = {
			{
				"rookie0", "curricula0", "titleTest", "descriptionTest", null
			},
			{
				"rookie0", "curricula0", "", "descriptionTest", ConstraintViolationException.class
			}
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.editPositionDataTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	
	@Test
	public void deletePositionDataDriver() {
		Object testingData[][] = {
			{
				"rookie0", "positionData0", null
			}
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.deletePositionDataTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	
	public void createPositionDataTemplate(String username, int curriculaId, String title, String description, String startDate, String endDate, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Curricula curricula = curriculaService.findOne(curriculaId);
			PositionData first = curricula.getPositionData().iterator().next();
			PositionData positionData = this.positionDataService.create();
			positionData.setTitle(title);
			positionData.setDescription(description);
			Date start = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(startDate);
			Date end = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(endDate);
			positionData.setStartDate(start);
			positionData.setEndDate(end);
//			PositionData posData = this.positionDataService.save(positionData, curricula);
			curricula.getPositionData().add(positionData);
			positionDataService.flush();
			Collection<PositionData> posData = curricula.getPositionData();
			posData.remove(first);
			PositionData second = posData.iterator().next();
//			curriculaService.flush();
			Assert.isTrue(curricula.getPositionData().contains(second));
//			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	
	public void editPositionDataTemplate(String username, int curriculaId, String title, String description, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Curricula curricula = curriculaService.findOne(curriculaId);
			PositionData positionData = curricula.getPositionData().iterator().next();
			positionData.setTitle(title);
			positionData.setDescription(description);
			PositionData saved = positionDataService.save(positionData, curricula);
			positionDataService.flush();
			curriculaService.flush();
			Assert.isTrue(curricula.getPositionData().contains(saved));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	
	public void deletePositionDataTemplate(String username, int positionDataId, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			PositionData positionData = positionDataService.findOne(positionDataId);
			Curricula curricula = curriculaService.findByPositionData(positionData);
			this.positionDataService.delete(positionData, curricula);
			positionDataService.flush();
			curriculaService.flush();
			Assert.isTrue(!curricula.getPositionData().contains(positionData));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	
	
	
	
}

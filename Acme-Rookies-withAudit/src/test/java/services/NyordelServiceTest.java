
package services;

import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.NyordelRepository;
import utilities.AbstractTest;
import domain.Nyordel;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class NyordelServiceTest extends AbstractTest {

	@Autowired
	private NyordelService		nyordelService;

	@Autowired
	private NyordelRepository	nyordelRepository;


	@Test
	public void createNyordelDriver() {
		Object testingData[][] = {
			{
				"company0", "audit0", "AB4856-191212", new Date(System.currentTimeMillis() - 5), "TEST", "https://i.imgur.com/aaaaa.jpg", true, null
			},								// Positive test case
			{
				"company0", "audit0", "AB4856-191212", new Date(System.currentTimeMillis() + 300000), "TEST", "https://i.imgur.com/aaaaa.jpg", true, ConstraintViolationException.class
			},								// Future moment
		};

		for (int i = 0; i < testingData.length; i++)
			this.createNyordelTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Date) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (boolean) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	public void createNyordelTemplate(String loggedUsername, int positionId, String ticker, Date moment, String body, String picture, boolean draftMode, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(loggedUsername);
			Nyordel nyordel = this.nyordelService.create(positionId);
			nyordel.setTicker(ticker);
			nyordel.setMoment(moment);
			nyordel.setBody(body);
			nyordel.setPicture(picture);
			nyordel.setDraftMode(draftMode);

			Nyordel saved = this.nyordelService.save(nyordel);
			this.nyordelRepository.flush();
			Assert.notNull(this.nyordelService.findOne(saved.getId()));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}

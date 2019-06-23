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
import domain.Rookie;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RookieServiceTest extends AbstractTest {

	//Test coverage: 99.8%  // Covered instructions: 503 // Missed instructions: 1 // Total Instructions: 504
	//RookieService coverage: 31.8%  // Covered instructions: 176 // Missed instructions: 377 // Total Instructions: 553
	
	@Autowired
	private RookieService rookieService;
	
	@Autowired
	private CreditCardService creditCardService;

	@Test
	public void registerRookieDriver() {
		Object testingData[][] = {
				{ "Address", "rookietest@acme.com", "(DE)123456789", "Name",
						"+34565756675", "http://i.imgur.com/aaaaa.jpg",
						"Surname", null }, // Positive test case
				{ "Address", "rookietest@acme.com", "(DE)123456789", "",
						"+34565756675", "http://i.imgur.com/aaaaa.jpg",
						"Surname", ConstraintViolationException.class }, // Blank
																			// name
				{ "Address", "BAD EMAIL", "(DE)123456789", "Name", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname",
						IllegalArgumentException.class }, // Email not
															// conforming to
															// constraints
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerRookieTemplate((String) testingData[i][0],
					(String) testingData[i][1], (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4],
					(String) testingData[i][5], (String) testingData[i][6],
					(Class<?>) testingData[i][7]);
	}

	@Test
	public void editRookieDriver() {
		Object testingData[][] = {
				{ "rookie0", "rookie0", "Address", "rookietest@acme.com",
						"(DE)123456789", "Name", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", null }, // Positive
																			// test
																			// case
				{ "rookie0", "rookie0", "Address", "rookietest@acme.com",
						"(DE)123456789", "", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname",
						ConstraintViolationException.class }, // Blank name
				{ "rookie1", "rookie0", "Address", "rookietest@acme.com",
						"(DE)123456789", "Name", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname",
						IllegalArgumentException.class }, // Edit a rookie that
															// is not yourself
		};

		for (int i = 0; i < testingData.length; i++)
			this.editRookieTemplate((String) testingData[i][0],
					super.getEntityId((String) testingData[i][1]),
					(String) testingData[i][2], (String) testingData[i][3],
					(String) testingData[i][4], (String) testingData[i][5],
					(String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	public void registerRookieTemplate(String address, String email,
			String vat, String name, String phoneNumber, String photo,
			String surname, Class<?> expected) {

		Class<?> caught = null;

		try {
			Rookie rookie = this.rookieService.create();
			rookie.setAddress(address);
			rookie.setEmail(email);
			rookie.setVat(vat);
			rookie.setName(name);
			rookie.setPhoneNumber(phoneNumber);
			rookie.setPicture(photo);
			rookie.setSurname(surname);
			rookie.setCreditCard(this.creditCardService.findAll().iterator().next());
			Rookie saved = this.rookieService.save(rookie);
			this.rookieService.flush();
			Assert.notNull(this.rookieService.findOne(saved.getId()));
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void editRookieTemplate(String loggedUsername, int rookieId,
			String address, String email, String vat, String name,
			String phoneNumber, String photo, String surname, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(loggedUsername);
			Rookie rookie = this.rookieService.findOne(rookieId);
			int previousVersion = rookie.getVersion();
			rookie.setAddress(address);
			rookie.setEmail(email);
			rookie.setVat(vat);
			rookie.setName(name);
			rookie.setPhoneNumber(phoneNumber);
			rookie.setPicture(photo);
			rookie.setSurname(surname);
			Rookie saved = this.rookieService.save(rookie);
			this.rookieService.flush();
			Assert.isTrue(saved.getVersion() - previousVersion == 1);
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}

package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Auditor;
import utilities.AbstractTest;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AuditorServiceTest extends AbstractTest {

	//Test coverage: 99.8%  // Covered instructions: 503 // Missed instructions: 1 // Total Instructions: 504
	//AuditorService coverage: 31.8%  // Covered instructions: 176 // Missed instructions: 377 // Total Instructions: 553
	
	@Autowired
	private AuditorService auditorService;

	@Autowired
	private CreditCardService creditCardService;

	@Test
	public void registerAuditorDriver() {
		Object testingData[][] = {
				{ "admin", "Address", "auditortest@acme.com", "(DE)123456789", "Name", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", null }, // Positive test case
				{ "admin", "Address", "auditortest@acme.com", "(DE)123456789", "", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", ConstraintViolationException.class }, // Blank
				};

		for (int i = 0; i < testingData.length; i++)
			this.registerAuditorTemplate((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4],
					(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(Class<?>) testingData[i][8]);
	}

	@Test
	public void editAuditorDriver() {
		Object testingData[][] = {
				{ "auditor0", "auditor0", "Address", "auditortest@acme.com", "(DE)123456789", "Name", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", null }, // Positive
																			// test
																			// case
				{ "auditor0", "auditor0", "Address", "auditortest@acme.com", "(DE)123456789", "", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", ConstraintViolationException.class }, // Blank name
				{ "auditor1", "auditor0", "Address", "auditortest@acme.com", "(DE)123456789", "Name", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", IllegalArgumentException.class }, // Edit a rookie
																										// that
																										// is not
																										// yourself
		};

		for (int i = 0; i < testingData.length; i++)
			this.editAuditorTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]),
					(String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4],
					(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	public void registerAuditorTemplate(String adminUsername, String address, String email, String vat, String name, String phoneNumber,
			String photo, String surname, Class<?> expected) {

		Class<?> caught = null;

		try {

			this.authenticate(adminUsername);
			Auditor auditor = this.auditorService.create();
			auditor.setAddress(address);
			auditor.setEmail(email);
			auditor.setVat(vat);
			auditor.setName(name);
			auditor.setPhoneNumber(phoneNumber);
			auditor.setPicture(photo);
			auditor.setSurname(surname);
			auditor.setCreditCard(this.creditCardService.findAll().iterator().next());
			Auditor saved = this.auditorService.save(auditor);
			this.auditorService.flush();
			Assert.notNull(this.auditorService.findOne(saved.getId()));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void editAuditorTemplate(String loggedUsername, int rookieId, String address, String email, String vat,
			String name, String phoneNumber, String photo, String surname, Class<?> expected) {

		Class<?> caught = null;

		try {
			
			this.authenticate(loggedUsername);
			Auditor auditor = this.auditorService.findOne(rookieId);
			int previousVersion = auditor.getVersion();
			auditor.setAddress(address);
			auditor.setEmail(email);
			auditor.setVat(vat);
			auditor.setName(name);
			auditor.setPhoneNumber(phoneNumber);
			auditor.setPicture(photo);
			auditor.setSurname(surname);
			Auditor saved = this.auditorService.save(auditor);
			this.auditorService.flush();
			Assert.isTrue(saved.getVersion() - previousVersion == 1);
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}

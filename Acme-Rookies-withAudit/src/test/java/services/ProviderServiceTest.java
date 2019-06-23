package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Provider;
import utilities.AbstractTest;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProviderServiceTest extends AbstractTest {

	//Test coverage: 99.8%  // Covered instructions: 335 // Missed instructions: 1 // Total Instructions: 261
	//ProviderService coverage: 78.3%  // Covered instructions: 65 // Missed instructions: 18 // Total Instructions: 83
	
	@Autowired
	private ProviderService providerService;

	@Autowired
	private CreditCardService creditCardService;

	@Test
	public void registerProviderDriver() {
		Object testingData[][] = {
				{ "makeTest", "Address", "providertest@acme.com", "(DE)123456789", "Name", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", null }, // Positive test case
				{ "makeTest", "Address", "providertest@acme.com", "(DE)123456789", "", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", ConstraintViolationException.class }, // Blank
				};

		for (int i = 0; i < testingData.length; i++)
			this.registerProviderTemplate((String) testingData[i][0], (String) testingData[i][1],
					(String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4],
					(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(Class<?>) testingData[i][8]);
	}

	@Test
	public void editProviderDriver() {
		Object testingData[][] = {
				{ "provider0", "provider0", "makeTest", "Address", "providertest@acme.com", "(DE)123456789", "Name", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", null }, // Positive
																			// test
																			// case
				{ "provider0", "provider0", "makeTest", "Address", "providertest@acme.com", "(DE)123456789", "", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", ConstraintViolationException.class }, // Blank name
				{ "provider1", "provider0", "makeTest", "Address", "providertest@acme.com", "(DE)123456789", "Name", "+34565756675",
						"http://i.imgur.com/aaaaa.jpg", "Surname", IllegalArgumentException.class }, // Edit a rookie
																										// that
																										// is not
																										// yourself
		};

		for (int i = 0; i < testingData.length; i++)
			this.editProviderTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]),
					(String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4],
					(String) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
					(String) testingData[i][8], (String) testingData[i][9], (Class<?>) testingData[i][10]);
	}

	public void registerProviderTemplate(String make, String address, String email, String vat, String name, String phoneNumber,
			String photo, String surname, Class<?> expected) {

		Class<?> caught = null;

		try {

			Provider provider = this.providerService.create();
			provider.setMake(make);
			provider.setAddress(address);
			provider.setEmail(email);
			provider.setVat(vat);
			provider.setName(name);
			provider.setPhoneNumber(phoneNumber);
			provider.setPicture(photo);
			provider.setSurname(surname);
			provider.setCreditCard(this.creditCardService.findAll().iterator().next());
			Provider saved = this.providerService.save(provider);
			this.providerService.flush();
			Assert.notNull(this.providerService.findOne(saved.getId()));
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void editProviderTemplate(String loggedUsername, int rookieId, String make, String address, String email, String vat,
			String name, String phoneNumber, String photo, String surname, Class<?> expected) {

		Class<?> caught = null;

		try {
			
			this.authenticate(loggedUsername);
			Provider provider = this.providerService.findOne(rookieId);
			int previousVersion = provider.getVersion();
			provider.setMake(make);
			provider.setAddress(address);
			provider.setEmail(email);
			provider.setVat(vat);
			provider.setName(name);
			provider.setPhoneNumber(phoneNumber);
			provider.setPicture(photo);
			provider.setSurname(surname);
			Provider saved = this.providerService.save(provider);
			this.providerService.flush();
			Assert.isTrue(saved.getVersion() - previousVersion == 1);
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}


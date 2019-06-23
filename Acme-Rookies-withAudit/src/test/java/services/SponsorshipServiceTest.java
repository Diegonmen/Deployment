
package services;

import java.util.Calendar;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.SponsorshipRepository;
import utilities.AbstractTest;
import domain.CreditCard;
import domain.Position;
import domain.Sponsorship;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	//Test coverage: 98.5%  // Covered instructions: 321 // Missed instructions: 5 // Total Instructions: 326
	//SponsorshipService coverage: 52.2%  // Covered instructions: 316 // Missed instructions: 289 // Total Instructions: 605
	
	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	@Autowired
	private PositionService			positionService;
	
	@Autowired
	private ProviderService			providerService;


	@Test
	public void createSponsorshipDriver() {
		Object testingData[][] = {
			{
				"provider0", "https://example.org/img.png", "https://example.org", "position0", null
			},								// Positive test case
			{
				"provider0", "https://example.org/img.png", "https://example.org", "position1", IllegalArgumentException.class
			},								// Position in draft mode
			{
				"provider0", "https://example.org/img.png", "https://example.org", "position3", IllegalArgumentException.class
			},								// Cancelled position
			{
				"provider3", "https://example.org/img.png", "https://example.org", "position0", IllegalArgumentException.class
			},								// Provider has an expired credit card
			{
				"provider0", "", "https://example.org", "position0", ConstraintViolationException.class
			}, 								// Blank banner
		};

		for (int i = 0; i < testingData.length; i++)
			this.createSponsorshipTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], super.getEntityId((String) testingData[i][3]), (Class<?>) testingData[i][4]);
	}

	@Test
	public void editSponsorshipDriver() {
		Object testingData[][] = {
			{
				"provider0", "sponsorship0", "https://example.org/img.png", "https://example.org", null
			},		// Positive test case
			{
				"provider0", "sponsorship0", "", "https://example.org", ConstraintViolationException.class
			}, 		// Blank banner
			{
				"provider1", "sponsorship0", "https://example.org/img.png", "https://example.org", IllegalArgumentException.class
			}, 		// Edit a sponsorship which is not yours
		};

		for (int i = 0; i < testingData.length; i++)
			this.editSponsorshipTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	@Test
	public void deleteSponsorshipDriver() {
		Object testingData[][] = {
			{
				"provider0", "sponsorship0", null
			},								// Positive test case
			{
				"provider1", "sponsorship0", IllegalArgumentException.class
			}, 								// Sponsorship is not yours
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteSponsorshipTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	public void createSponsorshipTemplate(String loggedUsername, String banner, String url, int positionId, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(loggedUsername);
			Sponsorship sponsorship = this.sponsorshipService.create();
			Position position = this.positionService.findOne(positionId);
			sponsorship.setBanner(banner);
			sponsorship.setUrl(url);

			// This code can be found in the reconstruct method in SponsorshipService
			// ======================================================================
			CreditCard creditCard = providerService.findByPrincipal().getCreditCard();
			Calendar cal = Calendar.getInstance();
			if ((creditCard.getExpirationYear() < cal.get(Calendar.YEAR) % 100) || (creditCard.getExpirationYear() == cal.get(Calendar.YEAR) % 100 && creditCard.getExpirationMonth() < cal.get(Calendar.MONTH) + 1))
				throw new IllegalArgumentException("sponsorship.expired.card");
			// ======================================================================
			
			Sponsorship saved = this.sponsorshipService.save(sponsorship, position);
			this.sponsorshipRepository.flush();
			Assert.notNull(this.sponsorshipService.findOne(saved.getId()));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void editSponsorshipTemplate(String loggedUsername, int sponsorshipId, String banner, String url, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(loggedUsername);
			Sponsorship sponsorship = this.sponsorshipService.findOneToEdit(sponsorshipId);
			int previousVersion = sponsorship.getVersion();
			sponsorship.setBanner(banner);
			sponsorship.setUrl(url);

			// This code can be found in the reconstruct method in SponsorshipService
			// ======================================================================
			CreditCard creditCard = providerService.findByPrincipal().getCreditCard();
			Calendar cal = Calendar.getInstance();
			if ((creditCard.getExpirationYear() < cal.get(Calendar.YEAR) % 100) || (creditCard.getExpirationYear() == cal.get(Calendar.YEAR) % 100 && creditCard.getExpirationMonth() < cal.get(Calendar.MONTH) + 1))
				throw new IllegalArgumentException("sponsorship.expired.card");
			// ======================================================================
			
			Sponsorship saved = this.sponsorshipService.save(sponsorship, null);
			this.sponsorshipRepository.flush();
			Assert.isTrue(saved.getVersion() - previousVersion == 1);
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void deleteSponsorshipTemplate(String loggedUsername, int sponsorshipId, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(loggedUsername);
			this.sponsorshipService.delete(sponsorshipId);
			this.sponsorshipRepository.flush();
			Assert.isNull(this.sponsorshipService.findOne(sponsorshipId));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}

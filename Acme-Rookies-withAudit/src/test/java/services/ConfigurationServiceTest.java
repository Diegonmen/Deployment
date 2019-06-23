
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

import domain.Configuration;
import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ConfigurationServiceTest extends AbstractTest {

	//Test coverage: 99.6%  // Covered instructions: 260 // Missed instructions: 1 // Total Instructions: 261
	//ConfigurationService coverage: 78.3%  // Covered instructions: 65 // Missed instructions: 18 // Total Instructions: 83
	
	@Autowired
	private ConfigurationService	configurationService;


	@SuppressWarnings("unchecked")
	@Test
	public void editConfigurationDriver() {
		Collection<String> spamWords = new ArrayList<>();
		spamWords.add("motherfucker");
		spamWords.add("noob");
		spamWords.add("fuck");
		spamWords.add("nigger");
		spamWords.add("porn");
		spamWords.add("spamWord0");
		Collection<String> spamWordsBad = new ArrayList<>();
		spamWordsBad.add(" ");
		Object testingData[][] = {
			{
				"admin", "https://i.imgur.com/7b8lu4b.png", 10, spamWords, null
				// Good editing
			}, {
				"admin", "https://i.imgur.com/7b8lu4b.png", 10, spamWordsBad, IllegalArgumentException.class
			}, {
				"rookie0", "https://i.imgur.com/7b8lu4b.png", 10, spamWords, IllegalArgumentException.class
			}, {
				"admin", "", 10, spamWords, ConstraintViolationException.class
				// BannerURL must not be blank
			}, {
				"admin", "https://i.imgur.com/7b8lu4b.png", 0, spamWords, ConstraintViolationException.class
				// Cache must be in range 1,24
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editConfigurationTemplate((String) testingData[i][0], (String) testingData[i][1], (int) testingData[i][2], (Collection<String>) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	public void editConfigurationTemplate(String username, String bannerURL, int cache, Collection<String> spamWords, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Configuration configuration = this.configurationService.findConfiguration();
			int previousVersion = configuration.getVersion();
			configuration.setBannerURL(bannerURL);
			configuration.setCache(cache);
			configuration.setSpamWords(spamWords);
			Configuration saved = this.configurationService.save(configuration);
			this.configurationService.flush();
			Assert.isTrue(saved.getVersion() - previousVersion == 1);
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);

	}

}

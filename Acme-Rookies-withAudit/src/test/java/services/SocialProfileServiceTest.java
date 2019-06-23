
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
import domain.Actor;
import domain.SocialProfile;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SocialProfileServiceTest extends AbstractTest {

	//Test coverage: 99.8%  // Covered instructions: 490 // Missed instructions: 1 // Total Instructions: 491
	//SocialProfileService coverage: 75%  // Covered instructions: 84 // Missed instructions: 28 // Total Instructions: 112
	
	@Autowired
	private SocialProfileService socialProfileService;
	
	@Autowired
	private ActorService actorService;

	@Test
	public void createSocialProfileDriver() {
		Object testingData[][] = {
			{
				"rookie0", "Nick", "Social Network", "https://www.redsocial.com/", null
			},
			{
				"rookie0", "", "Social Network", "https://www.redsocial.com/", ConstraintViolationException.class
			},
			{
				"rookie0", "Nick", "Social Network", "No es una URL", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.createSocialProfileTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	@Test
	public void editSocialProfileDriver() {
		Object testingData[][] = {
				{
					"admin", "socialProfile0", "Nick", "Social Network", "https://www.redsocial.com/", null
				},
				{
					"admin", "socialProfile0", "", "Social Network", "https://www.redsocial.com/", ConstraintViolationException.class
				},
				{
					"admin", "socialProfile0", "Nick", "Social Network", "No es una URL", ConstraintViolationException.class
				},
				{
					"rookie0", "socialProfile0", "Nick", "Social Network 2", "https://www.redsocial.com/", IllegalArgumentException.class
				},
		};

		for (int i = 0; i < testingData.length; i++)
			this.editSocialProfileTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	
	@Test
	public void deleteSocialProfileDriver() {
		Object testingData[][] = {
				{
					"admin", "socialProfile0", null
				},
				{
					"rookie0", "socialProfile1", IllegalArgumentException.class
				},
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteSocialProfileTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	public void createSocialProfileTemplate(String username, String nick, String socialNetwork, String link, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			SocialProfile socialProfile = socialProfileService.create();
			socialProfile.setNick(nick);
			socialProfile.setSocialNetwork(socialNetwork);
			socialProfile.setLink(link);
			SocialProfile result = socialProfileService.save(socialProfile);
			socialProfileService.flush();
			Assert.isTrue(socialProfileService.exists(result.getId()));
			Actor logged = actorService.findByPrincipal();
			Assert.isTrue(logged.getSocialProfiles().contains(result));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void editSocialProfileTemplate(String username, int socialProfileId, String nick, String socialNetwork, String link, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			SocialProfile socialProfile = socialProfileService.findOne(socialProfileId);
			int previousVersion = socialProfile.getVersion();
			socialProfile.setNick(nick);
			socialProfile.setSocialNetwork(socialNetwork);
			socialProfile.setLink(link);
			SocialProfile result = socialProfileService.save(socialProfile);
			socialProfileService.flush();
			Assert.isTrue(result.getVersion() - previousVersion == 1);
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	
	public void deleteSocialProfileTemplate(String username, int socialProfileId, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			SocialProfile socialProfile = socialProfileService.findOne(socialProfileId);
			socialProfileService.delete(socialProfile.getId());
			socialProfileService.flush();
			Assert.isNull(socialProfileService.findOne(socialProfileId));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}

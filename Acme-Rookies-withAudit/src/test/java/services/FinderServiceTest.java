
package services;

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

import utilities.AbstractTest;
import domain.Finder;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FinderServiceTest extends AbstractTest {

	//Test coverage: 99.6%  // Covered instructions: 269 // Missed instructions: 1 // Total Instructions: 270
	//FinderService coverage: 48.3%  // Covered instructions: 113 // Missed instructions: 121 // Total Instructions: 264
	
	@Autowired
	private FinderService finderService;
	
	@Autowired
	private PositionService positionService;

	@Test
	public void editFinderDriver() {
		Object testingData[][] = {
				{
					"rookie0", "finder0", "my god", 20., 20000., new Date(), null
				}, // Positive test case
				{
					"rookie0", "finder0", null, null, null, null, null
				}, // Another positive test case (no filters, show all positions)
				{
					"rookie0", "finder1", null, null, null, null, IllegalArgumentException.class
				}, // Try to edit a finder from another user
				{
					"rookie0", "finder0", null, -25., null, null, ConstraintViolationException.class
				}, // Invalid minimum salary
		};

		for (int i = 0; i < testingData.length; i++)
			this.editFinderTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Double) testingData[i][3], (Double) testingData[i][4], (Date) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	
	@Test
	public void clearFinderTest() {
		this.authenticate("rookie0");
		finderService.clear();
		finderService.flush();
		Finder finder = finderService.findFinderByPrincipal();
		Assert.isTrue(finder.getPositions().containsAll(positionService.findPositions(null, null, null, null)));
		this.unauthenticate();
	}
	
	@Test
	public void findFinderSearchingPositionTest() {
		this.authenticate("rookie0");
		finderService.clear();
		finderService.flush();
		Finder finder = finderService.findFinderByPrincipal();
		finder.setKeyword("Battle Tendency");
		Finder result = finderService.save(finder);
		finderService.flush();
		Collection<Finder> findersSearchingPosition = finderService.findFindersSearchingPosition(super.getEntityId("position2"));
		Assert.isTrue(findersSearchingPosition.contains(result));
		this.unauthenticate();
	}

	public void editFinderTemplate(String username, int finderId, String keyword, Double minimumSalary, Double maximumSalary, Date deadline, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Finder finder = finderService.findOne(finderId);
			int previousVersion = finder.getVersion();
			finder.setKeyword(keyword);
			finder.setMinimumSalary(minimumSalary);
			finder.setMaximumSalary(maximumSalary);
			finder.setDeadline(deadline);
			Finder result = finderService.save(finder);
			finderService.flush();
			Assert.isTrue(result.getVersion() - previousVersion == 1);
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}

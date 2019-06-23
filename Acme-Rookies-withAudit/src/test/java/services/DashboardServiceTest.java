
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Company;
import domain.DomainEntity;
import domain.Position;
import domain.Provider;
import domain.Rookie;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DashboardServiceTest extends AbstractTest {

	//Test coverage: 100%  // Covered instructions: 736 // Missed instructions: 0 // Total Instructions: 736
	//AdministratorService coverage: 11.5%  // Covered instructions: 70 // Missed instructions: 537 // Total Instructions: 607

	@Autowired
	private AdministratorService	administratorService;


	@Test
	public void avgMinMaxStdevDriver() {

		Double[] avgMinMax1 = this.administratorService.findAvgMinMaxStdevPositionsPerCompany();
		Double[] avgMinMax2 = this.administratorService.findAvgMinMaxStdevApplicationsPerRookie();
		Double[] avgMinMax3 = this.administratorService.findAvgMinMaxStdevCurriculaPerRookie();
		Double[] avgMinMax4 = this.administratorService.findAvgMinMaxStdevResultsPerFinder();
		Double[] avgMinMax5 = this.administratorService.findAvgMinMaxStdevSalaries();
		Double[] avgMinMax6 = this.administratorService.findAvgMinMaxStdevAuditScorePerPosition();
		Double[] avgMinMax7 = this.administratorService.findAvgMinMaxStdevScorePerCompany();
		Double[] avgMinMax8 = this.administratorService.findAvgMinMaxStdevItemsPerProvider();
		Double[] avgMinMax9 = this.administratorService.findAvgMinMaxStdevSponsorshipsPerProvider();
		Double[] avgMinMax10 = this.administratorService.findAvgMinMaxStdevSponsorshipsPerPosition();

		Object testingData[][] = {
			{
				avgMinMax1, new Double[] {
					2.0, 1.0, 3.0, 0.8164965805194777
				}, null
			// Positive test case 1st average, min, max and standard deviation
			}, {
				avgMinMax1, new Double[] {
					77d, 1.0, 3.0, 0.8164965805194777
				}, IllegalArgumentException.class
			// Negative test case 1st average, min, max and standard deviation
			}, {
				avgMinMax2, new Double[] {
					1.0, 0.0, 2.0, 0.7071067811865476
				}, null
			// Positive test case 2nd average, min, max and standard deviation
			}, {
				avgMinMax2, new Double[] {
					1.0, 0.0, 23.0, 0.7071067811865476
				}, IllegalArgumentException.class
			// Negative test case 2nd average, min, max and standard deviation
			}, {
				avgMinMax3, new Double[] {
					2.0, 0.0, 4.0, 1.4142135623730951
				}, null
			// Positive test case 3rd average, min, max and standard deviation
			}, {
				avgMinMax3, new Double[] {
					2.0, 0.0, 2344.0, 1.4142135623730951
				}, IllegalArgumentException.class
			// Negative test case 3rd average, min, max and standard deviation
			}, {
				avgMinMax4, new Double[] {
					0.25, 0.0, 1.0, 0.4330127018922193
				}, null
			// Positive test case 4th average, min, max and standard deviation
			}, {
				avgMinMax4, new Double[] {
					44.0, 0.0, 0.0, 0.0
				}, IllegalArgumentException.class
			// Negative test case 4th average, min, max and standard deviation
			}, {
				avgMinMax5, new Double[] {
					3916.6666666666665, 3000.0, 4500.0, 533.5936864527389
				}, null
			// Positive test case 5th average, min, max and standard deviation
			}, {
				avgMinMax5, new Double[] {
					3916.6666666666665, 3000.0, 395523434.0, 533.5936864527389
				}, IllegalArgumentException.class
			// Negative test case 5th average, min, max and standard deviation
			}, {
				avgMinMax6, new Double[] {
					7d, 6d, 8d, 0.816496580927726d
				}, null
			}, {
				avgMinMax6, new Double[] {
					70d, 6d, 8d, null
				}, IllegalArgumentException.class
			}, {
				avgMinMax7, new Double[] {
					4.666666666666667, 1.0, 8.0, 2.8674417556808747
				}, null
			}, {
				avgMinMax7, new Double[] {
					10.5d, 0d, 1d, 0.5d
				}, IllegalArgumentException.class
			}, {
				avgMinMax8, new Double[] {
					1d, 0d, 3d, 1.224744871391589d
				}, null
			}, {
				avgMinMax8, new Double[] {
					123d, 0d, 3d, 1.224744871391589d
				}, IllegalArgumentException.class
			}, {
				avgMinMax9, new Double[] {
					0.75d, 0d, 2d, 0.82915619758885d
				}, null
			}, {
				avgMinMax9, new Double[] {
					1230.75d, 0d, 2d, 0.82915619758885d
				}, IllegalArgumentException.class
			}, {
				avgMinMax10, new Double[] {
					0.5d, 0d, 2d, 0.7637626156077555d
				}, null
			}, {
				avgMinMax10, new Double[] {
					1231450.5d, 0d, 2d, 0.7637626156077555d
				}, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.avgMinMaxStdevTemplate((Double[]) testingData[i][0], (Double[]) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void listDriver() {
		List<Company> companies = this.administratorService.findCompaniesMorePositions();
		List<Rookie> rookies = this.administratorService.findRookiesMoreApplications();
		List<Position> greatestSalary = this.administratorService.selectPositionsGreatestSalary();
		List<Position> lowestSalary = this.administratorService.selectPositionsLowestSalary();
		List<Company> highestScores = this.administratorService.findCompaniesHighestScore();
		List<Provider> top5Providers = this.administratorService.findTop5ProvidersByItemCount();
		List<Provider> providers10Percent = this.administratorService.findProvidersWith10PercentMoreSponsorships();

		Object testingData[][] = {
			{
				companies, "company1", null
			// Positive test case 1st list
			}, {
				companies, "company2", IllegalArgumentException.class
			// Negative test case 1st list
			}, {
				rookies, "rookie0", null
			// Positive test case 2nd list
			}, {
				rookies, "rookie1", IllegalArgumentException.class
			// Negative test case 2nd list
			}, {
				greatestSalary, "position2", null
			// Positive test case 3rd list
			}, {
				greatestSalary, "position1", IllegalArgumentException.class
			// Negative test case 3rd list
			}, {
				lowestSalary, "position0", null
			// Positive test case 4th list
			}, {
				lowestSalary, "position1", IllegalArgumentException.class
			// Negative test case 4th list
			}, {
				highestScores, "company1", null
			// Positive test case 5th list
			}, {
				highestScores, "company2", IllegalArgumentException.class
			// Negative test case 5th list
			}, {
				top5Providers, "provider0", null
			// Positive test case 5th list
			}, {
				top5Providers, "provider2", IllegalArgumentException.class
			// Negative test case 5th list
			}, {
				providers10Percent, "provider0", null
			// Positive test case 5th list
			}, {
				providers10Percent, "provider2", IllegalArgumentException.class
			// Negative test case 5th list
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listTemplate((List<DomainEntity>) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	@Test
	public void ratioDriver() {
		String ratio = this.administratorService.findRatioFinders(null);
		Double avgSalaryHighestScores = this.administratorService.averageSalaryForHighestScorePositions();
		Object testingData[][] = {
			{
				ratio, "0.3333333333333333", null
			// Positive test ratio
			}, {
				ratio, "zapato", IllegalArgumentException.class
			// Negative test ratio
			}, {
				avgSalaryHighestScores, 3500d, null
			//Positive test ratio
			}, {
				avgSalaryHighestScores, 128371273d, IllegalArgumentException.class
			// Negative test ratio
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.ratioTemplate(testingData[i][0], testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void avgMinMaxStdevTemplate(Double[] input, Double[] output, Class<?> expected) {

		Class<?> caught = null;

		try {
			Assert.isTrue(input[0].equals(output[0]) && input[1].equals(output[1]) && input[2].equals(output[2]) && (input[3] == null || input[3].equals(output[3])));
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void listTemplate(List<DomainEntity> list, String firstElem, Class<?> expected) {

		Class<?> caught = null;

		try {
			Assert.isTrue(list.get(0).equals(Integer.valueOf(super.getEntityId(firstElem))));
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void ratioTemplate(Object original, Object test, Class<?> expected) {

		Class<?> caught = null;

		try {
			Assert.isTrue(original.equals(test));
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}

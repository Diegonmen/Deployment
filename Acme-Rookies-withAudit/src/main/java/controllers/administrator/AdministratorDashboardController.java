
package controllers.administrator;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import controllers.AbstractController;
import domain.Company;
import domain.Position;
import domain.Provider;
import domain.Rookie;

@Controller
@RequestMapping("/dashboard/administrator")
public class AdministratorDashboardController extends AbstractController {

	// Services

	@Autowired
	private AdministratorService	administratorService;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(Locale locale) {
		ModelAndView result;
		result = new ModelAndView("administrator/dashboard");

		Double[] avgMinMax1 = this.administratorService.findAvgMinMaxStdevPositionsPerCompany();
		Double[] avgMinMax2 = this.administratorService.findAvgMinMaxStdevApplicationsPerRookie();
		List<Company> companies = this.administratorService.findCompaniesMorePositions();
		List<Rookie> rookies = this.administratorService.findRookiesMoreApplications();
		Double[] avgMinMax3 = this.administratorService.findAvgMinMaxStdevCurriculaPerRookie();
		Double[] avgMinMax4 = this.administratorService.findAvgMinMaxStdevResultsPerFinder();
		Double[] avgMinMax5 = this.administratorService.findAvgMinMaxStdevSalaries();
		List<Position> greatestSalary = this.administratorService.selectPositionsGreatestSalary();
		List<Position> lowestSalary = this.administratorService.selectPositionsLowestSalary();
		String ratio = this.administratorService.findRatioFinders(locale);
		Double[] avgMinMax6 = this.administratorService.findAvgMinMaxStdevAuditScorePerPosition();
		Double[] avgMinMax7 = this.administratorService.findAvgMinMaxStdevScorePerCompany();
		Double[] avgMinMax8 = this.administratorService.findAvgMinMaxStdevItemsPerProvider();
		Double[] avgMinMax9 = this.administratorService.findAvgMinMaxStdevSponsorshipsPerProvider();
		Double[] avgMinMax10 = this.administratorService.findAvgMinMaxStdevSponsorshipsPerPosition();
		List<Company> highestScores = this.administratorService.findCompaniesHighestScore();
		Double avgSalaryHighestScores = this.administratorService.averageSalaryForHighestScorePositions();
		List<Provider> top5Providers = this.administratorService.findTop5ProvidersByItemCount();
		List<Provider> providers10Percent = this.administratorService.findProvidersWith10PercentMoreSponsorships();

		result.addObject("avgMinMaxStdevPositionsPerCompany", avgMinMax1);
		result.addObject("avgMinMaxStdevApplicationsPerRookie", avgMinMax2);
		result.addObject("avgMinMaxStdevCurriculaPerRookie", avgMinMax3);
		result.addObject("avgMinMaxStdevResultsPerFinder", avgMinMax4);
		result.addObject("avgMinMaxStdevSalaries", avgMinMax5);
		result.addObject("avgMinMax6", avgMinMax6);
		result.addObject("avgMinMax7", avgMinMax7);
		result.addObject("avgMinMax8", avgMinMax8);
		result.addObject("avgMinMax9", avgMinMax9);
		result.addObject("avgMinMax10", avgMinMax10);
		result.addObject("companiesMorePositions", companies);
		result.addObject("rookiesMoreApplications", rookies);
		result.addObject("positionsGreatestSalary", greatestSalary);
		result.addObject("positionsLowestSalary", lowestSalary);
		result.addObject("ratioFinders", ratio);
		result.addObject("highestScores", highestScores);
		result.addObject("avgSalaryHighestScores", avgSalaryHighestScores);
		result.addObject("top5Providers", top5Providers);
		result.addObject("providers10Percent", providers10Percent);

		return result;

	}

	@RequestMapping(value = "/auditorScore", method = RequestMethod.GET)
	public ModelAndView computeAuditorScore() {
		ModelAndView result;
		result = new ModelAndView("redirect:/");

		this.administratorService.computeAuditorScore();

		return result;

	}

}

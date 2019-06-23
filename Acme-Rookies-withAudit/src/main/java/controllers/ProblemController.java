
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CompanyService;
import services.PositionService;
import services.ProblemService;
import domain.Company;
import domain.Position;
import domain.Problem;

@Controller
@RequestMapping("/problem")
public class ProblemController extends AbstractController {

	@Autowired
	private ProblemService	problemService;

	@Autowired
	private CompanyService	companyService;
	
	@Autowired
	private PositionService positionService;


	//Show
			@RequestMapping("/show")
			public ModelAndView display(@RequestParam final int problemId) {
				ModelAndView result;
				Problem problem = this.problemService.findOne(problemId);
				
				result = new ModelAndView("problem/show");
				result.addObject("problem", problem);
				result.addObject("requestURI", "problem/show.do?positionId=" + problemId);

				return result;
			}
	@RequestMapping(value = "/company/list", method = RequestMethod.GET)
	public ModelAndView listForCompanies() {
		ModelAndView result;

		final Company company = this.companyService.findByPrincipal();
		final Collection<Problem> problems = company.getProblems();

		result = new ModelAndView("problem/list");

		result.addObject("requestURI", "problem/company/list.do");
		result.addObject("problems", problems);

		return result;
	}

	@RequestMapping(value = "/company/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Problem problem;

		problem = this.problemService.create();
		result = this.createEditModelAndView(problem);

		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int problemId) {
		ModelAndView result;

		final Problem problem = this.problemService.findOneToEdit(problemId);
		Assert.notNull(problem);
		result = this.createEditModelAndView(problem);

		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Problem problem, final BindingResult binding) {

		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(problem);
		else
			try {
				this.problemService.save(problem);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(problem, "problem.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.POST, params="delete")
	public ModelAndView delete(final Problem problem, final BindingResult binding){
		ModelAndView result;

		try{
			this.problemService.delete(problem.getId());
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops){
			
			if (oops.getMessage().contains("has.applications"))
				result = this.createEditModelAndView(problem, "problem.has.applications");
			else
				result = this.createEditModelAndView(problem, "problem.commit.error");
		}
		return result;
	}

	// Ancillary methods

	public ModelAndView createEditModelAndView(final Problem problem) {
		ModelAndView result;

		result = this.createEditModelAndView(problem, null);

		return result;
	}

	public ModelAndView createEditModelAndView(final Problem problem, final String messageError) {
		ModelAndView result;
		Collection<Position> positions = positionService.findPositionsNotCancelled();
		if (problem.getId() == 0)
			result = new ModelAndView("problem/create");
		else
			result = new ModelAndView("problem/edit");

		result.addObject("problem", problem);
		result.addObject("message", messageError);
		result.addObject("positions", positions);

		return result;

	}
}

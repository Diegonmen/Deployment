
package controllers;

import java.util.Collection;
import java.util.Set;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.CompanyService;
import services.PositionService;
import services.ProblemService;
import services.RookieService;
import services.SponsorshipService;
import domain.Position;
import domain.Problem;

@Controller
@RequestMapping("/position")
public class PositionController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private PositionService		positionService;
	@Autowired
	private CompanyService		companyService;
	@Autowired
	private ProblemService		problemService;
	@Autowired
	private RookieService		rookieService;
	@Autowired
	private ApplicationService	applicationService;
	@Autowired
	private SponsorshipService	sponsorshipService;


	// Constructors -----------------------------------------------------------

	public PositionController() {
		super();
	}
	//List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		boolean mines = false;

		final Collection<Position> positions = this.positionService.findPPublicPositions();
		result = new ModelAndView("position/list");

		try {
			Collection<Position> positionsApplied = this.applicationService.checkForApplyingToPositions(this.rookieService.findByPrincipal().getId());
			result.addObject("positionsApplied", positionsApplied);
		} catch (Throwable oops) {

		}

		result.addObject("requestURI", "position/list.do");
		result.addObject("positions", positions);
		result.addObject("areMines", mines);
		result.addObject("showSearch", true);

		return result;
	}
	@RequestMapping(value = "/company/list", method = RequestMethod.GET)
	public ModelAndView listFromCompany() {
		ModelAndView result;
		boolean mines = true;
		final Collection<Position> positions = this.companyService.findByPrincipal().getPositions();

		result = new ModelAndView("position/list");

		result.addObject("requestURI", "position/company/list.do");
		result.addObject("positions", positions);
		result.addObject("areMines", mines);

		return result;
	}

	@RequestMapping(value = "fromCompany/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int companyId) {
		ModelAndView result;
		boolean mines = false;

		final Collection<Position> positions = this.positionService.findPositionsInFinalFromCompanyId(companyId);
		result = new ModelAndView("position/list");

		result.addObject("requestURI", "position/fromCompany/list.do");
		result.addObject("positions", positions);
		result.addObject("areMines", mines);

		return result;
	}

	//Show
	@RequestMapping("/show")
	public ModelAndView display(@RequestParam final int positionId) {
		ModelAndView result;
		result = new ModelAndView("position/show");

		Position position = this.positionService.findOne(positionId);
		

		try {
			final Collection<Position> positions = this.companyService.findByPrincipal().getPositions();
			boolean canCancel = false;

			if (this.applicationService.findApplicationsForPosition(positionId).isEmpty() && !position.isDraftMode() && !position.isCancelled() && positions.contains(position))
				canCancel = true;
			result.addObject("canCancel", canCancel);

			if (position.isDraftMode())
				position = this.positionService.findOneToEdit(positionId);
		} catch (Throwable oops) {

		}

		Set<Problem> problems = this.problemService.findProblemsOfPosition(positionId);

		result.addObject("sponsorship", this.sponsorshipService.getRandomSponsorshipByPositionId(positionId));
		result.addObject("position", position);
		result.addObject("problems", problems);

		result.addObject("requestURI", "position/show.do?positionId=" + positionId);

		return result;
	}

	//Company methods

	@RequestMapping(value = "/company/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int positionId) {

		ModelAndView result;
		this.positionService.cancel(positionId);
		result = new ModelAndView("redirect:list.do");
		result.addObject("requestURI", "position/company/list.do");
		return result;
	}

	@RequestMapping(value = "/company/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Position position;

		position = this.positionService.create();
		result = this.createEditModelAndView(position);
		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int positionId) {
		ModelAndView result;
		Position position;

		position = this.positionService.findOneToEdit(positionId);
		Assert.notNull(position);
		result = this.createEditModelAndView(position);

		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Position position, final BindingResult binding) {
		ModelAndView result;

		try {
			position = this.positionService.reconstruct(position, binding);
			this.positionService.save(position);
			result = new ModelAndView("redirect:/position/company/list.do");

		} catch (ValidationException oops) {
			result = this.createEditModelAndView(position);
		} catch (Throwable oops) {
			if (oops.getMessage().contains("You must set this position to at least 2 problems to save in final mode"))
				result = this.createEditModelAndView(position, "position.need.problems");
			else
				result = this.createEditModelAndView(position, "commit.error");

		}

		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Position position, final BindingResult binding) {
		ModelAndView result;

		try {
			this.positionService.delete(position.getId());
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(position, "problem.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam String q) {
		ModelAndView result;

		final Collection<Position> positions = this.positionService.findPositionsByKeyword(q);
		result = new ModelAndView("position/list");

		result.addObject("requestURI", "position/list.do");
		result.addObject("positions", positions);
		result.addObject("showSearch", true);

		return result;
	}

	//Ancilliary method
	protected ModelAndView createEditModelAndView(final Position position) {
		ModelAndView result;
		result = this.createEditModelAndView(position, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Position position, final String messageCode) {
		ModelAndView result;

		if (position.getId() > 0)
			result = new ModelAndView("position/edit");
		else
			result = new ModelAndView("position/create");

		result.addObject("position", position);
		result.addObject("message", messageCode);

		return result;
	}

}

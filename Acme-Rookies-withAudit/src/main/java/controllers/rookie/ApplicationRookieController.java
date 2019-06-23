
package controllers.rookie;

import java.util.Collection;

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
import services.RookieService;
import controllers.AbstractController;
import domain.Application;
import domain.Curricula;
import domain.Rookie;

@Controller
@RequestMapping("/application/rookie")
public class ApplicationRookieController extends AbstractController {

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private RookieService		rookieService;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam int applicationId) {
		ModelAndView result;
		Application application;

		application = this.applicationService.findOne(applicationId);

		result = new ModelAndView("application/show");
		result.addObject("application", application);
		result.addObject("requestURI", "application/rookie/show.do");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Application> applications;

		applications = this.rookieService.findByPrincipal().getApplications();

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);
		result.addObject("rookieLogged", true);
		result.addObject("requestURI", "application/rookie/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int positionId) {
		ModelAndView result;
		Application application;

		application = this.applicationService.create();

		Assert.isNull(this.applicationService.checkExistingApplications(positionId, this.rookieService.findByPrincipal().getId()));

		result = this.createEditModelAndView(application);
		result.addObject("positionId", positionId);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int applicationId) {
		ModelAndView result;
		Application application;

		application = this.applicationService.findOneToEditRookie(applicationId);
		Assert.notNull(application);

		result = this.createEditModelAndView(application);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam int positionId, Application application, BindingResult binding) {
		ModelAndView result;

		try {
			application = this.applicationService.reconstruct(application, binding);
			this.applicationService.save(application, positionId);
			result = new ModelAndView("redirect:list.do");
		} catch (ValidationException e) {
			result = this.createEditModelAndView(application);
		} catch (Throwable oops) {
			result = this.createEditModelAndView(application, "application.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "submit")
	public ModelAndView submit(Application application, BindingResult binding) {
		ModelAndView result;

		try {
			application = this.applicationService.reconstruct(application, binding);
			this.applicationService.submit(application);
			result = new ModelAndView("redirect:list.do");
		} catch (ValidationException e) {
			result = this.createEditModelAndView(application);
		} catch (Throwable oops) {
			result = this.createEditModelAndView(application, "application.commit.error");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(Application application) {
		ModelAndView result;

		result = this.createEditModelAndView(application, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Application application, final String messageCode) {
		ModelAndView result;

		if (application.getId() == 0)
			result = new ModelAndView("application/create");
		else
			result = new ModelAndView("application/edit");

		Rookie rookie = this.rookieService.findByPrincipal();
		Collection<Curricula> curriculas = this.applicationService.selectCurricula(rookie.getId());

		result.addObject("curriculas", curriculas);
		result.addObject("application", application);
		result.addObject("message", messageCode);

		return result;
	}
}


package controllers.company;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.CompanyService;
import controllers.AbstractController;
import domain.Application;

@Controller
@RequestMapping("/application/company")
public class ApplicationCompanyController extends AbstractController {

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private CompanyService 		companyService;

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam int applicationId) {
		ModelAndView result;
		Application application;

		application = this.applicationService.findOne(applicationId);

		result = new ModelAndView("application/show");
		result.addObject("application", application);
		result.addObject("requestURI", "application/company/show.do");

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int positionId) {
		ModelAndView result;
		Collection<Application> applications;

		Assert.notNull(this.applicationService.checkOwner(this.companyService.findByPrincipal().getId(), positionId));
		applications = this.applicationService.findApplicationsForPosition(positionId);

		result = new ModelAndView("application/list");
		result.addObject("applications", applications);
		result.addObject("requestURI", "application/company/list.do");

		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam int applicationId){
		ModelAndView result;
		Application application = this.applicationService.findOne(applicationId);
		application = this.applicationService.findOneToEditCompany(applicationId, application.getProblem().getPosition().getId());

		try{
			this.applicationService.accept(application);
			result = new ModelAndView("redirect:list.do?positionId=" + application.getProblem().getPosition().getId());
		}
		catch (Throwable oops) {
			result = this.createEditModelAndView(application, "application.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam int applicationId){
		ModelAndView result;
		Application application = this.applicationService.findOne(applicationId);
		application = this.applicationService.findOneToEditCompany(applicationId, application.getProblem().getPosition().getId());

		try{
			this.applicationService.reject(application);
			result = new ModelAndView("redirect:list.do?positionId=" + application.getProblem().getPosition().getId());
		}
		catch (Throwable oops) {
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

		result = new ModelAndView("application/edit");

		result.addObject("application", application);
		result.addObject("message", messageCode);

		return result;
	}
}

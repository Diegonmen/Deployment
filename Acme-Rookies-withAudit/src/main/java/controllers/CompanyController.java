package controllers;

import java.util.Arrays;
import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Company;
import forms.CompanyForm;
import services.CompanyService;

@Controller
@RequestMapping("/company")
public class CompanyController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private CompanyService companyService;
	// Constructors -----------------------------------------------------------

	public CompanyController() {
		super();
	}
	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		final Collection<Company> companys = this.companyService.findAll();
		result = new ModelAndView("company/list");

		result.addObject("requestURI", "company/list.do");
		result.addObject("companys", companys);

		return result;
	}

	// Show
	@RequestMapping("fromPosition/show")
	public ModelAndView display(@RequestParam final int positionId) {
		ModelAndView result;
		Company company = this.companyService.findByPositionId(positionId);

		result = new ModelAndView("company/show");
		result.addObject("company", company);
		result.addObject("requestURI", "company/show.do?positionId=" + positionId);

		return result;
	}

	@RequestMapping("/show")
	public ModelAndView displayMe(@RequestParam (required = false) final Integer companyId) {
		ModelAndView result;
		Company company = new Company();
		boolean isMe = false;
		result = new ModelAndView("company/show");
		if (companyId == null) {
			company = this.companyService.findByPrincipal();
			isMe = true;
//			result.addObject("requestURI", "company/show.do");
		} else {
			company = this.companyService.findOne(companyId);
//			result.addObject("requestURI", "company/show.do?companyd=" + companyId);
		}

		result.addObject("requestURI", "company/show.do?companyd=" + companyId);
		result.addObject("company", company);
		result.addObject("isMe", isMe);
		

		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Company company;

		company = this.companyService.create();
		result = this.createEditModelAndView(companyService.construct(company));

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Company company;

		company = this.companyService.findByPrincipal();

		result = this.createEditModelAndView(companyService.construct(company));

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(CompanyForm companyForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(companyForm);
			for (final ObjectError e : binding.getAllErrors())
				System.out.println(
						e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
		} else if (!companyForm.getNewPassword().equals(companyForm.getConfirmPassword())) {
			result = this.createEditModelAndView(companyForm, "administrator.validator.passwordConfirmNotMatch");
		} else
			try {
				this.companyService.verifyAndSave(companyForm, binding);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (ValidationException oops) {
				result = this.createEditModelAndView(companyForm);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(companyForm, "company.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final CompanyForm company) {
		ModelAndView result;
		result = this.createEditModelAndView(company, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final CompanyForm company, final String messageCode) {
		ModelAndView result;

		if (company.getId() > 0)
			result = new ModelAndView("company/edit");
		else
			result = new ModelAndView("company/create");

		result.addObject("companyForm", company);
		result.addObject("message", messageCode);

		return result;
	}

}

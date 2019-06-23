package controllers;

import java.util.Arrays;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Auditor;
import forms.AuditorForm;
import services.AuditorService;

@Controller
@RequestMapping("/auditor")
public class AuditorController extends AbstractController {

	@Autowired
	private AuditorService auditorService;

	public AuditorController() {
		super();
	}

	// Show ---------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam(required = false) final Integer auditorId) {
		ModelAndView result;
		Auditor auditor = new Auditor();
		boolean isMe = false;
		result = new ModelAndView("auditor/show");
		if (auditorId == null) {
			auditor = this.auditorService.findByPrincipal();
			isMe = true;
		} else {
			auditor = this.auditorService.findOne(auditorId);
		}

		result.addObject("requestURI", "auditor/show.do?auditorId=" + auditorId);
		result.addObject("auditor", auditor);
		result.addObject("isMe", isMe);

		return result;
	}

	// Create ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Auditor auditor;

		auditor = this.auditorService.create();
		result = this.createEditModelAndView(auditorService.construct(auditor));

		return result;
	}

	// Edit ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Auditor auditor;

		auditor = this.auditorService.findByPrincipal();

		result = this.createEditModelAndView(auditorService.construct(auditor));

		return result;
	}

	// Save ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(AuditorForm auditorForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(auditorForm);
			for (final ObjectError e : binding.getAllErrors())
				System.out.println(
						e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
		} else if (!auditorForm.getNewPassword().equals(auditorForm.getConfirmPassword())) {
			result = this.createEditModelAndView(auditorForm, "auditor.validator.passwordConfirmNotMatch");
		} else
			try {
				this.auditorService.verifyAndSave(auditorForm, binding);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (ValidationException oops) {
				result = this.createEditModelAndView(auditorForm);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(auditorForm, "auditor.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final AuditorForm auditor) {
		ModelAndView result;

		result = this.createEditModelAndView(auditor, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final AuditorForm auditor, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("auditor/edit");

		result.addObject("auditorForm", auditor);
		result.addObject("message", messageCode);

		return result;
	}

}

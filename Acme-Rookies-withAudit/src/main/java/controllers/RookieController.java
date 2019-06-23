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

import domain.Rookie;
import forms.RookieForm;
import services.RookieService;

@Controller
@RequestMapping("/rookie")
public class RookieController extends AbstractController {

	// Services ---------------------------------------------------------------

		@Autowired
		private RookieService rookieService;
		// Constructors -----------------------------------------------------------

		public RookieController() {
			super();
		}
		
		@RequestMapping("/show")
		public ModelAndView displayMe(@RequestParam (required = false) final Integer rookieId) {
			ModelAndView result;
			Rookie rookie = new Rookie();
			boolean isMe = false;
			result = new ModelAndView("rookie/show");
			if (rookieId == null) {
				rookie = this.rookieService.findByPrincipal();
				isMe = true;
//				result.addObject("requestURI", "company/show.do");
			} else {
				rookie = this.rookieService.findOne(rookieId);
//				result.addObject("requestURI", "company/show.do?companyd=" + companyId);
			}

			result.addObject("requestURI", "rookie/show.do?companyd=" + rookieId);
			result.addObject("rookie", rookie);
			result.addObject("isMe", isMe);
			

			return result;
		}
		
		@RequestMapping(value = "/create", method = RequestMethod.GET)
		public ModelAndView create() {
			ModelAndView result;
			Rookie rookie;

			rookie = this.rookieService.create();
			result = this.createEditModelAndView(rookieService.construct(rookie));

			return result;
		}
		
		@RequestMapping(value = "/edit", method = RequestMethod.GET)
		public ModelAndView edit() {
			ModelAndView result;
			Rookie rookie;

			rookie = this.rookieService.findByPrincipal();

			result = this.createEditModelAndView(rookieService.construct(rookie));

			return result;
		}
		
		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
		public ModelAndView save(RookieForm rookieForm, final BindingResult binding) {
			ModelAndView result;

			if (binding.hasErrors()) {
				result = this.createEditModelAndView(rookieForm);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(
							e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else if (!rookieForm.getNewPassword().equals(rookieForm.getConfirmPassword())) {
				result = this.createEditModelAndView(rookieForm, "administrator.validator.passwordConfirmNotMatch");
			} else
				try {
					this.rookieService.verifyAndSave(rookieForm, binding);
					result = new ModelAndView("redirect:/welcome/index.do");
				} catch (ValidationException oops) {
					result = this.createEditModelAndView(rookieForm);
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(rookieForm, "rookie.commit.error");
				}
			return result;
		}
		
		protected ModelAndView createEditModelAndView(final RookieForm rookie) {
			ModelAndView result;
			result = this.createEditModelAndView(rookie, null);
			return result;
		}
		
		protected ModelAndView createEditModelAndView(final RookieForm rookie, final String messageCode) {
			ModelAndView result;

			if (rookie.getId() > 0)
				result = new ModelAndView("rookie/edit");
			else
				result = new ModelAndView("rookie/create");

			result.addObject("rookieForm", rookie);
			result.addObject("message", messageCode);

			return result;
		}
	
}

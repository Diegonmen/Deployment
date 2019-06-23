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

import domain.Provider;
import forms.ProviderForm;
import services.ProviderService;

@Controller
@RequestMapping("/provider")
public class ProviderController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ProviderService providerService;
	// Constructors -----------------------------------------------------------

	public ProviderController() {
		super();
	}
	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		final Collection<Provider> providers = this.providerService.findAll();
		result = new ModelAndView("provider/list");

		result.addObject("requestURI", "provider/list.do");
		result.addObject("providers", providers);

		return result;
	}

	@RequestMapping("/show")
	public ModelAndView displayMe(@RequestParam(required = false) final Integer providerId) {
		ModelAndView result;
		Provider provider = new Provider();
		boolean isMe = false;
		result = new ModelAndView("provider/show");
		if (providerId == null) {
			provider = this.providerService.findByPrincipal();
			isMe = true;
		} else {
			provider = this.providerService.findOne(providerId);
		}

		result.addObject("requestURI", "provider/show.do?providerId=" + providerId);
		result.addObject("provider", provider);
		result.addObject("isMe", isMe);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Provider provider;

		provider = this.providerService.create();
		result = this.createEditModelAndView(providerService.construct(provider));

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Provider provider;

		provider = this.providerService.findByPrincipal();

		result = this.createEditModelAndView(providerService.construct(provider));

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(ProviderForm providerForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(providerForm);
			for (final ObjectError e : binding.getAllErrors())
				System.out.println(
						e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
		} else if (!providerForm.getNewPassword().equals(providerForm.getConfirmPassword())) {
			result = this.createEditModelAndView(providerForm, "provider.validator.passwordConfirmNotMatch");
		} else
			try {
				this.providerService.verifyAndSave(providerForm, binding);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (ValidationException oops) {
				result = this.createEditModelAndView(providerForm);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(providerForm, "provider.commit.error");

			}
		return result;
	}


	protected ModelAndView createEditModelAndView(final ProviderForm provider) {
		ModelAndView result;
		result = this.createEditModelAndView(provider, null);
		return result;
	}

	@RequestMapping("fromList/show")
	public ModelAndView display(@RequestParam final Integer itemId) {
		ModelAndView result;
		Provider provider = new Provider();
		boolean isMe = false;
			
		result = new ModelAndView("provider/show");
			
		provider = this.providerService.findByItemId(itemId);
			

		result.addObject("requestURI", "provider/fromList/show.do?providerId=" + itemId);
		result.addObject("provider", provider);
		result.addObject("isMe", isMe);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ProviderForm provider, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("provider/edit");

		result.addObject("providerForm", provider);
		result.addObject("message", messageCode);

		return result;
	}

}

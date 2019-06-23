package controllers;

import java.util.Arrays;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curricula;
import domain.PersonalData;
import services.CurriculaService;
import services.PersonalDataService;

@Controller
@RequestMapping("/personalData")
public class PersonalDataController extends AbstractController {

	@Autowired
	private PersonalDataService personalDataService;

	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private Validator validator;

	public PersonalDataController() {
		super();
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result;
		PersonalData personalData;
		Curricula curricula;

		personalData = this.personalDataService.findOne(id);
		curricula = this.curriculaService.findByPersonalData(personalData);

		result = new ModelAndView("personalData/show");
		result.addObject("personalData", personalData);
		result.addObject("curricula", curricula);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {
		ModelAndView result;
		PersonalData personalData;
		Curricula curricula;

		personalData = this.personalDataService.findOne(id);
		curricula = this.curriculaService.findByPersonalData(personalData);

		Assert.notNull(personalData);
		Assert.isTrue(this.curriculaService.findByPersonalData(personalData).getPersonalData().equals(personalData));
		result = this.createEditModelAndView(personalData, curricula);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam int curriculaId, @Valid PersonalData personalData,
			final BindingResult binding) {

		ModelAndView result;
		Curricula curricula;
		curricula = this.curriculaService.findOne(curriculaId);
		if (personalData.getId() != 0) {
			Assert.isTrue(
					this.curriculaService.findByPersonalData(personalData).getPersonalData().equals(personalData));
		}

		if (binding.hasErrors()) {
			for (ObjectError e : binding.getAllErrors()) {
				System.out.println(
						e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			}
			result = this.createEditModelAndView(personalData, curricula);
		} else {

			try {
				this.validator.validate(personalData, binding);
				if (binding.hasErrors())
					throw new ValidationException();

				this.personalDataService.save(personalData, curricula);
				result = new ModelAndView("redirect:/curricula/list.do");
			} catch (ValidationException oops) {
				curricula = this.curriculaService.findOne(curriculaId);
				result = this.createEditModelAndView(personalData, curricula);
			}catch (final Throwable oops) {
				result = this.createEditModelAndView(personalData, curricula, "personalData.commit.error");
			}
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int curriculaId) {
		ModelAndView result;
		Curricula curricula;

		curricula = this.curriculaService.findOne(curriculaId);
		PersonalData personalData = this.personalDataService.create();
		result = this.createEditModelAndView(personalData, curricula);

		return result;
	}

	protected ModelAndView createEditModelAndView(final PersonalData personalData, final Curricula curricula) {
		ModelAndView result;

		result = this.createEditModelAndView(personalData, curricula, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final PersonalData personalData, final Curricula curricula,
			final String messageError) {
		ModelAndView result;

		boolean isMe = this.curriculaService.findByPersonalData(personalData).getPersonalData().equals(personalData);

		if (personalData.getId() == 0) {
			isMe = true;
			result = new ModelAndView("personalData/create");
		} else {
			result = new ModelAndView("personalData/edit");
		}

		result.addObject("isMe", isMe);
		result.addObject("personalData", personalData);
		result.addObject("message", messageError);
		result.addObject("curricula", curricula);

		return result;
	}

}

package controllers;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curricula;
import domain.EducationData;
import domain.Rookie;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.CurriculaService;
import services.EducationDataService;

@Controller
@RequestMapping("/educationData")
public class EducationDataController extends AbstractController {

	@Autowired
	private EducationDataService educationDataService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private Validator validator;

	public EducationDataController() {
		super();
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result;
		EducationData educationData;
		Curricula curricula;

		educationData = this.educationDataService.findOne(id);
		curricula = this.curriculaService.findByEducationData(educationData);

		result = new ModelAndView("educationData/show");
		result.addObject("educationData", educationData);
		result.addObject("curricula", curricula);
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int curriculaId) {
		ModelAndView result;
		Rookie rookie;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Curricula curricula = curriculaService.findOne(curriculaId);
		rookie = (Rookie) this.actorService.findByUserAccount(userAccount);
		Assert.isTrue(rookie.getCurricula().contains(curricula));

		result = new ModelAndView("curricula/list");
		result.addObject("requestURI", "educationData/list.do");
		result.addObject("educationData", curricula.getEducationData());

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int curriculaId) {
		ModelAndView result;
		Curricula curricula;

		curricula = this.curriculaService.findOne(curriculaId);
		EducationData educationData = this.educationDataService.create();
		result = this.createEditModelAndView(educationData, curricula);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int id) {
		ModelAndView result;
		EducationData educationData;
		Curricula curricula;

		educationData = this.educationDataService.findOne(id);
		curricula = this.curriculaService.findByEducationData(educationData);

		result = this.createEditModelAndView(educationData, curricula);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam int curriculaId, @Valid EducationData educationData,
			final BindingResult binding) {
		ModelAndView result;
		Curricula curricula;

		if (educationData.getId() != 0) {
			Assert.isTrue(this.curriculaService.findByPrincipal()
					.contains(this.curriculaService.findByEducationData(educationData)));
		}
		try {
			this.validator.validate(educationData, binding);
			if (binding.hasErrors())
				throw new ValidationException();
			
			curricula = this.curriculaService.findOne(curriculaId);
			this.educationDataService.save(educationData, curricula);
			result = new ModelAndView("redirect:/curricula/show.do?id=" + curriculaId);
		} catch (ValidationException oops) {
			curricula = this.curriculaService.findOne(curriculaId);
			result = this.createEditModelAndView(educationData, curricula);
		} catch (Throwable oops) {
			curricula = this.curriculaService.findOne(curriculaId);
			result = this.createEditModelAndView(educationData, curricula, "educationData.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final EducationData educationData, final BindingResult binding) {
		ModelAndView result;
		Curricula curricula;

		Assert.isTrue(this.curriculaService.findByPrincipal()
				.contains(this.curriculaService.findByEducationData(educationData)));

		try {
			curricula = this.curriculaService.findByEducationData(educationData);
			this.educationDataService.delete(educationData, curricula);
			result = new ModelAndView("redirect:/curricula/show.do?id=" + curricula.getId());
		} catch (final Throwable oops) {
			curricula = this.curriculaService.findByEducationData(educationData);
			result = this.createEditModelAndView(educationData, curricula, "educationData.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final EducationData educationData, final Curricula curricula) {
		ModelAndView result;

		result = this.createEditModelAndView(educationData, curricula, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final EducationData educationData, final Curricula curricula,
			final String messageError) {
		ModelAndView result;

		if (educationData.getId() == 0) {
			result = new ModelAndView("educationData/create");
		} else {
			result = new ModelAndView("educationData/edit");
		}

		result.addObject("educationData", educationData);
		result.addObject("message", messageError);
		result.addObject("curricula", curricula);

		return result;
	}

}
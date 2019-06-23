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
import domain.Rookie;
import domain.MiscellaneousData;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.CurriculaService;
import services.MiscellaneousDataService;

@Controller
@RequestMapping("/miscellaneousData")
public class MiscellaneousDataController extends AbstractController {

	@Autowired
	private MiscellaneousDataService miscellaneousDataService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private Validator validator;

	public MiscellaneousDataController() {
		super();
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result;
		MiscellaneousData miscellaneousData;
		Curricula curricula;

		miscellaneousData = this.miscellaneousDataService.findOne(id);
		curricula = this.curriculaService.findByMiscellaneousData(miscellaneousData);

		result = new ModelAndView("miscellaneousData/show");
		result.addObject("miscellaneousData", miscellaneousData);
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
		result.addObject("requestURI", "miscellaneousData/list.do");
		result.addObject("miscellaneousData", curricula.getMiscellaneousData());

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int curriculaId) {
		ModelAndView result;
		Curricula curricula;

		curricula = this.curriculaService.findOne(curriculaId);
		MiscellaneousData miscellaneousData = this.miscellaneousDataService.create();
		result = this.createEditModelAndView(miscellaneousData, curricula);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int id) {
		ModelAndView result;
		MiscellaneousData miscellaneousData;
		Curricula curricula;

		miscellaneousData = this.miscellaneousDataService.findOne(id);
		curricula = this.curriculaService.findByMiscellaneousData(miscellaneousData);

		result = this.createEditModelAndView(miscellaneousData, curricula);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam int curriculaId, @Valid MiscellaneousData miscellaneousData,
			final BindingResult binding) {
		ModelAndView result;
		Curricula curricula;

		if (miscellaneousData.getId() != 0) {
			Assert.isTrue(this.curriculaService.findByPrincipal()
					.contains(this.curriculaService.findByMiscellaneousData(miscellaneousData)));
		}
		try {
			this.validator.validate(miscellaneousData, binding);
			if (binding.hasErrors())
				throw new ValidationException();
			
			curricula = this.curriculaService.findOne(curriculaId);
			this.miscellaneousDataService.save(miscellaneousData, curricula);
			result = new ModelAndView("redirect:/curricula/show.do?id=" + curriculaId);
		} catch (ValidationException oops) {
			curricula = this.curriculaService.findOne(curriculaId);
			result = this.createEditModelAndView(miscellaneousData, curricula);
		} catch (Throwable oops) {
			curricula = this.curriculaService.findOne(curriculaId);
			result = this.createEditModelAndView(miscellaneousData, curricula, "miscellaneousData.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MiscellaneousData miscellaneousData, final BindingResult binding) {
		ModelAndView result;
		Curricula curricula;

		Assert.isTrue(this.curriculaService.findByPrincipal()
				.contains(this.curriculaService.findByMiscellaneousData(miscellaneousData)));

		try {
			curricula = this.curriculaService.findByMiscellaneousData(miscellaneousData);
			this.miscellaneousDataService.delete(miscellaneousData, curricula);
			result = new ModelAndView("redirect:/curricula/show.do?id=" + curricula.getId());
		} catch (final Throwable oops) {
			curricula = this.curriculaService.findByMiscellaneousData(miscellaneousData);
			result = this.createEditModelAndView(miscellaneousData, curricula, "miscellaneousData.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousData miscellaneousData,
			final Curricula curricula) {
		ModelAndView result;

		result = this.createEditModelAndView(miscellaneousData, curricula, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousData miscellaneousData, final Curricula curricula,
			final String messageError) {
		ModelAndView result;

		if (miscellaneousData.getId() == 0) {
			result = new ModelAndView("miscellaneousData/create");
		} else {
			result = new ModelAndView("miscellaneousData/edit");
		}

		result.addObject("miscellaneousData", miscellaneousData);
		result.addObject("message", messageError);
		result.addObject("curricula", curricula);

		return result;
	}

}

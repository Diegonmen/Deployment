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
import domain.PositionData;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.CurriculaService;
import services.PositionDataService;

@Controller
@RequestMapping("/positionData")
public class PositionDataController extends AbstractController {

	@Autowired
	private PositionDataService positionDataService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private Validator validator;

	public PositionDataController() {
		super();
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result;
		PositionData positionData;
		Curricula curricula;
		
		positionData = this.positionDataService.findOne(id);
		curricula = this.curriculaService.findByPositionData(positionData);

		result = new ModelAndView("positionData/show");
		result.addObject("positionData", positionData);
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
		result.addObject("requestURI", "positionData/list.do");
		result.addObject("positionData", curricula.getPositionData());

		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int curriculaId) {
		ModelAndView result;
		Curricula curricula;
		
		curricula = this.curriculaService.findOne(curriculaId);
		PositionData positionData = this.positionDataService.create();
		result = this.createEditModelAndView(positionData, curricula);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int id) {
		ModelAndView result;
		PositionData positionData;
		Curricula curricula;
		
		positionData = this.positionDataService.findOne(id);
		curricula = this.curriculaService.findByPositionData(positionData);
		
		result = this.createEditModelAndView(positionData, curricula);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@RequestParam int curriculaId, @Valid PositionData positionData,
			final BindingResult binding) {
		ModelAndView result;
		Curricula curricula;

		if (positionData.getId() != 0) {
			Assert.isTrue(this.curriculaService.findByPrincipal()
					.contains(this.curriculaService.findByPositionData(positionData)));
		}
		try {
			this.validator.validate(positionData, binding);

			if (binding.hasErrors())
				throw new ValidationException();
			
			curricula = this.curriculaService.findOne(curriculaId);
			this.positionDataService.save(positionData, curricula);
			result = new ModelAndView("redirect:/curricula/show.do?id=" + curriculaId);
		} catch (ValidationException oops) {
			curricula = this.curriculaService.findOne(curriculaId);
			result = this.createEditModelAndView(positionData, curricula);
		} catch (Throwable oops) {
			curricula = this.curriculaService.findOne(curriculaId);
			result = this.createEditModelAndView(positionData, curricula, "positionData.commit.error");
		}
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final PositionData positionData, final BindingResult binding) {
		ModelAndView result;
		Curricula curricula;

		Assert.isTrue(this.curriculaService.findByPrincipal()
				.contains(this.curriculaService.findByPositionData(positionData)));

		try {
			curricula = this.curriculaService.findByPositionData(positionData);
			this.positionDataService.delete(positionData, curricula);
			result = new ModelAndView("redirect:/curricula/show.do?id=" + curricula.getId());
		} catch (final Throwable oops) {
			curricula = this.curriculaService.findByPositionData(positionData);
			result = this.createEditModelAndView(positionData, curricula, "positionData.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final PositionData positionData, final Curricula curricula) {
		ModelAndView result;

		result = this.createEditModelAndView(positionData, curricula, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final PositionData positionData, final Curricula curricula,
			final String messageError) {
		ModelAndView result;

		if (positionData.getId() == 0) {
			result = new ModelAndView("positionData/create");
		} else {
			result = new ModelAndView("positionData/edit");
		}

		result.addObject("positionData", positionData);
		result.addObject("message", messageError);
		result.addObject("curricula", curricula);

		return result;
	}

}
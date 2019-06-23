package controllers;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curricula;
import domain.Rookie;
import forms.CurriculaForm;
import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.CurriculaService;

@Controller
@RequestMapping("/curricula")
public class CurriculaController extends AbstractController {

	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private ActorService actorService;

	public CurriculaController() {
		super();
	}

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result;
		Curricula curricula;
		curricula = this.curriculaService.findOne(id);

		result = new ModelAndView("curricula/show");
		result.addObject("curricula", curricula);
		return result;
	}
	
//	@RequestMapping(value = "/show", method = RequestMethod.GET)
//	 public ModelAndView show(@RequestParam final int id) {
//	  ModelAndView result;
//	  Curricula curricula;
//	  curricula = this.curriculaService.findOneNotCopied(id);
//
//	  result = new ModelAndView("curricula/show");
//	  result.addObject("curricula", curricula);
//	  return result;
//	 }
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Curricula curricula;

		curricula = this.curriculaService.create();
		result = this.createEditModelAndView(this.curriculaService.construct(curricula));

		return result;
	}
	
//	@RequestMapping(value = "/list", method = RequestMethod.GET)
//	public ModelAndView list() {
//		ModelAndView result;
//		Rookie rookie;
//		UserAccount userAccount;
//
//		userAccount = LoginService.getPrincipal();
//
//		rookie = (Rookie) this.actorService.findByUserAccount(userAccount);
//
//		result = new ModelAndView("curricula/list");
//		result.addObject("requestURI", "curricula/list.do");
//		result.addObject("curricula", rookie.getCurricula());
//
//		return result;
//	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	 public ModelAndView list() {
	  ModelAndView result;
	  UserAccount userAccount;

	  userAccount = LoginService.getPrincipal();

	  Collection<Curricula> curricula = this.curriculaService.findNotCopiedByUserAccount(userAccount);
	  
	  result = new ModelAndView("curricula/list");
	  result.addObject("requestURI", "curricula/list.do");
	  result.addObject("curricula", curricula);

	  return result;
	 }
	
//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
//	public ModelAndView edit(@RequestParam final int id) {
//		ModelAndView result;
//		Curricula curricula;
//
//		curricula = this.curriculaService.findOne(id);
//
//		result = this.createEditModelAndView(this.curriculaService.construct(curricula));
//
//		return result;
//	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	 public ModelAndView edit(@RequestParam final int id) {
	  ModelAndView result;
	  Curricula curricula;

	  curricula = this.curriculaService.findOneNotCopied(id);

	  result = this.createEditModelAndView(this.curriculaService.construct(curricula));

	  return result;
	 }

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final CurriculaForm curriculaForm, final BindingResult binding) {
		ModelAndView result;
		
			try {
				this.curriculaService.reconstruct(curriculaForm, binding);
				result = new ModelAndView("redirect:/curricula/list.do");
			
			} catch (ValidationException oops) {
				result = this.createEditModelAndView(curriculaForm);
			} catch (Throwable oops) {
				result = this.createEditModelAndView(curriculaForm, "curricula.commit.error");
			}
		return result;
	}
	
//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
//	public ModelAndView delete(@Valid final Curricula curricula, final BindingResult binding) {
//		ModelAndView result;
//		
//			try {
//				this.curriculaService.delete(curricula.getId());
//				result = new ModelAndView("redirect:/curricula/list.do");
//			
//			} catch (ValidationException oops) {
//				result = this.createEditModelAndView(curricula);
//			} catch (Throwable oops) {
//				result = this.createEditModelAndView(curricula, "curricula.commit.error");
//			}
//		return result;
//	}
	
//	@RequestMapping(value = "/delete", method = RequestMethod.GET)
//	public ModelAndView delete(@RequestParam int curriculaId) {
//		ModelAndView result;
//		
//			try {
//				Curricula curricula = curriculaService.findOne(curriculaId);
//				this.curriculaService.delete(curricula.getId());
//				result = new ModelAndView("redirect:/curricula/list.do");
//			} catch (Throwable oops) {
//				result = this.createListModelAndView("curricula.commit.error");
//			}
//		return result;
//	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	 public ModelAndView delete(@RequestParam int curriculaId) {
	  ModelAndView result;
	  
	   try {
	    Curricula curricula = curriculaService.findOneNotCopied(curriculaId);
	    this.curriculaService.delete(curricula.getId());
	    result = new ModelAndView("redirect:/curricula/list.do");
	   } catch (Throwable oops) {
	    result = this.createListModelAndView("curricula.commit.error");
	   }
	  return result;
	 }
	
	protected ModelAndView createEditModelAndView(final CurriculaForm curricula) {
		ModelAndView result;

		result = this.createEditModelAndView(curricula, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final CurriculaForm curricula, final String messageCode) {
		ModelAndView result;

		if (curricula.getId() == 0) {
			result = new ModelAndView("curricula/create");
		} else {
			result = new ModelAndView("curricula/edit");
		}
		
		result.addObject("curriculaForm", curricula);
		result.addObject("message", messageCode);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(final Curricula curricula) {
		ModelAndView result;

		result = this.createEditModelAndView(curricula, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Curricula curricula, final String messageCode) {
		ModelAndView result;

		if (curricula.getId() == 0) {
			result = new ModelAndView("curricula/create");
		} else {
			result = new ModelAndView("curricula/edit");
		}
		
		result.addObject("curriculaForm", curricula);
		result.addObject("message", messageCode);
		
		return result;
	}
	
	public ModelAndView createListModelAndView(String messageCode) {
		ModelAndView result;
		Rookie rookie;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		rookie = (Rookie) this.actorService.findByUserAccount(userAccount);

		result = new ModelAndView("curricula/list");
		result.addObject("requestURI", "curricula/list.do");
		result.addObject("curricula", rookie.getCurricula());
		result.addObject("messageCode", messageCode);

		return result;
	}

}

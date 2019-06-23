
package controllers;

import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NyordelService;
import services.PositionService;
import domain.Nyordel;
import domain.Position;

@Controller
@RequestMapping("/nyordel")
public class NyordelController extends AbstractController {

	@Autowired
	private NyordelService	nyordelService;
	
	@Autowired
	private PositionService	positionService;


	public NyordelController() {
		super();
	}

	// Show
	@RequestMapping("/show")
	public ModelAndView display(@RequestParam final int nyordelId) {
		ModelAndView result;
		Nyordel nyordel = this.nyordelService.findOne(nyordelId);
		
		Position position = positionService.findPositionByNyordelId(nyordelId);

		result = new ModelAndView("nyordel/show");
		result.addObject("nyordel", nyordel);
		result.addObject("position", position);
		result.addObject("requestURI", "nyordel/show.do?nyordelId=" + nyordelId);

		return result;
	}

	//List
	@RequestMapping(value = "/company/list", method = RequestMethod.GET)
	public ModelAndView listForCompanies() {
		ModelAndView result;
		boolean edit = true;

		List<Nyordel> nyordels = this.nyordelService.findAllMines();

		result = new ModelAndView("nyordel/list");

		result.addObject("requestURI", "nyordel/company/list.do");
		result.addObject("nyordels", nyordels);
		result.addObject("edit", edit);

		return result;
	}

//	@RequestMapping(value = "/company/listFromPosition", method = RequestMethod.GET)
//	public ModelAndView listForCompanies(@RequestParam int positionId) {
//		ModelAndView result;
//
//		List<Nyordel> nyordels = this.nyordelService.findAllByPosition(positionId);
//
//		result = new ModelAndView("nyordel/list");
//
//		result.addObject("requestURI", "nyordel/company/listFromPosition.do?positionId=" + positionId);
//		result.addObject("nyordels", nyordels);
//
//		return result;
//	}

	@RequestMapping(value = "/company/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int auditId) {
		ModelAndView result;
		Nyordel nyordel;

		nyordel = this.nyordelService.create(auditId);
		result = this.createEditModelAndView(nyordel);

		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int nyordelId) {
		ModelAndView result;

		final Nyordel nyordel = this.nyordelService.findOneToEdit(nyordelId);
		Assert.notNull(nyordel);
		result = this.createEditModelAndView(nyordel);

		return result;
	}

	@RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Nyordel nyordel, BindingResult binding) {

		ModelAndView result;
		try {
			nyordel = this.nyordelService.reconstruct(nyordel, binding, nyordel.getAudit().getId());
			this.nyordelService.save(nyordel);
			result = new ModelAndView("redirect:list.do");
		} catch (ValidationException oops) {
			result = this.createEditModelAndView(nyordel);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(nyordel, "nyordel.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "company/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Nyordel nyordel, final BindingResult binding) {
		ModelAndView result;

		this.nyordelService.delete(nyordel.getId());
		result = new ModelAndView("redirect:list.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final Nyordel nyordel) {
		ModelAndView result;

		result = this.createEditModelAndView(nyordel, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Nyordel nyordel, final String messageError) {
		ModelAndView result;

		if (nyordel.getId() == 0)
			result = new ModelAndView("nyordel/create");
		else
			result = new ModelAndView("nyordel/edit");

		result.addObject("nyordel", nyordel);
		result.addObject("message", messageError);

		return result;
	}

}

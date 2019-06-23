package controllers.rookie;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.FinderService;
import controllers.AbstractController;
import domain.Finder;
import domain.Position;

@Controller
@RequestMapping("/finder/rookie")
public class FinderRookieController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FinderService finderService;
	

	// Constructors -----------------------------------------------------------

	public FinderRookieController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = new ModelAndView("finder/list");
		Finder finder = finderService.findFinderByPrincipal();
		
		Collection<Position> positions;
		positions = finder.getPositions();
		result.addObject("positions", positions);
		
		result.addObject("requestURI", "finder/rookie/list.do");

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView search() {
		
		ModelAndView result;
		
		Finder finder = finderService.findFinderByPrincipal();

		result = this.createEditModelAndView(finder);
	
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Finder finder, final BindingResult binding) {

		ModelAndView result;
		
		try {
			if(finderService.finderHasChangedOrIsExpired(finder)) {
				finder = finderService.reconstruct(finder, binding);
				this.finderService.save(finder) ;
			}
			result = new ModelAndView("redirect:list.do");
		} catch (ValidationException oops) {
			result = this.createEditModelAndView(finder);
		} catch (Throwable oops) {
			result = this.createEditModelAndView(finder, "finder.commit.error");
		}
		
		return result;
	}
	
	@RequestMapping(value = "/clear", method = RequestMethod.GET)
	public ModelAndView clear() {

		ModelAndView result;
		
		try {
			this.finderService.clear();
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:edit.do");
			result.addObject("message", "finder.commit.error");
		}
		
		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Finder finder) {
		ModelAndView result;
		result = this.createEditModelAndView(finder, null);
		
		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder, final String messageError) {
		ModelAndView result;
		
		result = new ModelAndView("finder/edit");

		result.addObject("finder", finder);
		result.addObject("message", messageError);
		
		return result;
	}

}

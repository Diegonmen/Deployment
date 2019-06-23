package controllers.provider;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.PositionService;
import services.ProviderService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Position;
import domain.Provider;
import domain.Sponsorship;

@Controller
@RequestMapping("/sponsorship/provider")
public class SponsorshipProviderController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ProviderService providerService;
	
	@Autowired
	private SponsorshipService sponsorshipService;
	
	@Autowired
	private PositionService positionService;

	// Constructors -----------------------------------------------------------

	public SponsorshipProviderController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		
		Provider loggedProvider = providerService.findByPrincipal();
		Collection<Sponsorship> sponsorships = loggedProvider.getSponsorships();

		result = new ModelAndView("sponsorship/list");

		result.addObject("requestURI", "sponsorship/provider/list.do");
		result.addObject("sponsorships", sponsorships);
		return result;
	}
	
	// Showing -----------------------------------------------------------------
	
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int id) {
		ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.findOneToEdit(id);
		Assert.notNull(sponsorship);

		result = this.showModelAndView(sponsorship);
		return result;
	}
	
	// Creation -----------------------------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.create();
		result = this.createEditModelAndView(sponsorship);
		return result;
	}
	
	// Edition -----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {
		ModelAndView result;
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.findOneToEdit(id);
		Assert.notNull(sponsorship);
		result = this.createEditModelAndView(sponsorship);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Sponsorship sponsorship, final BindingResult binding, @RequestParam(required = false) final Integer positionId) {
		
		ModelAndView result;
		
		Position position = null;
		if (positionId != null) {
			position = positionService.findOne(positionId);
		}

		try {
			sponsorship = this.sponsorshipService.reconstruct(sponsorship, binding);
			this.sponsorshipService.save(sponsorship, position);
			result = new ModelAndView("redirect:list.do");
		} catch (ValidationException oops) {
			result = this.createEditModelAndView(sponsorship);
		} catch (final Throwable oops) {
			if(oops.getMessage().contains("sponsorship.expired.card"))
				result = this.createEditModelAndView(sponsorship, "sponsorship.expired.card");
			else
				result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;

		try {
			this.sponsorshipService.delete(sponsorship.getId());
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(sponsorship, "sponsorship.commit.error");
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;
		result = this.createEditModelAndView(sponsorship, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {
		ModelAndView result;

		if (sponsorship.getId() > 0) {
			result = new ModelAndView("sponsorship/edit");
		} else {
			result = new ModelAndView("sponsorship/create");
		}

		result.addObject("sponsorship", sponsorship);
		result.addObject("message", messageCode);

		return result;
	}
	
	protected ModelAndView showModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;
		result = new ModelAndView("sponsorship/show");
		result.addObject("sponsorship", sponsorship);

		return result;
	}

}

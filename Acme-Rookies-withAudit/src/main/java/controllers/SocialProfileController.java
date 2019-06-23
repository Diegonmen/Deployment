package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SocialProfileService;
import domain.Actor;
import domain.SocialProfile;

@Controller
@RequestMapping("/social-profile")
public class SocialProfileController extends AbstractController {

	@Autowired
	private SocialProfileService socialProfileService;
	
	@Autowired
	private ActorService actorService;
	
	// Constructors -----------------------------------------------------------

		public SocialProfileController() {
			super();
		}
	
	// Listing ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		
		Actor loggedActor = actorService.findByPrincipal();
		Collection<SocialProfile> socialProfiles = loggedActor.getSocialProfiles(); ;
		
		result = new ModelAndView("social-profile/list");

		result.addObject("requestURI", "social-profile/list.do");
		result.addObject("socialProfiles", socialProfiles);

		return result;
	}
	
	// Edit
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.create() ;
		result = this.createEditModelAndView(socialProfile);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int id) {
		ModelAndView result;

		SocialProfile socialProfile = this.socialProfileService.findOneToEdit(id);
		Assert.notNull(socialProfile);
		result = this.createEditModelAndView(socialProfile);

		return result;
	}
	
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SocialProfile socialProfile, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			System.out.println(binding);
			result = this.createEditModelAndView(socialProfile);
		} else {
			try {
				this.socialProfileService.save(socialProfile) ;
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
			}
		}

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final SocialProfile socialProfile, final BindingResult binding) {
		ModelAndView result;

		try {
			this.socialProfileService.delete(socialProfile.getId());
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile) {
		ModelAndView result;

		result = this.createEditModelAndView(socialProfile, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile, final String messageError) {
		ModelAndView result;

		if(socialProfile.getId() == 0) {
			result = new ModelAndView("social-profile/create");
		} else {
			result = new ModelAndView("social-profile/edit");
		}
		
		result.addObject("socialProfile", socialProfile);
		result.addObject("message", messageError);

		return result;
	}
	

}

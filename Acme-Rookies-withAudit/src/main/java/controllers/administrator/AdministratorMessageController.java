
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.MessageService;
import controllers.AbstractController;
import domain.Actor;

@Controller
@RequestMapping("/message/administrator")
public class AdministratorMessageController extends AbstractController {

	// Services

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ActorService			actorService;


	@RequestMapping(value = "/recalculateSpammer", method = RequestMethod.GET)
	public ModelAndView recalcSpam() {
		ModelAndView result;
		result = new ModelAndView("redirect:/");

		this.messageService.calculateSpammer();

		return result;

	}

	@RequestMapping(value = "/listSpammers", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Actor> spammers = this.actorService.findSpammer();

		result = new ModelAndView("message/listSpammers");
		result.addObject("requestURI", "/message/administrator/listSpammers.do");
		result.addObject("spammers", spammers);

		return result;
	}

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView banActor(@RequestParam int actorId) {
		ModelAndView result;
		result = new ModelAndView("redirect:/message/administrator/listSpammers.do");
		Actor a = this.actorService.findByActorId(actorId);
		Assert.notNull(a);
		try {
			Assert.isTrue(this.administratorService.findByPrincipal() != null);
			this.actorService.updateBan(a, true);
		} catch (Throwable oops) {
			result.addObject("error", "noAdminBanError");
		}

		return result;

	}
	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unbanActor(@RequestParam int actorId) {
		ModelAndView result;
		result = new ModelAndView("redirect:/message/administrator/listSpammers.do");
		Actor a = this.actorService.findByActorId(actorId);
		Assert.notNull(a);

		this.actorService.updateBan(a, false);

		return result;

	}

	@RequestMapping(value = "/rebrand", method = RequestMethod.GET)
	public ModelAndView notifyRebranding() {
		ModelAndView result;
		result = new ModelAndView("redirect:/");
		this.administratorService.announceRebranding();
		return result;
	}

}

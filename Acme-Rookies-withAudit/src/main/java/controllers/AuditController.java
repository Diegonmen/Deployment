
package controllers;

import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AuditService;
import services.AuditorService;
import services.NyordelService;
import domain.Actor;
import domain.Audit;
import domain.Auditor;
import domain.Company;
import domain.Nyordel;
import domain.Position;

@Controller
@RequestMapping("/audit")
public class AuditController extends AbstractController {

	// Services

	@Autowired
	private AuditService	auditService;
	@Autowired
	private AuditorService	auditorService;
	@Autowired
	private ActorService	actorService;
	@Autowired
	private NyordelService	nyordelService;


	// Methods

	// List for the logged auditor

	@RequestMapping(value = "/auditor/list", method = RequestMethod.GET)
	public ModelAndView listOwned() {
		ModelAndView result;
		Auditor auditor = this.auditorService.findByPrincipal();

		List<Audit> auditsOwned = this.auditService.principalAudits(auditor.getId());

		//		Map<Audit, Position> map = new HashMap<>();
		//
		//		for(Audit a: auditsOwned)
		//			map.put(a, this.auditService.findPositionForAudit(a.getId()));

		result = new ModelAndView("audit/list");

		result.addObject("audits", auditsOwned);
		result.addObject("requestURI", "audit/auditor/list.do");
		//		result.addObject("map", map);

		return result;
	}

	// List for a given position

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int positionId) {
		ModelAndView result;

		List<Audit> audits = this.auditService.findAuditsForPosition(positionId);

		result = new ModelAndView("audit/list");

		result.addObject("audits", audits);
		result.addObject("requestURI", "audit/list.do");

		return result;
	}

	// Showing an audit

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam int auditId) {
		ModelAndView result;

		Audit audit = this.auditService.findOne(auditId);

		Position position = this.auditService.findPositionForAudit(auditId);
		
		List<Nyordel> nyordels = nyordelService.findAllByAuditInFinal(auditId);

		result = new ModelAndView("audit/show");

		result.addObject("position", position);
		result.addObject("audit", audit);
		result.addObject("nyordels", nyordels);
		
		Actor loggedActor = actorService.findByPrincipal();
		if(loggedActor != null && loggedActor instanceof Company) {
			if(((Company) loggedActor).getPositions().contains(audit.getPosition())) {
				result.addObject("isAboutMyPosition", true);
			}
		}
		
		result.addObject("requestURI", "audit/show.do");

		return result;
	}

	@RequestMapping(value = "/auditor/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int positionId) {
		ModelAndView result;

		this.auditService.selfAssign(positionId);
		result = new ModelAndView("redirect:list.do");

		return result;
	}

	@RequestMapping(value = "/auditor/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int auditId) {
		ModelAndView result;
		Audit audit;

		audit = this.auditService.findOneToEdit(auditId);
		result = this.createEditModelAndView(audit);
		return result;
	}

	@RequestMapping(value = "/auditor/edit", method = RequestMethod.POST, params="save")
	public ModelAndView save(Audit audit, BindingResult binding) {
		ModelAndView result;

		try {
			audit = this.auditService.reconstruct(audit, binding);
			this.auditService.save(audit);
			result = new ModelAndView("redirect:list.do");
		} catch (ValidationException e) {
			result = this.createEditModelAndView(audit);
		} catch (Throwable oops) {
			result = this.createEditModelAndView(audit, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/auditor/edit", method = RequestMethod.POST, params="delete")
	public ModelAndView delete(Audit audit) {
		ModelAndView result;

		try {
			this.auditService.delete(audit.getId());
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(audit, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(Audit audit) {
		ModelAndView result;

		result = this.createEditModelAndView(audit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Audit audit, String messageCode) {
		ModelAndView result;

		if (audit.getId() == 0)
			result = new ModelAndView("audit/create");
		else
			result = new ModelAndView("audit/edit");

		result.addObject("audit", audit);
		result.addObject("message", messageCode);

		return result;
	}

}

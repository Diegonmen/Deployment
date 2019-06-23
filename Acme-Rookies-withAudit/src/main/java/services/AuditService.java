
package services;

import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AuditRepository;
import domain.Audit;
import domain.Auditor;
import domain.Position;

@Service
@Transactional
public class AuditService {

	@Autowired
	private AuditRepository	auditRepository;

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private PositionService	positionService;

	@Autowired
	private Validator		validator;


	public List<Audit> findAll() {
		return this.auditRepository.findAll();
	}

	public Audit findOne(Integer id) {
		return this.auditRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return this.auditRepository.exists(id);
	}

	public void delete(Integer id) {
		this.findOneToEdit(id);
		Position pos = this.findPositionForAudit(id);
		pos.getAudits().remove(this.findOne(id));
		this.auditRepository.delete(id);
	}

	public Audit create() {
		Audit audit = new Audit();
		audit.setDraftMode(true);
		return audit;
	}

	public Audit save(Audit audit) {
		Assert.isTrue(audit.getScore() >= 0 && audit.getScore() <= 10);
		Assert.isTrue(!audit.getText().trim().equals(""));

		Audit saved = this.auditRepository.save(audit);

		return saved;
	}

	public Audit selfAssign(int positionId) {
		Audit audit = this.create();
		Auditor auditor = this.auditorService.findByPrincipal();
		audit.setAuditor(auditor);
		audit.setMoment(new Date(System.currentTimeMillis() - 1));
		Position pos = this.positionService.findOne(positionId);
		audit.setPosition(pos);
		Audit assigned = this.auditRepository.saveAndFlush(audit);

		pos.getAudits().add(assigned);
		Assert.isTrue(pos.getAudits().contains(assigned));

		return assigned;
	}

	public Audit reconstruct(Audit entity, BindingResult binding) {
		Audit result;

		if (entity.getId() == 0)
			result = this.create();
		else
			result = this.findOne(entity.getId());
		result.setDraftMode(entity.isDraftMode());
		result.setScore(entity.getScore());
		result.setText(entity.getText());

		this.validator.validate(result, binding);

		if (result.getText().trim().equals(""))
			binding.rejectValue("text", "text.not.blank");

		if(result.getScore() < 0 || result.getScore() > 10 || result.getScore() == null || result.getScore().toString().trim().equals(""))
			binding.rejectValue("score", "score.error");

		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public Audit findOneToEdit(Integer id) {
		Auditor auditor = this.auditorService.findByPrincipal();
		Audit toEdit = this.findOne(id);
		Assert.isTrue(toEdit.getAuditor().getId() == auditor.getId());
		Assert.isTrue(toEdit.isDraftMode());
		return toEdit;
	}

	public List<Audit> principalAudits(Integer auditorId) {
		return this.auditRepository.principalAudits(auditorId);
	}

	public List<Audit> findAuditsForPosition(Integer positionId) {
		return this.auditRepository.findAuditsForPosition(positionId);
	}

	public Position findPositionForAudit(Integer auditId) {
		return this.auditRepository.findPositionForAudit(auditId);
	}

	public List<Audit> findForAuditorId(int id) {
		return this.auditRepository.principalAudits(id);
	}

	public Double findAverageAuditScoreForCompanyId(int id) {
		return this.auditRepository.findAvgAuditScoreForCompany(id);
	}

}

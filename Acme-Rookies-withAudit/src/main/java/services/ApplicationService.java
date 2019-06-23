
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ApplicationRepository;
import domain.Application;
import domain.Company;
import domain.Curricula;
import domain.Rookie;
import domain.Position;
import domain.Problem;

@Service
@Transactional
public class ApplicationService {

	@Autowired
	private ApplicationRepository	applicationRepository;

	@Autowired
	private RookieService			rookieService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private PositionService			positionService;

	@Autowired
	private CurriculaService		curriculaService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private Validator				validator;


	public Application create() {
		final Application application = new Application();

		return application;
	}

	// Application creation when a rookie clicks on create application for a given position.
	public Application save(final Application entity, final int positionId) {
		Application application = new Application();

		final Rookie rookie = this.rookieService.findByPrincipal();

		application = entity;

		application.setStatus("PENDING");

		Assert.isNull(this.checkExistingApplications(positionId, rookie.getId()));

		Assert.isTrue(!this.positionService.findOne(positionId).isDraftMode());

		Collection<Problem> problemsToAssign = this.applicationRepository.assignRandomProblem(positionId);
		Random r = new Random();
		List<Problem> problemsToRandomize = new ArrayList<>(problemsToAssign);
		Problem problem = problemsToRandomize.get(r.nextInt(problemsToAssign.size()));
		application.setProblem(problem);

		application.setMoment(new Date(System.currentTimeMillis() - 1));

		Assert.isNull(application.getAnswer());
		Assert.isNull(application.getAnswerLink());
		Assert.isNull(application.getAnswerMoment());

		Curricula copied = this.curriculaService.copyCurricula(entity.getCurricula());

		application.setCurricula(copied);

		final Application saved = this.applicationRepository.save(application);

		List<Application> apps = new ArrayList<>(rookie.getApplications());
		apps.add(saved);
		rookie.setApplications(apps);

		return saved;
	}

	public Application submit(final Application entity) {
		Assert.notNull(this.rookieService.findByPrincipal());
		final Application application = entity;

		Assert.notNull(application.getAnswer());
		Assert.isTrue(!application.getAnswer().trim().equals(""));
		Assert.notNull(application.getAnswerLink());
		Assert.isTrue(!application.getAnswerLink().trim().equals(""));
		Assert.notNull(application.getCurricula());

		Assert.isTrue(application.getStatus().equals("PENDING"));

		application.setAnswerMoment(new Date(System.currentTimeMillis() - 1));

		application.setStatus("SUBMITTED");

		Application saved = this.applicationRepository.save(application);

		Company company = this.notifyCompany(saved.getProblem().getPosition().getId());

		this.messageService.sendNotification("Application status changed - El estado de una solicitud ha cambiado", "The status of an application for position " + saved.getProblem().getPosition().getTitle()
			+ " has changed to 'SUBMITTED' - El estado de una solicitud al puesto " + saved.getProblem().getPosition().getTitle() + " ha cambiado a 'SUBMITTED'", company);

		return saved;
	}

	public void accept(Application entity) {
		Assert.notNull(this.companyService.findByPrincipal());
		Application application = entity;

		Assert.isTrue(application.getStatus().equals("SUBMITTED"));
		application.setStatus("ACCEPTED");

		this.applicationRepository.save(application);

		Rookie rookie = this.notifyRookie(application.getId());

		this.messageService.sendNotification("Application status changed - El estado de una solicitud ha cambiado", "The status of an application for position " + application.getProblem().getPosition().getTitle()
			+ " has changed to 'ACCEPTED' - El estado de una solicitud al puesto " + application.getProblem().getPosition().getTitle() + " ha cambiado a 'ACCEPTED'", rookie);
	}

	public void reject(Application entity) {
		Assert.notNull(this.companyService.findByPrincipal());
		Application application = entity;

		Assert.isTrue(application.getStatus().equals("SUBMITTED"));
		application.setStatus("REJECTED");

		this.applicationRepository.save(application);

		Rookie rookie = this.notifyRookie(application.getId());

		this.messageService.sendNotification("Application status changed - El estado de una solicitud ha cambiado", "The status of an application for position " + application.getProblem().getPosition().getTitle()
			+ " has changed to 'REJECTED' - El estado de una solicitud al puesto " + application.getProblem().getPosition().getTitle() + " ha cambiado a 'REJECTED'", rookie);
	}

	public List<Application> findAll() {
		return this.applicationRepository.findAll();
	}

	public Application findOne(final Integer id) {
		return this.applicationRepository.findOne(id);
	}

	public Application findOneToEditRookie(final Integer id) {
		final Rookie rookie = this.rookieService.findByPrincipal();
		Assert.notNull(rookie);
		final Application toEdit = this.findOne(id);
		Assert.isTrue(rookie.getApplications().contains(toEdit));
		Assert.isTrue(toEdit.getStatus().equals("PENDING"));
		return toEdit;
	}

	public Application findOneToEditCompany(final Integer id, final int positionId) {
		final Company company = this.companyService.findByPrincipal();
		Assert.notNull(company);
		final Application toEdit = this.findOne(id);
		final Collection<Application> applications = this.applicationsForPosition(positionId, company.getId());
		Assert.isTrue(applications.contains(toEdit));
		Assert.isTrue(toEdit.getStatus().equals("SUBMITTED"));
		return toEdit;
	}

	public boolean exists(final Integer id) {
		return this.applicationRepository.exists(id);
	}

	public void delete(final Integer id) {
		this.applicationRepository.delete(id);
	}

	public Rookie checkExistingApplications(final int positionId, final int rookieId) {
		return this.applicationRepository.checkExistingApplications(positionId, rookieId);
	}

	public Collection<Application> applicationsForPosition(final int positionId, final int companyId) {
		return this.applicationRepository.applicationsForPosition(positionId, companyId);
	}

	public Collection<Application> findApplicationsForPosition(int positionId) {
		return this.applicationRepository.findApplicationsForPosition(positionId);
	}

	public Collection<Position> checkForApplyingToPositions(int rookieId) {
		return this.applicationRepository.checkForApplyingToPositions(rookieId);
	}

	public Collection<Curricula> selectCurricula(int rookieId) {
		return this.applicationRepository.selectCurricula(rookieId);
	}

	public Collection<Application> findApplicationToProblem(int problemId) {
		return this.applicationRepository.findApplicationsToProblem(problemId);
	}

	public Position checkOwner(int companyId, int positionId) {
		return this.applicationRepository.checkOwner(companyId, positionId);
	}

	public Rookie notifyRookie(int applicationId) {
		return this.applicationRepository.notifyRookie(applicationId);
	}

	public Company notifyCompany(int positionId) {
		return this.applicationRepository.notifyCompany(positionId);
	}

	public Application reconstruct(final Application application, final BindingResult binding) {
		Application result;

		if (application.getId() == 0) {
			result = this.create();
			result.setCurricula(application.getCurricula());
			result.setMoment(application.getMoment());
		} else{
			result = this.findOne(application.getId());
			if(application.getAnswer().trim().equals(""))
				binding.rejectValue("answer", "application.answer.blank");
			if(application.getAnswerLink().trim().equals(""))
				binding.rejectValue("answerLink", "application.answer.blank");
		}

		result.setAnswer(application.getAnswer());
		result.setAnswerLink(application.getAnswerLink());
		result.setAnswerMoment(application.getAnswerMoment());
		//		result.setStatus(application.getStatus());

		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}
	
	public Collection<Application> findApplicationsByCurricula(Curricula curricula){
		Assert.isTrue(curriculaService.exists(curricula.getId()));
		Collection<Application> res = this.applicationRepository.findApplicationsByCurricula(curricula.getId());
		return res;
	}
}

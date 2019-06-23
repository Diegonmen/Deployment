package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AuditRepository;
import repositories.PositionRepository;
import domain.Actor;
import domain.Application;
import domain.Audit;
import domain.Company;
import domain.Finder;
import domain.Nyordel;
import domain.Position;
import domain.Problem;

@Service
@Transactional
public class PositionService {

	@Autowired
	private PositionRepository positionRepository;
	
	@Autowired
	private AuditRepository auditRepository;

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private NyordelService nyordelService;

	@Autowired
	private ProblemService problemService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private FinderService finderService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private Validator validator;


	public List<Position> findAll() {
		return positionRepository.findAll();
	}

	public Position findOne(Integer id) {
		return positionRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return positionRepository.exists(id);
	}


	public void delete(Integer id){
		Collection<Problem> problems = this.problemService.findProblemsByPositionIdToDelete(id);
		Collection<Finder> finders = this.finderService.findFindersSearchingPosition(id);
		Company creator = this.companyService.findByPrincipal();
		Position position = this.findOne(id);
		Assert.isTrue(position.isDraftMode());
		
		for (Nyordel n: this.nyordelService.findAllByPosition(id)){
			this.nyordelService.delete(n.getId());
		}

		
		for (Audit  a: position.getAudits()){
			this.auditRepository.delete(a);
		}

		for (final Problem p : problems) {
			p.setPosition(null);
			this.problemService.save(p);
		}

		for (final Finder f : finders) {
			f.getPositions().remove(position);
//			this.finderService.save(f);
		}

		creator.getPositions().remove(position);
		this.companyService.save(creator);

		this.positionRepository.delete(id);
	}

	public void cancel(Integer id) {
//		Collection<Finder> finders = this.finderService.findFindersSearchingPosition(id);
		Collection<Problem> problems = this.problemService.findProblemsByPositionIdToDelete(id);
		Company creator = this.companyService.findByPrincipal();
		Collection<Application> applications = this.applicationService.findApplicationsForPosition(id);

		Position position = this.findOne(id);
		Assert.isTrue(!position.isCancelled());
		Assert.isTrue(!position.isDraftMode());
		Assert.isTrue(applications.isEmpty());
		Assert.isTrue(creator.getPositions().contains(position));
		position.setCancelled(true);

		for (final Problem p : problems) {
			if(p.isDraftMode())
				p.setPosition(null);
				this.problemService.save(p);
		}

//		for (final Finder f : finders) {
//			f.getPositions().remove(position);
//			
//		}

		this.save(position);
	}

	public Position create() {
		final Position res = new Position();
		res.setTicker(this.tickerGenerator());
		res.setDraftMode(true);
		res.setCancelled(false);
		return res;
	}


	public Position save(final Position entity) {
		Position saved;
		Date date = new Date(System.currentTimeMillis()-1);
		final Company creator = this.companyService.findByPrincipal();

		Assert.isTrue(entity.getDeadline().after(date));
		saved=this.positionRepository.save(entity);

		if(entity.getId()==0)
			creator.getPositions().add(saved);

		if(!entity.isDraftMode()) {
			Collection<Actor> rookies = finderService.findRookiesWithFindersMatchingParameters(saved.getTitle(), saved.getDescription(), saved.getTicker(), saved.getSkills(), saved.getTechnologies(), saved.getProfile(), saved.getDeadline(), saved.getSalary());
			messageService.sendNotification("A new offer matches your finder - Una nueva oferta de trabajo coincide con tu buscador", "The position " + saved.getTitle() + " matches your finder parameters.\n\n---\n\nEl puesto " + saved.getTitle() + " cumple los parámetros de tu buscador.", rookies);
		}

		return saved;
	}

	public Collection<Position> findPositionsInFinalFromCompanyId(int companyId){
		return this.positionRepository.findPositionsInFinalFromCompanyId(companyId);
	}

	public Collection<Position> findPPublicPositions(){
		Date today = new Date(System.currentTimeMillis()-1);
		return this.positionRepository.findPublicPositions(today);
	}

	public Collection<Position> findPositionsNotCancelled(){
		Date today = new Date(System.currentTimeMillis()-1);
		int creatorId = this.companyService.findByPrincipal().getId();
		return this.positionRepository.findPositionsNotCancelled(creatorId,today) ;
	}

	public Position reconstruct(final Position position, final BindingResult binding) {
		Position result;
		Date date = new Date(System.currentTimeMillis()-1);

		if (position.getId() == 0) {
			result = this.create();
		} else {
			result = this.positionRepository.findOne(position.getId());

		}
		result.setTitle(position.getTitle());
		result.setDescription(position.getDescription());
		result.setDeadline(position.getDeadline());
		result.setDraftMode(position.isDraftMode());
		result.setProfile(position.getProfile());
		result.setSkills(position.getSkills());
		result.setTechnologies(position.getTechnologies());
		result.setSalary(position.getSalary());




		this.validator.validate(result, binding);
		if(binding.hasErrors())
			throw new ValidationException();
		if(!result.isDraftMode()){
			Assert.isTrue(problemService.findProblemsOfPosition(result.getId()).size()>=2,"You must set this position to at least 2 problems to save in final mode");
		}
		if(!position.getDeadline().after(date)){
			binding.rejectValue("deadline", "position.deadline.error", "The dead line must be in future");
			throw new ValidationException();
		}


		return result;
	}

		//String.format(format, args) guardar substring
	public String tickerGenerator() {
		Company creator = this.companyService.findByPrincipal();
		String first = creator.getCommercialName().trim().toUpperCase()+"XXXX";
		String str = null;
		str = first.substring(0,4)+"-"+this.generateAlphanumeric();
		while(this.positionRepository.findPositionsWithSameTicker(str)!=null){
			str = first.substring(0,4)+"-"+this.generateAlphanumeric();
		}
		return str;
	}

	public String generateAlphanumeric() {
		final Character[] letras = {
			 '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
		};
		final Random rand = new Random();
		String alpha = "";
		for (int i = 0; i < 4; i++)
			alpha += letras[rand.nextInt(letras.length - 1)];

		return alpha;
	}

	public Position findOneToEdit(int positionId) {
		final Company creator = this.companyService.findByPrincipal();
		Position mine = this.findOne(positionId);
		Assert.isTrue(mine.isDraftMode());
		Assert.isTrue(creator.getPositions().contains(mine));
		return mine;
	}

	public Collection<Position> findPositions(String keyword, Double minimumSalary, Double maximumSalary, Date deadline) {

		final Pageable pageable = new PageRequest(0, this.configurationService.findConfiguration().getFinderResults());

		return this.positionRepository.findPositions(keyword, minimumSalary, maximumSalary, deadline, pageable);

	}
	
	public Collection<Position> findPositionsByKeyword(String keyword) {

		return this.positionRepository.findPositionsByKeyword(keyword);
		
	}
	
	public Position findPositionByNyordelId(Integer id) {
		return positionRepository.findPositionByNyordelId(id);
	}

}

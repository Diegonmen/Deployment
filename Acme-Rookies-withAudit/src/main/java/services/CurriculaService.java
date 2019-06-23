package services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Curricula;
import domain.EducationData;
import domain.Rookie;
import domain.MiscellaneousData;
import domain.PersonalData;
import domain.PositionData;
import forms.CurriculaForm;
import repositories.CurriculaRepository;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class CurriculaService {

	@Autowired
	private CurriculaRepository curriculaRepository;

	@Autowired
	private EducationDataService educationDataService;

	@Autowired
	private MiscellaneousDataService miscellaneousDataService;

	@Autowired
	private PersonalDataService personalDataService;

	@Autowired
	private PositionDataService positionDataService;

	@Autowired
	private RookieService rookieService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private Validator validator;

	public Curricula save(Curricula entity) {
		return curriculaRepository.save(entity);
	}

	public void flush() {
		curriculaRepository.flush();
	}

	public List<Curricula> findAll() {
		return curriculaRepository.findAll();
	}

	public Curricula findOne(Integer id) {
		return curriculaRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return curriculaRepository.exists(id);
	}

	public void delete(Integer id) {
		Curricula saved = this.findOne(id);
		Assert.isTrue(rookieService.findByPrincipal().getCurricula().contains(saved));
		Rookie rookie = rookieService.findByPrincipal();
//		PersonalData personaldata = saved.getPersonalData();

		for (EducationData data : saved.getEducationData()) {
			this.educationDataService.delete(data.getId());
		}
		for (MiscellaneousData data : saved.getMiscellaneousData()) {
			this.miscellaneousDataService.delete(data.getId());
		}
		for (PositionData data : saved.getPositionData()) {
			this.positionDataService.delete(data.getId());
		}
		rookie.getCurricula().remove(saved);
		this.rookieService.save(rookie);
		this.curriculaRepository.delete(saved);

//		this.personalDataService.delete(personaldata.getId());
	}

	public Curricula create() {
		Curricula result = new Curricula();
		Collection<PositionData> positionData = new LinkedList<>();
		result.setPositionData(positionData);
		Collection<EducationData> educationData = new LinkedList<>();
		result.setEducationData(educationData);
		Collection<MiscellaneousData> miscellaneousData = new LinkedList<>();
		result.setMiscellaneousData(miscellaneousData);
		result.setPersonalData(new PersonalData());

		return result;
	}

	public Collection<Curricula> findByPrincipal() {
		Collection<Curricula> result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Curricula findByPersonalData(PersonalData personalData) {
		Curricula result = this.curriculaRepository.findByPersonalDataId(personalData.getId());
		Assert.notNull(result);
		return result;
	}

	public Curricula findByEducationData(EducationData educationData) {
		Curricula result = this.curriculaRepository.findByEducationData(educationData.getId());
		Assert.notNull(result);
		return result;
	}

	public Curricula findByPositionData(PositionData positionData) {
		Curricula result = this.curriculaRepository.findByPositionData(positionData.getId());
		Assert.notNull(result);
		return result;
	}

	public Curricula findByMiscellaneousData(MiscellaneousData miscellaneousData) {
		Curricula result = this.curriculaRepository.findByMiscellaneousData(miscellaneousData.getId());
		Assert.notNull(result);
		return result;
	}

	public Collection<Curricula> findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);
		Collection<Curricula> result;
		result = this.curriculaRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public CurriculaForm construct(Curricula curricula) {
		CurriculaForm result = new CurriculaForm();
		result.setTitle(curricula.getTitle());
		result.setId(curricula.getId());

		if (curricula.getId() != 0) {

			result.setFullName(curricula.getPersonalData().getFullName());
			result.setStatement(curricula.getPersonalData().getStatement());
			result.setPhoneNumber(curricula.getPersonalData().getPhoneNumber());
			result.setGithubProfile(curricula.getPersonalData().getGithubProfile());
			result.setLinkedinProfile(curricula.getPersonalData().getLinkedinProfile());
		}

		return result;
	}

	public Curricula reconstruct(CurriculaForm form, BindingResult binding) {
		Curricula result;
		Rookie logedRookie = rookieService.findByPrincipal();
		PersonalData personalData = new PersonalData();

		if (form.getId() == 0)
			result = this.create();

		else {
			result = (Curricula) this.curriculaRepository.findByCurriculaId(form.getId());
			Assert.isTrue(logedRookie.getCurricula().contains(result));
			logedRookie.getCurricula().remove(result);
			form.setFullName(result.getPersonalData().getFullName());
			form.setStatement(result.getPersonalData().getStatement());
			form.setPhoneNumber(result.getPersonalData().getPhoneNumber());
			form.setGithubProfile(result.getPersonalData().getGithubProfile());
			form.setLinkedinProfile(result.getPersonalData().getLinkedinProfile());
		}
		result.setTitle(form.getTitle());

		personalData.setFullName(form.getFullName());
		personalData.setStatement(form.getStatement());

		if (!StringUtils.isEmpty(form.getPhoneNumber())) {

			Pattern pattern = Pattern.compile("^\\d{4,}$", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(form.getPhoneNumber());

			if (matcher.matches())
				form.setPhoneNumber(
						this.configurationService.findAll().iterator().next().getCountryCode() + form.getPhoneNumber());
		}

		personalData.setPhoneNumber(form.getPhoneNumber());
		personalData.setGithubProfile(form.getGithubProfile());
		personalData.setLinkedinProfile(form.getLinkedinProfile());

		result.setPersonalData(personalData);

//		personalDataService.save(personalData, result);

//		result.setPersonalData(personalData);

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();

		Curricula saved = this.save(result);
		logedRookie.getCurricula().add(saved);
		this.rookieService.save(logedRookie);

		return saved;
	}

	public Curricula copyCurricula(Curricula curricula) {
		Curricula result = this.create();
		Rookie rookie = rookieService.findByPrincipal();
		result.setCopied(true);
		result.setTitle(curricula.getTitle());
		// PersonalData
		PersonalData personalData = personalDataService.create();
		personalData.setFullName(curricula.getPersonalData().getFullName());
		personalData.setStatement(curricula.getPersonalData().getStatement());
		personalData.setPhoneNumber(curricula.getPersonalData().getPhoneNumber());
		personalData.setGithubProfile(curricula.getPersonalData().getGithubProfile());
		personalData.setLinkedinProfile(curricula.getPersonalData().getLinkedinProfile());
//		personalDataService.save(personalData, result);
		result.setPersonalData(personalData);
		this.save(result);
		// PositionData
		PositionData positionData = positionDataService.create();
		for (PositionData p : curricula.getPositionData()) {
			positionData.setTitle(p.getTitle());
			positionData.setDescription(p.getDescription());
			positionData.setStartDate(p.getStartDate());
			positionData.setEndDate(p.getEndDate());
			positionDataService.save(positionData, result);
		}
		// EducationData
		EducationData educationData = this.educationDataService.create();
		for (EducationData e : curricula.getEducationData()) {
			educationData.setDegree(e.getDegree());
			educationData.setInstitution(e.getInstitution());
			educationData.setMark(e.getMark());
			educationData.setStartDate(e.getStartDate());
			educationData.setEndDate(e.getEndDate());
			educationDataService.save(educationData, result);
		}
		// MiscellaneousData
		MiscellaneousData miscellaneousData = this.miscellaneousDataService.create();
		for (MiscellaneousData m : curricula.getMiscellaneousData()) {
			miscellaneousData.setText(m.getText());
			if (!m.getAttachments().isEmpty()) {
				miscellaneousData.setAttachments(m.getAttachments());
			}
			miscellaneousDataService.save(miscellaneousData, result);
		}
		
		Curricula saved = this.save(result);
		
		rookie.getCurricula().add(saved);
		rookieService.save(rookie);
		return saved;
	}

	public Collection<Curricula> findNotCopiedByUserAccount(UserAccount user) {
		Assert.notNull(user);
		Assert.isTrue(LoginService.getPrincipal().equals(user));
		Collection<Curricula> res = this.curriculaRepository.findByUserAccountIdNotCopied(user.getId());
		return res;
	}

	public Curricula findOneNotCopied(int curriculaId) {
		Assert.isTrue(this.exists(curriculaId));
		Curricula res = this.findOne(curriculaId);
		Assert.isTrue(res.isCopied() == false);
		return res;

	}

}

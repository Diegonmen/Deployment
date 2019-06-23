package services;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import domain.Curricula;
import domain.PersonalData;
import repositories.PersonalDataRepository;

@Service
@Transactional
public class PersonalDataService {

	@Autowired
	private PersonalDataRepository personalDataRepository;

	@Autowired
	private CurriculaService curriculaService;

	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private RookieService rookieService;

	public PersonalData save(PersonalData entity, Curricula curricula) {
		if(curricula.getId() != 0) {
			Assert.isTrue(rookieService.findByPrincipal().getCurricula().contains(curricula));
		}
		if (entity.getId() != 0) {
			Assert.notNull(this.curriculaService.findByPersonalData(entity)
					.getPersonalData().getId() == entity.getId());
		}
		if (!StringUtils.isEmpty(entity.getPhoneNumber())) {

			Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(entity.getPhoneNumber());

			if (matcher.matches()) {
				entity.setPhoneNumber(this.configurationService.findAll()
						.iterator().next().getCountryCode()
						+ entity.getPhoneNumber());
			}
		}

		PersonalData result = this.personalDataRepository.saveAndFlush(entity);

		curricula.setPersonalData(result);
		curriculaService.save(curricula);

		return result;
	}

	public List<PersonalData> findAll() {
		return personalDataRepository.findAll();
	}

	public PersonalData findOne(Integer id) {
		return personalDataRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return personalDataRepository.exists(id);
	}

	public void delete(Integer id) {
		personalDataRepository.delete(id);
	}

	public void flush() {
		configurationService.flush();
	}

	public PersonalData create() {
		PersonalData result = new PersonalData();

		return result;
	}

}

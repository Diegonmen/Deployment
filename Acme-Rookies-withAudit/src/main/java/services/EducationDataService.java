package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Curricula;
import domain.EducationData;
import repositories.EducationDataRepository;

@Service
@Transactional
public class EducationDataService {
	
	@Autowired
	private EducationDataRepository	educationDataRepository;
	
	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private RookieService rookieService;

	public EducationData save(EducationData entity, Curricula curricula) {
		if(curricula.getId() != 0) {
			Assert.isTrue(rookieService.findByPrincipal().getCurricula().contains(curricula));
		}
		EducationData saved = this.findOne(entity.getId());
		if(entity.getId() != 0) {
			Assert.notNull(this.curriculaService.findByEducationData(entity).getEducationData().contains(saved));
		}
		EducationData result = this.educationDataRepository.save(entity);
		if(entity.getId()==0) {
			curricula.getEducationData().add(entity);
			curriculaService.save(curricula);
		}
		
		return result;
	}

	public List<EducationData> findAll() {
		return educationDataRepository.findAll();
	}

	public EducationData findOne(Integer id) {
		return educationDataRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return educationDataRepository.exists(id);
	}

	public void delete(Integer id) {
		educationDataRepository.delete(id);
	}
	
	public void delete(domain.EducationData entity, Curricula curricula) {
		curricula.getEducationData().remove(entity);
		this.curriculaService.save(curricula);
		educationDataRepository.delete(entity);
	}
	
	public EducationData create() {
		EducationData result = new EducationData();
		return result;
	}
	
}

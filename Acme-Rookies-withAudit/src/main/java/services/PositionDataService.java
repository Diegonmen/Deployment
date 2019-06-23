package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Curricula;
import domain.PositionData;
import repositories.PositionDataRepository;

@Service
@Transactional
public class PositionDataService {
	
	@Autowired
	private PositionDataRepository	positionDataRepository;
	
	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private RookieService rookieService;

	public PositionData save(PositionData entity, Curricula curricula) {
		if(curricula.getId() != 0) {
			Assert.isTrue(rookieService.findByPrincipal().getCurricula().contains(curricula));
		}
		PositionData saved = this.findOne(entity.getId());
		if(entity.getId() != 0) {
			Assert.notNull(this.curriculaService.findByPositionData(entity).getPositionData().contains(saved));
		}
		PositionData result = this.positionDataRepository.save(entity);
		if(entity.getId()==0) {
			curricula.getPositionData().add(entity);
			curriculaService.save(curricula);
		}
		
		return result;
	}

	public List<PositionData> findAll() {
		return positionDataRepository.findAll();
	}

	public PositionData findOne(Integer id) {
		return positionDataRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return positionDataRepository.exists(id);
	}

	public void delete(Integer id) {
		positionDataRepository.delete(id);
	}
	
	public void flush() {
		curriculaService.flush();
	}

	public void delete(domain.PositionData entity, Curricula curricula) {
		curricula.getPositionData().remove(entity);
		this.curriculaService.save(curricula);
		positionDataRepository.delete(entity);
	}
	
	public PositionData create() {
		PositionData result = new PositionData();
		return result;
	}
	
}

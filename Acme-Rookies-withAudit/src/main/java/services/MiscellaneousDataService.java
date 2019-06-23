package services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Curricula;
import domain.MiscellaneousData;
import repositories.MiscellaneousDataRepository;

@Service
@Transactional
public class MiscellaneousDataService {
	
	@Autowired
	private MiscellaneousDataRepository	miscellaneousDataRepository;
	
	@Autowired
	private CurriculaService curriculaService;
	
	@Autowired
	private RookieService rookieService;

	public MiscellaneousData save(MiscellaneousData entity, Curricula curricula) {
		if(curricula.getId() != 0) {
			Assert.isTrue(rookieService.findByPrincipal().getCurricula().contains(curricula));
		}
		MiscellaneousData saved = this.findOne(entity.getId());
		if(entity.getId() != 0) {
			Assert.notNull(this.curriculaService.findByMiscellaneousData(entity).getMiscellaneousData().contains(saved));
		}
		MiscellaneousData result = this.miscellaneousDataRepository.save(entity);
		if(entity.getId()==0) {
			curricula.getMiscellaneousData().add(entity);
			curriculaService.save(curricula);
		}
		
		return result;
	}

	public List<MiscellaneousData> findAll() {
		return miscellaneousDataRepository.findAll();
	}

	public MiscellaneousData findOne(Integer id) {
		return miscellaneousDataRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return miscellaneousDataRepository.exists(id);
	}

	public void delete(Integer id) {
		miscellaneousDataRepository.delete(id);
	}
	
	public void delete(domain.MiscellaneousData entity, Curricula curricula) {
		curricula.getMiscellaneousData().remove(entity);
		this.curriculaService.save(curricula);
		miscellaneousDataRepository.delete(entity);
	}
	
	public MiscellaneousData create() {
		MiscellaneousData result = new MiscellaneousData();
		Collection<String> attachments = new LinkedList<>();
		result.setAttachments(attachments);
		return result;
	}
	
}

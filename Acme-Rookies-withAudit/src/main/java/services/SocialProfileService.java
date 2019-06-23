package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SocialProfileRepository;
import domain.Actor;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	// Managed repository -----------------------------------------------------
	
	@Autowired
	private ActorService				actorService;

	@Autowired
	private SocialProfileRepository		socialProfileRepository;

	public SocialProfile save(SocialProfile entity) {
		Actor logged = actorService.findByPrincipal();
		if(entity.getId()!=0){
			Assert.isTrue(logged.getSocialProfiles().contains(entity));
		}
		SocialProfile result = socialProfileRepository.save(entity);
		if(entity.getId()==0){
			Collection<SocialProfile> profiles = logged.getSocialProfiles();
			profiles.add(result) ;
			logged.setSocialProfiles(profiles);	
		}
		return result;
	}

	public List<SocialProfile> findAll() {
		return socialProfileRepository.findAll();
	}

	public SocialProfile findOne(Integer id) {
		return socialProfileRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return socialProfileRepository.exists(id);
	}
	
	public void flush() {
		socialProfileRepository.flush();
	}

	public void delete(Integer id) {
		SocialProfile socialProfile = socialProfileRepository.findOne(id);
		Assert.notNull(socialProfile);
		Actor logged = actorService.findByPrincipal();
		Assert.isTrue(logged.getSocialProfiles().contains(socialProfile));
		logged.getSocialProfiles().remove(socialProfile);
		socialProfileRepository.delete(id);
	}
	
	public SocialProfile create() {
		SocialProfile res = new SocialProfile();
		return res;
	}
	
	public SocialProfile findOneToEdit(Integer id) {
		Actor loggedActor = actorService.findByPrincipal();
		Collection<SocialProfile> socialProfiles =  loggedActor.getSocialProfiles();
		SocialProfile socialProfile = socialProfileRepository.findOne(id);
		Assert.isTrue(socialProfiles.contains(socialProfile));
		return socialProfile;
	}

	public void delete(Iterable<SocialProfile> entities) {
		socialProfileRepository.delete(entities);
	}
	
	

}

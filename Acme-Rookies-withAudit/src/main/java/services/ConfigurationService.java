
package services;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ConfigurationRepository;
import domain.Configuration;

@Service
@Transactional
public class ConfigurationService {

	@Autowired
	private ConfigurationRepository	configurationRepository;
	@Autowired
	private AdministratorService	administratorService;


	public Configuration save(Configuration entity) {
		Assert.notNull(this.administratorService.findByPrincipal());
		Assert.isTrue(this.exists(entity.getId()));
		Collection<String> spamWords = new HashSet<>();
		spamWords.addAll(entity.getSpamWords());
		entity.setUsersHaveBeenNotified(this.findConfiguration().getUsersHaveBeenNotified());

		while (spamWords.iterator().hasNext()) {
			String s = spamWords.iterator().next();
			if (s.trim().equals("") || s == null)
				Assert.isTrue(false);
			spamWords.remove(s);
		}

		return this.configurationRepository.save(entity);
	}

	public List<Configuration> findAll() {
		return this.configurationRepository.findAll();
	}

	public Configuration findOne(Integer id) {
		return this.configurationRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return this.configurationRepository.exists(id);
	}

	public void flush() {
		this.configurationRepository.flush();
	}

	public Configuration findConfiguration() {
		return this.configurationRepository.findAll().iterator().next();
	}

	public void triggerUsersNotifiedRebranding() {
		Configuration c = this.findConfiguration();
		c.setUsersHaveBeenNotified(true);
		this.configurationRepository.save(c);
	}

	public String findCountryCode() {
		String result;

		result = this.findConfiguration().getCountryCode();

		return result;
	}

}

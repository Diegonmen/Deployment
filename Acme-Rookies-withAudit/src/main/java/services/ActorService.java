
package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	@Autowired
	private ActorRepository	actorRepository;


	public Actor save(Actor entity) {
		return this.actorRepository.save(entity);
	}

	public List<Actor> findAll() {
		return this.actorRepository.findAll();
	}

	public Actor findOne(Integer id) {
		return this.actorRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return this.actorRepository.exists(id);
	}

	public void delete(Integer id) {
		this.actorRepository.delete(id);
	}

	// Other business methods -------------------------------------------------

	public Actor findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);
		Actor result;
		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public Actor findByPrincipal() {
		Actor result;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		if (userAccount != null)
			result = this.findByUserAccount(userAccount);
		else
			result = null;
		return result;
	}

	public Actor findByActorId(int id) {
		Assert.notNull(id);
		Actor res = this.actorRepository.findByActorId(id);

		return res;
	}

	public void updateSpammer(Actor c, boolean spammer) {
		Assert.isTrue(c.getIsSpammer() != spammer);
		c.setIsSpammer(spammer);
		this.actorRepository.save(c);

	}

	public void updateBan(Actor a, boolean v) {
		Assert.isTrue(a.getIsBanned() != v);
		a.setIsBanned(v);
		this.actorRepository.save(a);
	}

	public Collection<Actor> findSpammer() {
		return this.actorRepository.findSpammer();
	}

}

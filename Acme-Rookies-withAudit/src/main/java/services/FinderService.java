package services;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import domain.Actor;
import domain.Finder;
import domain.Rookie;
import domain.Position;

@Service
@Transactional
public class FinderService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private FinderRepository		finderRepository;

	@Autowired
	private RookieService			rookieService;

	@Autowired
	private PositionService			positionService;
	
	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private Validator				validator;


	public Finder save(Finder finder) {
		
		Finder result;
		
		
		if(finder.getId() != 0) {
			Rookie loggedRookie = rookieService.findByPrincipal();
			Assert.isTrue(loggedRookie.getFinder().getId() == finder.getId(), "This is not your finder");
		}
		
		finder.setSearchMoment(new Date(System.currentTimeMillis() - 1));

		String keyword = finder.getKeyword();
		Double minimumSalary = finder.getMinimumSalary();
		Double maximumSalary = finder.getMaximumSalary();
		Date deadline = finder.getDeadline();

		finder.setPositions(this.positionService.findPositions(keyword, minimumSalary, maximumSalary, deadline));

		result = this.finderRepository.save(finder);
		return result;
		
	}

	public List<Finder> findAll() {
		return this.finderRepository.findAll();
	}

	public Finder findOne(Integer id) {
		return this.finderRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return this.finderRepository.exists(id);
	}
	
	public void flush() {
		this.finderRepository.flush();
	}

	public Finder create() {
		Finder res = new Finder();
		Collection<Position> positions = new LinkedList<>();
		res.setPositions(positions);
		return res;
	}

	public Finder findFinderByPrincipal() {
		Finder result;
		Rookie logged = this.rookieService.findByPrincipal();
		result = this.finderRepository.findFinderForActorId(logged.getId());
		return result;
	}
	
	public Collection<Finder> findFindersSearchingPosition(Integer positionId) {
		Collection<Finder> result = this.finderRepository.findFinderSearchingPosition(positionId);
		return result;
	}

	public Finder clear() {
		Rookie loggedRookie = rookieService.findByPrincipal();
		Finder finder = loggedRookie.getFinder();
		finder.setKeyword(null);
		finder.setMinimumSalary(null);
		finder.setMaximumSalary(null);
		finder.setDeadline(null);
		Finder result = this.save(finder);
		return result;
	}

	public Finder reconstruct(final Finder finder, final BindingResult binding) {
		Finder result;

		if (finder.getId() == 0) {
			result = finder;
			Collection<Position> positions = new LinkedList<>();
			result.setPositions(positions);
		} else {
			Rookie loggedRookie = rookieService.findByPrincipal();
			Assert.isTrue(loggedRookie.getFinder().getId() == finder.getId(), "This is not your finder");
			result = this.finderRepository.findOne(finder.getId());
		}

		result.setKeyword(finder.getKeyword());
		result.setMinimumSalary(finder.getMinimumSalary());
		result.setMaximumSalary(finder.getMaximumSalary());
		result.setDeadline(finder.getDeadline());

		this.validator.validate(result, binding);
		if(binding.hasErrors())
			throw new ValidationException();

		return result;
	}
	
	public Collection<Actor> findRookiesWithFindersMatchingParameters(String title, String description, String ticker, String skills, String technologies, String profile, Date deadline, Double salary) {
		return finderRepository.findRookiesWithFindersMatchingParameters(title, description, ticker, skills, technologies, profile, deadline, salary);
	}
	
	public boolean finderHasChangedOrIsExpired(Finder finder) {
		Finder originalFinder = this.findOne(finder.getId());
		Date cacheDate = new Date(System.currentTimeMillis() + 1 - configurationService.findConfiguration().getCache()*3600*1000);
		return !(originalFinder.equals(finder)) || originalFinder.getSearchMoment().before(cacheDate);
	}

}

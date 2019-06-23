
package services;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ProblemRepository;
import domain.Application;
import domain.Company;
import domain.Problem;

@Service
@Transactional
public class ProblemService {

	@Autowired
	private ProblemRepository	problemRepository;

	@Autowired
	private CompanyService		companyService;
	
	@Autowired
	private ApplicationService		applicationService;


	public Problem create() {
		final Problem problem = new Problem();
		problem.setDraftMode(true);

		return problem;
	}

	public Problem save(final Problem entity) {
		Problem saved = this.problemRepository.save(entity);

		Company creator = this.companyService.findByPrincipal();
		
		if(entity.getPosition()==null && entity.getId()==0)
				Assert.isTrue(entity.isDraftMode());
		
		
		if (entity.getId() == 0)
			creator.getProblems().add(saved);

		return saved;
	}

	public Set<Problem> findProblemsOfPosition(final int positionId){
		return this.problemRepository.findProblemsByPositionId(positionId);
	}


	public List<Problem> findAll() {
		return this.problemRepository.findAll();
	}

	public Problem findOne(final Integer id) {
		return this.problemRepository.findOne(id);
	}

	public Problem findOneToEdit(final Integer id) {
		final Company company = this.companyService.findByPrincipal();
		final Problem problem = this.findOne(id);
		Assert.isTrue(company.getProblems().contains(problem));
		Assert.isTrue(problem.isDraftMode());
		return problem;
	}

	public boolean exists(final Integer id) {
		return this.problemRepository.exists(id);
	}

	public void delete(final Integer id) {
		final Company company = this.companyService.findByPrincipal();
		final Problem problem = this.findOne(id);
		Collection<Application> applications = this.applicationService.findApplicationToProblem(id);
		Assert.isTrue(applications.size()==0,"has.applications");
		Assert.isTrue(problem.isDraftMode());
		company.getProblems().remove(problem);
		
		this.problemRepository.delete(id);
		this.companyService.save(company);
	}

	public Collection<Problem> findProblemsForPositionId(final int positionId) {
		return this.problemRepository.findProblemsByPositionId(positionId);
	}
	
	public Collection<Problem> findProblemsByPositionIdToDelete(final int positionId) {
		return this.problemRepository.findProblemsByPositionIdToDelete(positionId);
	}
	



}

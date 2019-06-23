package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;
import domain.Company;
import domain.Curricula;
import domain.Rookie;
import domain.Position;
import domain.Problem;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer>{

	@Query("select distinct h from Rookie h join h.applications a join a.problem.position p where p.id = ?1 and h.id = ?2 and (a.status = 'PENDING' or a.status = 'SUBMITTED' or a.status = 'ACCEPTED')")
	public Rookie checkExistingApplications(int positionId, int rookieId);

	@Query("select p from Problem p where p.position.id = ?1 and p.draftMode = false")
	public Collection<Problem> assignRandomProblem(int positionId);

	@Query("select a from Application a where a.problem.position.id = (select p from Company c join c.positions p where p.id = ?1 and c.id = ?2)")
	public Collection<Application> applicationsForPosition(int positionId, int companyId);

	@Query("select a from Application a join a.problem.position p where p.id = ?1 and (a.status='SUBMITTED' or a.status='REJECTED' or a.status='ACCEPTED')")
	public Collection<Application> findApplicationsForPosition(int positionId);

	@Query("select p from Rookie h join h.applications a join a.problem.position p where h.id = ?1 and a.status!='REJECTED'")
	public Collection<Position> checkForApplyingToPositions(int rookieId);

	@Query("select c from Rookie h join h.curricula c where h.id = ?1 and c.copied = false")
	public Collection<Curricula> selectCurricula(int rookieId);

	@Query("select a from Application a where a.problem.id=?1")
	public Collection<Application> findApplicationsToProblem(int problemId);

	@Query("select p from Company c join c.positions p where c.id = ?1 and p.id = ?2")
	public Position checkOwner(int companyId, int positionId);

	@Query("select h from Rookie h join h.applications a where a.id = ?1")
	public Rookie notifyRookie(int applicationId);

	@Query("select c from Company c join c.positions p where p.id = ?1")
	public Company notifyCompany(int positionId);
	
	@Query("select a from Application a where a.curricula.id = ?1")
	public Collection<Application> findApplicationsByCurricula(int curriculaId);
}

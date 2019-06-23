
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Audit;
import domain.Position;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Integer> {

	@Query("select a from Audit a where a.auditor.id = ?1")
	List<Audit> principalAudits(Integer id);

	@Query("select a from Position p join p.audits a where p.id = ?1 and a.draftMode = false")
	List<Audit> findAuditsForPosition(Integer id);

	@Query("select p from Position p join p.audits a where a.id = ?1")
	Position findPositionForAudit(int auditId);

	@Query("select avg(a.score) from Audit a, Company c join c.positions p where c.id = ?1 and a.position = p")
	Double findAvgAuditScoreForCompany(int companyId);

}

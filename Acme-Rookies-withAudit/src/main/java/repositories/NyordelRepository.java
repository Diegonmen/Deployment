
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Company;
import domain.Nyordel;

@Repository
public interface NyordelRepository extends JpaRepository<Nyordel, Integer> {

	@Query("select n from Nyordel n where n.audit.id = ?1")
	public List<Nyordel> findAllByAuditId(int auditId);

	@Query("select n from Nyordel n where n.audit.id = ?1 and n.draftMode = false")
	public List<Nyordel> findAllByAuditIdInFinal(int auditId);

	@Query("select distinct n from Nyordel n, Company c join c.positions p where c.id = ?1 and n.audit.position.id = p.id")
	public List<Nyordel> findAllByCompanyId(int companyId);

	@Query("select n from Nyordel n where n.ticker = ?1")
	public Nyordel findOneWithSameTicker(String ticker);

	@Query("select distinct c from Nyordel n, Company c join c.positions p join p.audits a where n.audit.id = a.id and n.id = ?1")
	public Company findCompanyByNyordelId(int id);

}

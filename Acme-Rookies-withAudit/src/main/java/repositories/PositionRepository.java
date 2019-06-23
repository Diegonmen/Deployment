package repositories;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer>{
	
	@Query("select p from Position p where p.draftMode=false and p.cancelled=false and (?1 is null or (p.title like %?1% or p.description like %?1% or p.skills like %?1% or p.technologies like %?1% or p.profile like %?1%)) and ((?2 is null or p.salary >= ?2) and (?3 is null or p.salary <= ?3)) and (?4 is null or p.deadline < ?4)")
	public List<Position> findPositions(String keyword, Double minimumSalary, Double maximumSalary, Date deadline, Pageable pageable);
	
	@Query("select p from Company c join c.positions p where (p.title like %?1% or p.description like %?1% or p.skills like %?1% or p.technologies like %?1% or p.profile like %?1% or c.commercialName like %?1%) and p.draftMode=false and p.cancelled=false")
	public List<Position> findPositionsByKeyword(String keyword);

	@Query("select p from Position p where p.ticker = ?1")
	public Position findPositionsWithSameTicker(String ticker);
	
	@Query("select p from Company c join c.positions p where c.id=?1 AND p.draftMode=false AND p.cancelled=false")
	public Collection<Position> findPositionsInFinalFromCompanyId(int companyId);
	
	@Query("select p from Position p where p.draftMode=false AND p.cancelled=false AND p.deadline >= ?1")
	public Collection<Position> findPublicPositions(Date today);
	
	@Query("select p from Company c join c.positions p where c.id=?1 AND p.draftMode=true AND p.cancelled=false AND p.deadline >= ?2")
	public Collection<Position> findPositionsNotCancelled(int companyId, Date today);
	
	@Query("select a from Position p join p.audits a where a.id = ?1")
	public List<Position> findAuditsForPosition(Integer id);
	
	@Query("select distinct p from Position p, Nyordel n join p.audits a where n.id = ?1 and n.audit = a")
	public Position findPositionByNyordelId(Integer id);
}

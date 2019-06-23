package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer>{


	@Query("select f from Rookie h join h.finder f where h.id = ?1")
	public Finder findFinderForActorId(int id);

	@Query("select f from Finder f join f.positions p where p.id = ?1")
	public Collection<Finder> findFinderSearchingPosition(int id);
	
	@Query("select h from Rookie h join h.finder f where (f.keyword is null or f.keyword = '' or ?1 like concat('%', f.keyword,'%') or ?2 like concat('%', f.keyword,'%') or ?3 like concat('%', f.keyword,'%') or ?4 like concat('%', f.keyword,'%') or ?5 like concat('%', f.keyword,'%') or ?6 like concat('%', f.keyword,'%')) and (f.deadline is null or ?7 < f.deadline) and (f.minimumSalary is null or (f.minimumSalary < ?8 and f.maximumSalary > ?8))")
	public Collection<Actor> findRookiesWithFindersMatchingParameters(String title, String description, String ticker, String skills, String technologies, String profile, Date deadline, Double salary);

}

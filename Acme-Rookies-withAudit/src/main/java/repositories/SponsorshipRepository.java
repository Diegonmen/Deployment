
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Position;
import domain.Provider;
import domain.Sponsorship;

public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {
	
	@Query("select s from Position p join p.sponsorships s join s.creditCard c where p.id = ?1 and (c.expirationYear > year(curdate())%100 or c.expirationYear = year(curdate())%100 and c.expirationMonth <= month(curdate()))")
	public Collection<Sponsorship> getActiveSponsorshipsByPositionId(int id);
	
	@Query("select distinct p from Provider p join p.sponsorships s where s.id = ?1")
	public Provider getProviderBySponsorshipId(int id);
	
	@Query("select distinct p from Position p join p.sponsorships s where s.id = ?1")
	public Position getPositionBySponsorshipId(Integer id);

}

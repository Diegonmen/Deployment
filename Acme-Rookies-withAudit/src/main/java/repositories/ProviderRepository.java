package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer>{

	@Query("select p from Provider p where p.userAccount.id = ?1")
	Provider findByUserAccountId(int userAccountId);

	@Query("select p from Provider p join p.items i where i.id = ?1")
	Provider findByItemId(int itemId);

	@Query("select p from Provider p join p.sponsorships sp where sp.id = ?1")
	public Provider getProviderBySponsorshipId(int id);

}

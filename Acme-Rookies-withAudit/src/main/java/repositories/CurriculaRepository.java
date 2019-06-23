package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Curricula;

@Repository
public interface CurriculaRepository extends JpaRepository<Curricula, Integer>{
	
	@Query("select b.curricula from Rookie b where b.userAccount.id = ?1")
	Collection<Curricula> findByUserAccountId(int userAccountId);
	
	@Query("select c from Curricula c where c.id = ?1")
	Curricula findByCurriculaId(int curriculaId);
	
	@Query("select c from Curricula c where c.personalData.id = ?1")
	Curricula findByPersonalDataId(int personalDataId);
	
	@Query("select c from Curricula c join c.positionData d where d.id = ?1")
	Curricula findByPositionData(int positionData);
	
	@Query("select c from Curricula c join c.educationData d where d.id = ?1")
	Curricula findByEducationData(int educationData);
	
	@Query("select c from Curricula c join c.miscellaneousData d where d.id = ?1")
	Curricula findByMiscellaneousData(int miscellaneousData);
	
	@Query("select c from Rookie b join b.curricula c where b.userAccount.id = ?1 and c.copied=false")
	 Collection<Curricula> findByUserAccountIdNotCopied(int userAccountId);
}

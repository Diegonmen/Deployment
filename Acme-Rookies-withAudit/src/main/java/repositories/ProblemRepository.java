package repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer>{
	
	@Query("select p from Problem p where p.position.id = ?1 and p.draftMode=false")
	public Set<Problem> findProblemsByPositionId(int positionId);
	
	@Query("select p from Problem p where p.position.id = ?1")
	public Set<Problem> findProblemsByPositionIdToDelete(int positionId);
	
	

}

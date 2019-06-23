
package repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Company;
import domain.Position;
import domain.Provider;
import domain.Rookie;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select a from Administrator a where a.userAccount.id = ?1")
	Administrator findByUserAccountId(int userAccountId);

	// DASHBOARD QUERIES
	@Query("select avg(c.positions.size), min(c.positions.size), max(c.positions.size), sqrt(sum(c.positions.size* c.positions.size)/count(c.positions.size) - (avg(c.positions.size)*avg(c.positions.size))) from Company c")
	Double[] findAvgMinMaxStdevPositionsPerCompany();

	@Query("select avg(c.applications.size), min(c.applications.size), max(c.applications.size), sqrt(sum(c.applications.size* c.applications.size)/count(c.applications.size) - (avg(c.applications.size)*avg(c.applications.size))) from Rookie c")
	Double[] findAvgMinMaxStdevApplicationsPerRookie();

	@Query("select c from Company c where c.positions.size >= (select max(c2.positions.size) from Company c2)*0.75 order by c.positions.size desc")
	List<Company> findCompaniesMorePositions();

	@Query("select c from Rookie c where c.applications.size >= (select max(c2.applications.size) from Rookie c2)*0.75 order by c.applications.size desc")
	List<Rookie> findRookiesMoreApplications();

	@Query("select avg(c.salary), min(c.salary), max(c.salary), sqrt(sum(c.salary* c.salary)/count(c.salary) - (avg(c.salary)*avg(c.salary))) from Position c")
	Double[] findAvgMinMaxStdevSalaries();

	@Query("select p from Position p where p.salary = (select max(p2.salary) from Position p2))")
	List<Position> selectPositionsGreatestSalary();

	@Query("select p from Position p where p.salary = (select min(p2.salary) from Position p2))")
	List<Position> selectPositionsLowestSalary();

	@Query("select avg(c.curricula.size), min(c.curricula.size), max(c.curricula.size), sqrt(sum(c.curricula.size* c.curricula.size)/count(c.curricula.size) - (avg(c.curricula.size)*avg(c.curricula.size))) from Rookie c")
	Double[] findAvgMinMaxStdevCurriculaPerRookie();

	@Query("select avg(c.positions.size), min(c.positions.size), max(c.positions.size), sqrt(sum(c.positions.size* c.positions.size)/count(c.positions.size) - (avg(c.positions.size)*avg(c.positions.size))) from Finder c")
	Double[] findAvgMinMaxStdevResultsPerFinder();

	@Query("select count(distinct f) from Finder f where f.keyword = Null and f.deadline = Null and f.minimumSalary = null and f.maximumSalary = null")
	public Double findCountEmptyFinders();

	@Query("select count(distinct f) from Finder f")
	public Double findCountFinders();

	// D04 DASHBOARD QUERIES

	@Query("select avg(c.auditScore), min(c.auditScore), max(c.auditScore), sqrt(sum(c.auditScore* c.auditScore)/count(c.auditScore) - (avg(c.auditScore)*avg(c.auditScore))) from Company c")
	public Double[] findAvgMinMaxStdevAuditScoreForCompanies();

	@Query("select p from Company p where p.auditScore = (select max(p2.auditScore) from Company p2))")
	List<Company> selectCompaniesGreatestAuditScore();

	@Query("select avg(c.sponsorships.size), min(c.sponsorships.size), max(c.sponsorships.size), sqrt(sum(c.sponsorships.size* c.sponsorships.size)/count(c.sponsorships.size) - (avg(c.sponsorships.size)*avg(c.sponsorships.size))) from Provider c")
	public Double[] findAvgMinMaxStdevSponsorshipsPerProvider();

	@Query("select avg(c.sponsorships.size), min(c.sponsorships.size), max(c.sponsorships.size), sqrt(sum(c.sponsorships.size* c.sponsorships.size)/count(c.sponsorships.size) - (avg(c.sponsorships.size)*avg(c.sponsorships.size))) from Position c")
	public Double[] findAvgMinMaxStdevSponsorshipsPerPosition();

	@Query("select p from Provider p where p.sponsorships.size > (select 1.1*avg(p2.sponsorships.size) from Provider p2)")
	public List<Provider> getProvidersMoreSponsorships();

	@Query("select avg(p0.salary) from Position p0 where (select avg(a.score) from p0.audits a) = (select max(1.0*(select avg(a.score) from Audit a where a.position = p)) from Position p)")
	public Double getAvgSalaryForBestRatedPositions();

	@Query("select avg(1.0*(select avg(a.score) from Audit a where a.position = p)), min(1.0*(select avg(a.score) from Audit a where a.position = p)), max(1.0*(select avg(a.score) from Audit a where a.position = p)), stddev((select avg(a.score) from Audit a where a.position = p)) from Position p")
	public Double[] findAvgMinMaxStdevScoreForPositions();

	@Query("select avg(c.items.size), min(c.items.size), max(c.items.size), sqrt(sum(c.items.size* c.items.size)/count(c.items.size) - (avg(c.items.size)*avg(c.items.size))) from Provider c")
	public Double[] avgMinMaxStdevItemsPerProvider();

	@Query("select c from Provider c order by c.items.size desc")
	public Page<Provider> top5ProvidersByItems(Pageable pageable);

}

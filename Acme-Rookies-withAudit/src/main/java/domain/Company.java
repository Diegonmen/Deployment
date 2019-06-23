
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "commercialName")
})
public class Company extends Actor {

	private String	email;
	private String	commercialName;
	private Double	auditScore;


	@NotBlank
	@SafeHtml
	public String getCommercialName() {
		return this.commercialName;
	}

	public void setCommercialName(String commercialName) {
		this.commercialName = commercialName;
	}

	@NotBlank
	@SafeHtml
	@Pattern(regexp = "^([A-z0-9 ]+[ ]<[A-z0-9]+@([A-z0-9]+\\.{0,1})+[A-z0-9]+>|[A-z0-9]+@([A-z0-9]+\\.{0,1})+[A-z0-9]+)$")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Range(min = 0, max = 10)
	public Double getAuditScore() {
		return this.auditScore;
	}


	public void setAuditScore(Double auditScore) {
		this.auditScore = auditScore;
	}

	// --------- Relationships -------------


	private Collection<Problem>		problems;
	private Collection<Position>	positions;


	@OneToMany
	@Valid
	@NotNull
	public Collection<Problem> getProblems() {
		return this.problems;
	}

	public void setProblems(Collection<Problem> problems) {
		this.problems = problems;
	}

	@OneToMany
	@Valid
	@NotNull
	public Collection<Position> getPositions() {
		return this.positions;
	}

	public void setPositions(Collection<Position> positions) {
		this.positions = positions;
	}

}

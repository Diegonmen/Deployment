
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Rookie extends Actor {

	private String	email;


	@NotBlank
	@SafeHtml
	@Pattern(regexp = "^([A-z0-9 ]+[ ]<[A-z0-9]+@([A-z0-9]+\\.{0,1})+[A-z0-9]+>|[A-z0-9]+@([A-z0-9]+\\.{0,1})+[A-z0-9]+)$")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	// --------- Relationships -------------

	private Collection<Application>	applications;
	private Finder					finder;
	private Collection<Curricula>	curricula;


	@OneToMany
	public Collection<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(Collection<Application> applications) {
		this.applications = applications;
	}

	@OneToOne(optional = false)
	public Finder getFinder() {
		return this.finder;
	}

	public void setFinder(Finder finder) {
		this.finder = finder;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public Collection<Curricula> getCurricula() {
		return this.curricula;
	}

	public void setCurricula(Collection<Curricula> curricula) {
		this.curricula = curricula;
	}

}

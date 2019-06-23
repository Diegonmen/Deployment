package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Administrator extends Actor{
	
	private String email;
	
	@NotBlank
	@SafeHtml
	@Pattern(regexp = "^([A-z0-9 ]+[ ]<[A-z0-9]+@(([A-z0-9]+\\.{0,1})+[A-z0-9]+){0,}>|[A-z0-9]+@(([A-z0-9]+\\.{0,1})+[A-z0-9]+){0,})$")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

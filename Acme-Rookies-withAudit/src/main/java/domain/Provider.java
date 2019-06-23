package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Provider extends Actor {

	private String email;
	private String make;

	@NotBlank
	@SafeHtml
	public String getMake() {
		return this.make;
	}

	public void setMake(String make) {
		this.make = make;
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

	// --------- Relationships -------------

	private Collection<Sponsorship> sponsorships;
	private Collection<Item>		items;

	@OneToMany
	public Collection<Item> getItems() {
		return this.items;
	}


	public void setItems(Collection<Item> items) {
		this.items = items;
	}

	@OneToMany(cascade=CascadeType.ALL)
	public Collection<Sponsorship> getSponsorships(){
		return this.sponsorships;
	}

	public void setSponsorships(Collection<Sponsorship> sponsorships){
		this.sponsorships = sponsorships;
	}

}

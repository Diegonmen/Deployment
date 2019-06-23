
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import cz.jirutka.validator.collection.constraints.EachURL;

@Entity
@Access(AccessType.PROPERTY)
public class Item extends DomainEntity {

	// Attributes

	private String				name;
	private String				description;
	private Collection<String>	links;
	private Collection<String>	pictures;


	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@EachURL
	@ElementCollection(targetClass = String.class)
	public Collection<String> getLinks() {
		return this.links;
	}
	public void setLinks(Collection<String> links) {
		this.links = links;
	}

	@EachURL
	@ElementCollection(targetClass = String.class)
	public Collection<String> getPictures() {
		return this.pictures;
	}
	public void setPictures(Collection<String> pictures) {
		this.pictures = pictures;
	}
}

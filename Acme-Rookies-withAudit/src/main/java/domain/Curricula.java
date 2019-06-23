
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
		@Index(columnList = "copied")
	})
public class Curricula extends DomainEntity {

	private boolean copied;
	private String title;

	public boolean isCopied() {
		return this.copied;
	}

	public void setCopied(boolean copied) {
		this.copied = copied;
	}

	@SafeHtml
	@NotBlank
	public String getTitle() {
		return this.title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	// Relationships --------------------------

	private Collection<PositionData> positionData;
	private PersonalData personalData;
	private Collection<EducationData> educationData;
	private Collection<MiscellaneousData> miscellaneousData;

	@OneToMany(cascade = CascadeType.ALL)
	public Collection<PositionData> getPositionData() {
		return this.positionData;
	}

	public void setPositionData(Collection<PositionData> positionData) {
		this.positionData = positionData;
	}

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	public PersonalData getPersonalData() {
		return this.personalData;
	}

	public void setPersonalData(PersonalData personalData) {
		this.personalData = personalData;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public Collection<EducationData> getEducationData() {
		return this.educationData;
	}

	public void setEducationData(Collection<EducationData> educationData) {
		this.educationData = educationData;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public Collection<MiscellaneousData> getMiscellaneousData() {
		return this.miscellaneousData;
	}

	public void setMiscellaneousData(Collection<MiscellaneousData> miscellaneousData) {
		this.miscellaneousData = miscellaneousData;
	}

}

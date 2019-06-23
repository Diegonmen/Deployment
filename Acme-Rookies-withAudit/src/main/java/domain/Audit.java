
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Audit extends DomainEntity {

	private Date	moment;
	private String	text;
	private Double	score;
	private boolean	draftMode;


	@Past
	//	@NotNull
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	//	@NotBlank
	@SafeHtml
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	//	@Range(min = 0, max = 10)
	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public boolean isDraftMode() {
		return this.draftMode;
	}

	public void setDraftMode(boolean draftMode) {
		this.draftMode = draftMode;
	}


	// Relationships

	private Auditor		auditor;
	private Position	position;


	@ManyToOne
	public Position getPosition() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@ManyToOne(optional = false)
	@Valid
	public Auditor getAuditor() {
		return this.auditor;
	}

	public void setAuditor(Auditor auditor) {
		this.auditor = auditor;
	}

}

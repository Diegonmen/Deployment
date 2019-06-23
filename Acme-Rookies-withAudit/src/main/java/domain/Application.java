
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
		@Index(columnList = "status")
	})
public class Application extends DomainEntity {

	// Attributes

	private String	status;
	private String	answer;
	private String	answerLink;
	private Date	answerMoment;
	private Date	moment;


	@Pattern(regexp = "^PENDING|SUBMITTED|ACCEPTED|REJECTED$")
	@SafeHtml
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@SafeHtml
	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(final String answer) {
		this.answer = answer;
	}

	@URL
	@SafeHtml
	public String getAnswerLink() {
		return this.answerLink;
	}

	public void setAnswerLink(final String answerLink) {
		this.answerLink = answerLink;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getAnswerMoment() {
		return this.answerMoment;
	}

	public void setAnswerMoment(final Date answerMoment) {
		this.answerMoment = answerMoment;
	}

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	// Relationships

	private Problem problem;
	private Curricula curricula;

	@Valid
	@ManyToOne(optional = false)
	public Problem getProblem(){
		return this.problem;
	}

	public void setProblem(final Problem problem){
		this.problem = problem;
	}

	@Valid
	@OneToOne(optional = false)
	public Curricula getCurricula(){
		return this.curricula;
	}

	public void setCurricula(final Curricula curricula){
		this.curricula = curricula;
	}
}

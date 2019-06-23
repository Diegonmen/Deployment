package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "keyword,deadline,minimumSalary,maximumSalary")
})
public class Finder extends DomainEntity {

	// Attributes

	private String keyword;
	private Date deadline;
	private Double minimumSalary;
	private Double maximumSalary;
	private Date searchMoment;

	@SafeHtml
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(final String keyword) {
		this.keyword = keyword;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(final Date deadline) {
		this.deadline = deadline;
	}

	@Min(value = 0)
	public Double getMaximumSalary() {
		return this.maximumSalary;
	}

	public void setMaximumSalary(final Double maximumSalary) {
		this.maximumSalary = maximumSalary;
	}

	@Min(value = 0)
	public Double getMinimumSalary() {
		return this.minimumSalary;
	}

	public void setMinimumSalary(final Double minimumSalary) {
		this.minimumSalary = minimumSalary;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSearchMoment() {
		return this.searchMoment;
	}

	public void setSearchMoment(final Date searchMoment) {
		this.searchMoment = searchMoment;
	}

	// Relationships

	private Collection<Position> positions;

	@ManyToMany
	public Collection<Position> getPositions() {
		return this.positions;
	}


	public void setPositions(final Collection<Position> positions) {
		this.positions = positions;
	}

	// Additional methods

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Finder other = (Finder) obj;
		if (other.deadline == null) {
			if (this.deadline != null)
				return false;
		} else if (this.deadline == null) {
			if (other.deadline != null)
				return false;
		} else if (this.deadline.getTime() != other.deadline.getTime())
			return false;
		if (this.keyword == null) {
			if (other.keyword != null)
				return false;
		} else if (!this.keyword.equals(other.keyword))
			return false;
		if (this.maximumSalary == null) {
			if (other.maximumSalary != null)
				return false;
		} else if (!this.maximumSalary.equals(other.maximumSalary))
			return false;
		if (this.minimumSalary == null) {
			if (other.minimumSalary != null)
				return false;
		} else if (!this.minimumSalary.equals(other.minimumSalary))
			return false;
		return true;
	}

}

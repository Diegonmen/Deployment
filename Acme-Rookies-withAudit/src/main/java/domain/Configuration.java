
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	private String				sysName;
	private String				welcomeMessageEng;
	private String				welcomeMessageEsp;
	private String				bannerURL;
	private String				countryCode;
	private int					cache;
	private int					finderResults;
	private Collection<String>	spamWords;
	private boolean				usersHaveBeenNotified;
	private double				sponsorshipFare;
	private double				vat;


	public boolean getUsersHaveBeenNotified() {
		return this.usersHaveBeenNotified;
	}

	public void setUsersHaveBeenNotified(boolean usersHaveBeenNotified) {
		this.usersHaveBeenNotified = usersHaveBeenNotified;
	}

	@NotBlank
	@SafeHtml
	public String getSysName() {
		return this.sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	@NotBlank
	@SafeHtml
	public String getWelcomeMessageEng() {
		return this.welcomeMessageEng;
	}

	public void setWelcomeMessageEng(String welcomeMessageEng) {
		this.welcomeMessageEng = welcomeMessageEng;
	}

	@NotBlank
	@SafeHtml
	public String getWelcomeMessageEsp() {
		return this.welcomeMessageEsp;
	}

	public void setWelcomeMessageEsp(String welcomeMessageEsp) {
		this.welcomeMessageEsp = welcomeMessageEsp;
	}

	@NotBlank
	@SafeHtml
	@URL
	public String getBannerURL() {
		return this.bannerURL;
	}

	public void setBannerURL(String bannerURL) {
		this.bannerURL = bannerURL;
	}

	@NotBlank
	@Pattern(regexp = "^\\+\\d{1,3}$")
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Range(min = 1, max = 24)
	public int getCache() {
		return this.cache;
	}

	public void setCache(int cache) {
		this.cache = cache;
	}

	@Range(min = 1, max = 100)
	public int getFinderResults() {
		return this.finderResults;
	}

	public void setFinderResults(int finderResults) {
		this.finderResults = finderResults;
	}

	@ElementCollection(targetClass = String.class)
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	@Min(value = 0)
	public double getVat() {
		return this.vat;
	}

	public void setVat(double vat) {
		this.vat = vat;
	}

	@Min(value = 0)
	public double getSponsorshipFare() {
		return this.sponsorshipFare;
	}

	public void setSponsorshipFare(double sponsorshipFare) {
		this.sponsorshipFare = sponsorshipFare;
	}

}

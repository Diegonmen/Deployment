package forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

public class AuditorForm {

		// Actor
		private String name;
		private String surname;
		private String picture;
		private String phoneNumber;
		private String address;
		private String vat;
		private int id;
		// Auditor
		private String email;
		// CreditCard
		private String number;
		private Integer cvv;
		private String holderName;
		private String make;
		private Integer expirationMonth;
		private Integer expirationYear;
		// UserAccount
		private String username;
//		private String oldPassword;
		private String newPassword;
		private String confirmPassword;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		@SafeHtml(whitelistType = WhiteListType.NONE)
		@Size(min = 5, max = 32)
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

//		public String getOldPassword() {
//			return oldPassword;
//		}
//
//		public void setOldPassword(String oldPassword) {
//			this.oldPassword = oldPassword;
//		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}

		@SafeHtml(whitelistType = WhiteListType.NONE)
		@NotBlank
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@SafeHtml(whitelistType = WhiteListType.NONE)
		public String getSurname() {
			return surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}

		@SafeHtml(whitelistType = WhiteListType.NONE)
		@URL
		public String getPicture() {
			return picture;
		}

		public void setPicture(String picture) {
			this.picture = picture;
		}

		@SafeHtml(whitelistType = WhiteListType.NONE)
		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		@SafeHtml(whitelistType = WhiteListType.NONE)
		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		@SafeHtml(whitelistType = WhiteListType.NONE)
		@NotBlank
		@Pattern(regexp = "^\\([A-Z]{2}\\)\\d{9}$")
		public String getVat() {
			return vat;
		}

		public void setVat(String vat) {
			this.vat = vat;
		}

		@SafeHtml(whitelistType = WhiteListType.NONE)
		@NotBlank
		@Pattern(regexp = "^([A-z0-9 ]+[ ]<[A-z0-9]+@(([A-z0-9]+\\.{0,1})+[A-z0-9]+){0,}>|[A-z0-9]+@(([A-z0-9]+\\.{0,1})+[A-z0-9]+){0,})$")
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		@NotBlank
		@CreditCardNumber
		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		@NotNull
		@Range(min = 1, max = 999)
		public Integer getCvv() {
			return cvv;
		}

		public void setCvv(Integer cvv) {
			this.cvv = cvv;
		}

		@NotBlank
		@SafeHtml
		public String getHolderName() {
			return holderName;
		}

		public void setHolderName(String holderName) {
			this.holderName = holderName;
		}

		@NotBlank
		@SafeHtml
		public String getMake() {
			return make;
		}

		public void setMake(String make) {
			this.make = make;
		}

		@NotNull
		@Range(min = 1, max = 12)
		public Integer getExpirationMonth() {
			return expirationMonth;
		}

		public void setExpirationMonth(Integer expirationMonth) {
			this.expirationMonth = expirationMonth;
		}

		@NotNull
		@Range(min = 18, max = 99)
		public Integer getExpirationYear() {
			return expirationYear;
		}

		public void setExpirationYear(Integer expirationYear) {
			this.expirationYear = expirationYear;
		}
	
}

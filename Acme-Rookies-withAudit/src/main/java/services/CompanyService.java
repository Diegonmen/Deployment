
package services;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CompanyRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Company;
import domain.CreditCard;
import domain.Position;
import domain.Problem;
import domain.SocialProfile;
import forms.CompanyForm;

@Service
@Transactional
public class CompanyService {

	@Autowired
	private CompanyRepository		companyRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private Validator				validator;

	@Autowired
	private CreditCardService		creditCardService;


	public Company findByPrincipal() {
		Company result;
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.companyRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	public Company save(Company company) {
		Company result, saved;
		final UserAccount logedUserAccount;
		Authority authority;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		authority.setAuthority("COMPANY");
		Assert.notNull(company, "company.not.null");
		Pattern pattern0 = Pattern.compile("^([A-z0-9 ]+[ ]<[A-z0-9]+@(([A-z0-9]+\\.)+[A-z0-9]+){0,}>|[A-z0-9]+@(([A-z0-9]+\\.)+[A-z0-9]+){0,})$");
		Assert.isTrue(pattern0.matcher(company.getEmail()).find());

		if (this.exists(company.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "company.notLogged ");
			int string1 = logedUserAccount.getId();
			int string2 = company.getUserAccount().getId();
			Assert.isTrue(string1 == string2, "company.notEqual.userAccount");

			saved = this.companyRepository.findOne(company.getId());
			Assert.notNull(saved, "company.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(company.getUserAccount().getUsername()), "company.notEqual.username");
		} else {
			company.getUserAccount().setPassword(encoder.encodePassword(company.getUserAccount().getPassword(), null));
			company.setIsBanned(false);
		}

		result = this.companyRepository.save(company);

		return result;

	}

	public Company create() {
		Company result;
		UserAccount userAccount;
		Authority authority;

		result = new Company();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setIsSpammer(false);

		authority.setAuthority("COMPANY");
		userAccount.addAuthority(authority);

		Collection<SocialProfile> socialProfiles = new LinkedList<>();
		result.setSocialProfiles(socialProfiles);
		Collection<Problem> problems = new LinkedList<>();
		result.setProblems(problems);
		Collection<Position> positions = new LinkedList<>();
		result.setPositions(positions);
		result.setUserAccount(userAccount);
		result.setIsBanned(false);
		result.setIsSpammer(false);
		CreditCard creditCard = new CreditCard();
		result.setCreditCard(creditCard);

		return result;

	}

	public Company findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);
		Company result;
		result = this.companyRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public CompanyForm construct(Company company) {
		CompanyForm result = new CompanyForm();
		result.setName(company.getName());
		result.setSurname(company.getSurname());
		result.setPicture(company.getPicture());
		result.setPhoneNumber(company.getPhoneNumber());
		result.setAddress(company.getAddress());
		result.setVat(company.getVat());
		result.setEmail(company.getEmail());
		result.setCommercialName(company.getCommercialName());

		result.setUsername(company.getUserAccount().getUsername());
		result.setId(company.getId());

		if (company.getId() > 0) {
			result.setNumber(company.getCreditCard().getNumber());
			result.setCvv(company.getCreditCard().getCvv());
			result.setHolderName(company.getCreditCard().getHolderName());
			result.setMake(company.getCreditCard().getMake());
			result.setExpirationMonth(company.getCreditCard().getExpirationMonth());
			result.setExpirationYear(company.getCreditCard().getExpirationYear());
		}

		return result;
	}

	public Company reconstruct(CompanyForm form, BindingResult binding) {
		Company result;

		if (form.getId() == 0)
			result = this.create();
		else
			result = (Company) this.actorService.findByActorId(form.getId());

		this.validator.validate(form, binding);
		result.setName(form.getName());
		result.setSurname(form.getSurname());
		result.setPicture(form.getPicture());
		result.setEmail(form.getEmail());
		result.setVat(form.getVat());
		result.setCommercialName(form.getCommercialName());

		CreditCard creditCard = result.getCreditCard();

		creditCard.setNumber(form.getNumber());
		creditCard.setCvv(form.getCvv());
		creditCard.setHolderName(form.getHolderName());
		creditCard.setMake(form.getMake());
		creditCard.setExpirationMonth(form.getExpirationMonth());
		creditCard.setExpirationYear(form.getExpirationYear());

		result.setCreditCard(this.creditCardService.save(creditCard));

		if (!StringUtils.isEmpty(form.getPhoneNumber())) {

			Pattern pattern = Pattern.compile("^\\d{4,}$", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(form.getPhoneNumber());

			if (matcher.matches())
				form.setPhoneNumber(this.configurationService.findAll().iterator().next().getCountryCode() + form.getPhoneNumber());
		}

		result.setPhoneNumber(form.getPhoneNumber());
		result.setAddress(form.getAddress());
		String s1 = form.getUsername();
		String s2 = result.getUserAccount().getUsername();

		if (!s1.equals(s2) || s2 == null)
			if (!this.userAccountRepository.findUserAccountsByUsername(form.getUsername()).isEmpty())
				binding.rejectValue("username", "company.validator.username", "This username already exists");
		

		if (form.getNewPassword().trim().length() < 5)
			binding.rejectValue("newPassword", "company.validator.password", "Size must be between 5 and 32 characters");

		if (form.getConfirmPassword().trim().length() < 5)
			binding.rejectValue("confirmPassword", "company.validator.password", "Size must be between 5 and 32 characters");

//		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();
		
		result.getUserAccount().setUsername(form.getUsername());
		result.getUserAccount().setPassword(form.getNewPassword());
		
		return result;

	}

	public Company verifyAndSave(CompanyForm form, BindingResult binding) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();

		String passwordConfirm = form.getNewPassword();
		if (form.getId() != 0) {
			UserAccount ua = this.actorService.findByPrincipal().getUserAccount();
			if (form.getNewPassword() == "" /*&& form.getOldPassword() == ""*/ && form.getConfirmPassword() == "") {
//				form.setOldPassword(ua.getPassword());
				form.setNewPassword(ua.getPassword());
				form.setConfirmPassword(ua.getPassword());
//				Assert.isTrue(form.getOldPassword().equals(ua.getPassword()), "error.password.incorrectOld");
			} /*else
				Assert.isTrue(encoder.encodePassword(form.getOldPassword(), null).equals(ua.getPassword()), "error.password.incorrectOld");*/
		}

		String s1 = form.getConfirmPassword();
		String s2 = form.getNewPassword();
		Assert.isTrue(s1.equals(s2), "error.password.notMatching");

		Company temp = this.reconstruct(form, binding);
		if (!passwordConfirm.equals("") && form.getId() > 0)
			temp.getUserAccount().setPassword(encoder.encodePassword(temp.getUserAccount().getPassword(), null));

		Company result = this.save(temp);
		return result;
	}

	public List<Company> findAll() {
		return this.companyRepository.findAll();
	}

	public Company findOne(Integer id) {
		return this.companyRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return this.companyRepository.exists(id);
	}

	public void delete(Integer id) {
		this.companyRepository.delete(id);
	}

	public Company findByPositionId(int positionId) {
		return this.companyRepository.findByPositionId(positionId);
	}

	public void updateAuditScore(int companyId, Double auditScore) {
		Company a = this.findOne(companyId);
		Assert.isTrue(a != null);
		Assert.isTrue(auditScore == null || (auditScore >= 0 && auditScore <= 1));
		a.setAuditScore(auditScore);
		this.companyRepository.save(a);
	}
}

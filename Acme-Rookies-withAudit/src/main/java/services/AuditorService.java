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

import domain.Auditor;
import domain.CreditCard;
import domain.SocialProfile;
import forms.AuditorForm;
import repositories.AuditorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;

@Service
@Transactional
public class AuditorService {

	@Autowired
	private AuditorRepository	auditorRepository;

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


	public Auditor save(Auditor auditor) {
		Auditor result, saved;
		final UserAccount logedUserAccount;
		Authority authority;
		Md5PasswordEncoder encoder;
		
		if(auditor.getId() == 0) {
			UserAccount adminUserAccount = LoginService.getPrincipal();
			Assert.isTrue(adminUserAccount.getAuthorities().iterator().next().getAuthority().equals("ADMINISTRATOR"));
		}

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		authority.setAuthority("AUDITOR");
		Assert.notNull(auditor, "auditor.not.null");
		Pattern pattern0 = Pattern.compile("^([A-z0-9 ]+[ ]<[A-z0-9]+@(([A-z0-9]+\\.)+[A-z0-9]+){0,}>|[A-z0-9]+@(([A-z0-9]+\\.)+[A-z0-9]+){0,})$");
		Assert.isTrue(pattern0.matcher(auditor.getEmail()).find());

		if (this.exists(auditor.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "auditor.notLogged ");
			int string1 = logedUserAccount.getId();
			int string2 = auditor.getUserAccount().getId();
			Assert.isTrue(string1 == string2, "auditor.notEqual.userAccount");

			saved = this.auditorRepository.findOne(auditor.getId());
			Assert.notNull(saved, "auditor.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(auditor.getUserAccount().getUsername()), "auditor.notEqual.username");
		} else {
			auditor.getUserAccount().setPassword(encoder.encodePassword(auditor.getUserAccount().getPassword(), null));
			auditor.setIsBanned(false);
		}

		result = this.auditorRepository.save(auditor);

		return result;
	}

	public Auditor create() {
		Auditor result;
		UserAccount userAccount;
		Authority authority;

		result = new Auditor();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setIsSpammer(false);

		authority.setAuthority("AUDITOR");
		userAccount.addAuthority(authority);

		Collection<SocialProfile> socialProfiles = new LinkedList<>();
		result.setSocialProfiles(socialProfiles);
		result.setUserAccount(userAccount);
		result.setIsBanned(false);
		result.setIsSpammer(false);
		CreditCard creditCard = new CreditCard();
		result.setCreditCard(creditCard);

		return result;

	}

	public Auditor findByPrincipal() {
		Auditor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Auditor findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);
		Auditor result;
		result = this.auditorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public AuditorForm construct(Auditor auditor) {
		AuditorForm result = new AuditorForm();
		result.setName(auditor.getName());
		result.setSurname(auditor.getSurname());
		result.setPicture(auditor.getPicture());
		result.setPhoneNumber(auditor.getPhoneNumber());
		result.setAddress(auditor.getAddress());
		result.setVat(auditor.getVat());
		result.setEmail(auditor.getEmail());

		result.setUsername(auditor.getUserAccount().getUsername());
		result.setId(auditor.getId());

		if (auditor.getId() > 0) {
			result.setNumber(auditor.getCreditCard().getNumber());
			result.setCvv(auditor.getCreditCard().getCvv());
			result.setHolderName(auditor.getCreditCard().getHolderName());
			result.setMake(auditor.getCreditCard().getMake());
			result.setExpirationMonth(auditor.getCreditCard().getExpirationMonth());
			result.setExpirationYear(auditor.getCreditCard().getExpirationYear());
		}

		return result;
	}

	public Auditor reconstruct(AuditorForm form, BindingResult binding) {
		Auditor result;

		if (form.getId() == 0)
			result = this.create();
		else
			result = (Auditor) this.actorService.findByActorId(form.getId());

		this.validator.validate(form, binding);
		result.setName(form.getName());
		result.setSurname(form.getSurname());
		result.setPicture(form.getPicture());
		result.setEmail(form.getEmail());
		result.setVat(form.getVat());

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
				binding.rejectValue("username", "auditor.validator.username", "This username already exists");
		

		if (form.getNewPassword().trim().length() < 5)
			binding.rejectValue("newPassword", "auditor.validator.password", "Size must be between 5 and 32 characters");

		if (form.getConfirmPassword().trim().length() < 5)
			binding.rejectValue("confirmPassword", "auditor.validator.password", "Size must be between 5 and 32 characters");

//		this.validator.validate(result, binding);
		
		if (binding.hasErrors())
			throw new ValidationException();

		result.getUserAccount().setUsername(form.getUsername());
		result.getUserAccount().setPassword(form.getNewPassword());
		
		return result;

	}

	public Auditor verifyAndSave(AuditorForm form, BindingResult binding) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();

		String passwordConfirm = form.getNewPassword();
		if (form.getId() != 0) {
			UserAccount ua = this.actorService.findByPrincipal().getUserAccount();
			if (form.getNewPassword() == "" /*&& form.getOldPassword() == "" */&& form.getConfirmPassword() == "") {
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

		Auditor temp = this.reconstruct(form, binding);
		if (!passwordConfirm.equals("") && form.getId() > 0)
			temp.getUserAccount().setPassword(encoder.encodePassword(temp.getUserAccount().getPassword(), null));

		Auditor result = this.save(temp);
		return result;
	}

	public List<Auditor> findAll() {
		return this.auditorRepository.findAll();
	}

	public Auditor findOne(Integer id) {
		return this.auditorRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return this.auditorRepository.exists(id);
	}

	public void delete(Integer id) {
		this.auditorRepository.delete(id);
	}
	
	public void flush() {
		auditorRepository.flush();
	}
	
}

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

import domain.CreditCard;
import domain.Finder;
import domain.Rookie;
import domain.SocialProfile;
import forms.RookieForm;
import repositories.RookieRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;

@Service
@Transactional
public class RookieService {
	
	@Autowired
	private RookieRepository	rookieRepository;
	
	@Autowired
	private ActorService actorService;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private Validator validator;

	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private FinderService finderService;

	public Rookie save(Rookie rookie) {
		Rookie result, saved;
		final UserAccount logedUserAccount;
		Authority authority;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		authority.setAuthority("ROOKIE");
		Assert.notNull(rookie, "company.not.null");
		Pattern pattern0 = Pattern.compile(
				"^([A-z0-9 ]+[ ]<[A-z0-9]+@(([A-z0-9]+\\.)+[A-z0-9]+){0,}>|[A-z0-9]+@(([A-z0-9]+\\.)+[A-z0-9]+){0,})$");
		Assert.isTrue(pattern0.matcher(rookie.getEmail()).find());

		if (rookie.getId()!=0) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "rookie.notLogged ");
			int string1 = logedUserAccount.getId();
			int string2 = rookie.getUserAccount().getId();
			Assert.isTrue(string1 == string2, "rookie.notEqual.userAccount");

			saved = this.rookieRepository.findOne(rookie.getId());
			Assert.notNull(saved, "rookie.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(rookie.getUserAccount().getUsername()),
					"rookie.notEqual.username");
		} else {
			Finder finder = this.finderService.create();
			Finder savedFinder = this.finderService.save(finder);
			rookie.setFinder(savedFinder);
			rookie.getUserAccount().setPassword(encoder.encodePassword(rookie.getUserAccount().getPassword(), null));
			rookie.setIsBanned(false);
		}

		result = this.rookieRepository.save(rookie);

		return result;
	}
	
	public Rookie create() {
		Rookie result;
		UserAccount userAccount;
		Authority authority;

		result = new Rookie();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setIsSpammer(false);

		authority.setAuthority("ROOKIE");
		userAccount.addAuthority(authority);

		Collection<SocialProfile> socialProfiles = new LinkedList<>();
		result.setSocialProfiles(socialProfiles);
		Collection<SocialProfile> applications = new LinkedList<>();
		result.setSocialProfiles(applications);
		Collection<SocialProfile> curricula = new LinkedList<>();
		result.setSocialProfiles(curricula);
		
		result.setUserAccount(userAccount);
		result.setIsBanned(false);
		result.setIsSpammer(false);
		CreditCard creditCard = new CreditCard();
		result.setCreditCard(creditCard);

		return result;

	}
	
	public RookieForm construct(Rookie rookie) {
		RookieForm result = new RookieForm();
		result.setName(rookie.getName());
		result.setSurname(rookie.getSurname());
		result.setPicture(rookie.getPicture());
		result.setPhoneNumber(rookie.getPhoneNumber());
		result.setAddress(rookie.getAddress());
		result.setVat(rookie.getVat());
		result.setEmail(rookie.getEmail());

		result.setUsername(rookie.getUserAccount().getUsername());
		result.setId(rookie.getId());

		if (rookie.getId() > 0) {
			result.setNumber(rookie.getCreditCard().getNumber());
			result.setCvv(rookie.getCreditCard().getCvv());
			result.setHolderName(rookie.getCreditCard().getHolderName());
			result.setMake(rookie.getCreditCard().getMake());
			result.setExpirationMonth(rookie.getCreditCard().getExpirationMonth());
			result.setExpirationYear(rookie.getCreditCard().getExpirationYear());
		}

		return result;
	}
	
	public Rookie reconstruct(RookieForm form, BindingResult binding) {
		Rookie result;
		
		if (form.getId() == 0)
			result = this.create();
		else
			result = (Rookie) this.actorService.findByActorId(form.getId());

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

//		this.validator.validate(creditCard, binding);
		result.setCreditCard(creditCardService.save(creditCard));

		if (!StringUtils.isEmpty(form.getPhoneNumber())) {

			Pattern pattern = Pattern.compile("^\\d{4,}$", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(form.getPhoneNumber());

			if (matcher.matches())
				form.setPhoneNumber(
						this.configurationService.findAll().iterator().next().getCountryCode() + form.getPhoneNumber());
		}

		result.setPhoneNumber(form.getPhoneNumber());
		result.setAddress(form.getAddress());
		String s1 = form.getUsername();
		String s2 = result.getUserAccount().getUsername();

		if (!s1.equals(s2) || s2 == null)
			if (!this.userAccountRepository.findUserAccountsByUsername(form.getUsername()).isEmpty())
				binding.rejectValue("username", "rookie.validator.username", "This username already exists");
	
//		result.getUserAccount().setUsername(form.getUsername());
//		result.getUserAccount().setPassword(form.getNewPassword());

		if (form.getNewPassword().trim().length() < 5)
			binding.rejectValue("newPassword", "rookie.validator.password",
					"Size must be between 5 and 32 characters");

		if (form.getConfirmPassword().trim().length() < 5)
			binding.rejectValue("confirmPassword", "rookie.validator.password",
					"Size must be between 5 and 32 characters");
//		if (form.getUsername().trim().length() < 5)
//			binding.rejectValue("username", "rookie.validator.password",
//					"Size must be between 5 and 32 characters");
		
//		this.validator.validate(result, binding);
		

		if (binding.hasErrors())
			throw new ValidationException();
		
		result.getUserAccount().setUsername(form.getUsername());
		result.getUserAccount().setPassword(form.getNewPassword());

		return result;

	}
	
	public Rookie verifyAndSave(RookieForm form, BindingResult binding) {
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
				Assert.isTrue(encoder.encodePassword(form.getOldPassword(), null).equals(ua.getPassword()),
						"error.password.incorrectOld");*/
		}

		String s1 = form.getConfirmPassword();
		String s2 = form.getNewPassword();
		Assert.isTrue(s1.equals(s2), "error.password.notMatching");

		Rookie temp = this.reconstruct(form, binding);
		if (!passwordConfirm.equals("") && form.getId() > 0)
			temp.getUserAccount().setPassword(encoder.encodePassword(temp.getUserAccount().getPassword(), null));

		Rookie result = this.save(temp);
		return result;
	}

	public List<Rookie> findAll() {
		return rookieRepository.findAll();
	}

	public Rookie findOne(Integer id) {
		return rookieRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return rookieRepository.exists(id);
	}

	public void delete(Integer id) {
		rookieRepository.delete(id);
	}
	
	public Rookie findByPrincipal() {
		Rookie result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}
	
	public Rookie findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);
		Rookie result = this.rookieRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public void flush() {
		rookieRepository.flush();
	}

	

}

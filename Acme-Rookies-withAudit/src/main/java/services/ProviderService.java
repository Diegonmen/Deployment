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
import domain.Item;
import domain.Provider;
import domain.SocialProfile;
import forms.ProviderForm;
import repositories.ProviderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;

@Service
@Transactional
public class ProviderService {

	@Autowired
	private ProviderRepository providerRepository;

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

	public Provider findByPrincipal() {
		Provider result;
		UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.providerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		return result;
	}

	public Provider save(Provider provider) {
		Provider result, saved;
		final UserAccount logedUserAccount;
		Authority authority;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		authority.setAuthority("PROVIDER");
		Assert.notNull(provider, "provider.not.null");
		Pattern pattern0 = Pattern.compile(
				"^([A-z0-9 ]+[ ]<[A-z0-9]+@(([A-z0-9]+\\.)+[A-z0-9]+){0,}>|[A-z0-9]+@(([A-z0-9]+\\.)+[A-z0-9]+){0,})$");
		Assert.isTrue(pattern0.matcher(provider.getEmail()).find());

		if (this.exists(provider.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "provider.notLogged ");
			int string1 = logedUserAccount.getId();
			int string2 = provider.getUserAccount().getId();
			Assert.isTrue(string1 == string2, "provider.notEqual.userAccount");

			saved = this.providerRepository.findOne(provider.getId());
			Assert.notNull(saved, "provider.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(provider.getUserAccount().getUsername()),
					"provider.notEqual.username");
		} else {
			provider.getUserAccount().setPassword(encoder.encodePassword(provider.getUserAccount().getPassword(), null));
			provider.setIsBanned(false);
		}

		result = this.providerRepository.save(provider);

		return result;

	}

	public Provider create() {
		Provider result;
		UserAccount userAccount;
		Authority authority;

		result = new Provider();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setIsSpammer(false);

		authority.setAuthority("PROVIDER");
		userAccount.addAuthority(authority);

		Collection<SocialProfile> socialProfiles = new LinkedList<>();
		result.setSocialProfiles(socialProfiles);
		Collection<Item> items = new LinkedList<>();
		result.setItems(items);
		result.setUserAccount(userAccount);
		result.setIsBanned(false);
		result.setIsSpammer(false);
		CreditCard creditCard = new CreditCard();
		result.setCreditCard(creditCard);

		return result;

	}

	public Provider findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);
		Provider result;
		result = this.providerRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public ProviderForm construct(Provider provider) {
		ProviderForm result = new ProviderForm();
		result.setName(provider.getName());
		result.setSurname(provider.getSurname());
		result.setPicture(provider.getPicture());
		result.setPhoneNumber(provider.getPhoneNumber());
		result.setAddress(provider.getAddress());
		result.setVat(provider.getVat());
		result.setEmail(provider.getEmail());
		result.setMakeProvider(provider.getMake());

		result.setUsername(provider.getUserAccount().getUsername());
		result.setId(provider.getId());

		if (provider.getId() > 0) {
			result.setNumber(provider.getCreditCard().getNumber());
			result.setCvv(provider.getCreditCard().getCvv());
			result.setHolderName(provider.getCreditCard().getHolderName());
			result.setMake(provider.getCreditCard().getMake());
			result.setExpirationMonth(provider.getCreditCard().getExpirationMonth());
			result.setExpirationYear(provider.getCreditCard().getExpirationYear());

		}

		return result;
	}

	public Provider reconstruct(ProviderForm form, BindingResult binding) {
		Provider result;

		if (form.getId() == 0)
			result = this.create();
		else
			result = (Provider) this.actorService.findByActorId(form.getId());

		this.validator.validate(form, binding);
		result.setName(form.getName());
		result.setSurname(form.getSurname());
		result.setPicture(form.getPicture());
		result.setEmail(form.getEmail());
		result.setVat(form.getVat());
		result.setMake(form.getMakeProvider());

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
				binding.rejectValue("username", "provider.validator.username", "This username already exists");
		

		if (form.getNewPassword().trim().length() < 5)
			binding.rejectValue("newPassword", "provider.validator.password", "Size must be between 5 and 32 characters");

		if (form.getConfirmPassword().trim().length() < 5)
			binding.rejectValue("confirmPassword", "provider.validator.password", "Size must be between 5 and 32 characters");

//		this.validator.validate(result, binding);
		
		if (binding.hasErrors())
			throw new ValidationException();

		result.getUserAccount().setUsername(form.getUsername());
		result.getUserAccount().setPassword(form.getNewPassword());
		
		return result;

	}

	public Provider verifyAndSave(ProviderForm form, BindingResult binding) {
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

		Provider temp = this.reconstruct(form, binding);
		if (!passwordConfirm.equals("") && form.getId() > 0)
			temp.getUserAccount().setPassword(encoder.encodePassword(temp.getUserAccount().getPassword(), null));

		Provider result = this.save(temp);
		return result;
	}

	public Provider findByItemId(int itemId) {

		Provider result;
		result = this.providerRepository.findByItemId(itemId);
		return result;
	}

	public List<Provider> findAll() {
		return providerRepository.findAll();
	}

	public Provider findOne(Integer id) {
		return providerRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return providerRepository.exists(id);
	}

	public void delete(Integer id) {
		providerRepository.delete(id);
	}

	public void flush() {
		providerRepository.flush();
	}

	public Provider getProviderBySponsorshipId(int id) {
		return providerRepository.getProviderBySponsorshipId(id);
	}
	
}


package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Administrator;
import domain.Company;
import domain.CreditCard;
import domain.Position;
import domain.Provider;
import domain.Rookie;
import domain.SocialProfile;
import forms.AdministratorForm;

@Service
@Transactional
public class AdministratorService {

	@Autowired
	private AdministratorRepository	administratorRepository;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AuditService			auditService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private UserAccountRepository	userAccountRepository;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private Validator				validator;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private MessageSource			messageSource;


	public Administrator save(Administrator administrator) {
		Administrator result, saved;
		final UserAccount logedUserAccount;
		Authority authority;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		authority = new Authority();
		authority.setAuthority("ADMINISTRATOR");
		Assert.notNull(administrator, "administrator.not.null");
		Pattern pattern0 = Pattern.compile("^([A-z0-9 ]+[ ]<[A-z0-9]+@(([A-z0-9]+\\.)+[A-z0-9]+){0,}>|[A-z0-9]+@(([A-z0-9]+\\.)+[A-z0-9]+){0,})$");
		Assert.isTrue(pattern0.matcher(administrator.getEmail()).find());

		if (this.exists(administrator.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "administrator.notLogged ");
			int string1 = logedUserAccount.getId();
			int string2 = administrator.getUserAccount().getId();
			Assert.isTrue(string1 == string2, "administrator.notEqual.userAccount");

			saved = this.administratorRepository.findOne(administrator.getId());
			Assert.notNull(saved, "administrator.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(administrator.getUserAccount().getUsername()), "administrator.notEqual.username");
		} else {
			administrator.getUserAccount().setPassword(encoder.encodePassword(administrator.getUserAccount().getPassword(), null));
			administrator.setIsBanned(false);
		}

		result = this.administratorRepository.save(administrator);

		return result;
	}

	public Administrator create() {
		Administrator result;
		UserAccount userAccount;
		Authority authority;

		result = new Administrator();
		userAccount = new UserAccount();
		authority = new Authority();

		result.setIsSpammer(false);

		authority.setAuthority("ADMINISTRATOR");
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

	public Administrator findByPrincipal() {
		Administrator result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Administrator findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);
		Administrator result;
		result = this.administratorRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public AdministratorForm construct(Administrator admin) {
		AdministratorForm result = new AdministratorForm();
		result.setName(admin.getName());
		result.setSurname(admin.getSurname());
		result.setPicture(admin.getPicture());
		result.setPhoneNumber(admin.getPhoneNumber());
		result.setAddress(admin.getAddress());
		result.setVat(admin.getVat());
		result.setEmail(admin.getEmail());

		result.setUsername(admin.getUserAccount().getUsername());
		result.setId(admin.getId());

		if (admin.getId() > 0) {
			result.setNumber(admin.getCreditCard().getNumber());
			result.setCvv(admin.getCreditCard().getCvv());
			result.setHolderName(admin.getCreditCard().getHolderName());
			result.setMake(admin.getCreditCard().getMake());
			result.setExpirationMonth(admin.getCreditCard().getExpirationMonth());
			result.setExpirationYear(admin.getCreditCard().getExpirationYear());
		}

		return result;
	}

	public Administrator reconstruct(AdministratorForm form, BindingResult binding) {
		Administrator result;

		if (form.getId() == 0)
			result = this.create();
		else
			result = (Administrator) this.actorService.findByActorId(form.getId());

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
				binding.rejectValue("username", "administrator.validator.username", "This username already exists");
		

		if (form.getNewPassword().trim().length() < 5)
			binding.rejectValue("newPassword", "administrator.validator.password", "Size must be between 5 and 32 characters");

		if (form.getConfirmPassword().trim().length() < 5)
			binding.rejectValue("confirmPassword", "administrator.validator.password", "Size must be between 5 and 32 characters");

//		this.validator.validate(result, binding);
		
		if (binding.hasErrors())
			throw new ValidationException();

		result.getUserAccount().setUsername(form.getUsername());
		result.getUserAccount().setPassword(form.getNewPassword());
		
		return result;

	}

	public Administrator verifyAndSave(AdministratorForm form, BindingResult binding) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();

		String passwordConfirm = form.getNewPassword();
		if (form.getId() != 0) {
			UserAccount ua = this.actorService.findByPrincipal().getUserAccount();
			if (form.getNewPassword() == ""/* && form.getOldPassword() == ""*/ && form.getConfirmPassword() == "") {
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

		Administrator temp = this.reconstruct(form, binding);
		if (!passwordConfirm.equals("") && form.getId() > 0)
			temp.getUserAccount().setPassword(encoder.encodePassword(temp.getUserAccount().getPassword(), null));

		Administrator result = this.save(temp);
		return result;
	}

	public List<Administrator> findAll() {
		return this.administratorRepository.findAll();
	}

	public Administrator findOne(Integer id) {
		return this.administratorRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return this.administratorRepository.exists(id);
	}

	public void delete(Integer id) {
		this.administratorRepository.delete(id);
	}

	public Double[] findAvgMinMaxStdevPositionsPerCompany() {
		return this.administratorRepository.findAvgMinMaxStdevPositionsPerCompany();
	}

	public Double[] findAvgMinMaxStdevApplicationsPerRookie() {
		return this.administratorRepository.findAvgMinMaxStdevApplicationsPerRookie();
	}

	public List<Company> findCompaniesMorePositions() {
		return this.administratorRepository.findCompaniesMorePositions();
	}

	public List<Rookie> findRookiesMoreApplications() {
		return this.administratorRepository.findRookiesMoreApplications();
	}

	public Double[] findAvgMinMaxStdevSalaries() {
		return this.administratorRepository.findAvgMinMaxStdevSalaries();
	}

	public List<Position> selectPositionsGreatestSalary() {
		return this.administratorRepository.selectPositionsGreatestSalary();
	}

	public List<Position> selectPositionsLowestSalary() {
		return this.administratorRepository.selectPositionsLowestSalary();
	}

	public Double[] findAvgMinMaxStdevCurriculaPerRookie() {
		return this.administratorRepository.findAvgMinMaxStdevCurriculaPerRookie();
	}

	public Double[] findAvgMinMaxStdevResultsPerFinder() {
		return this.administratorRepository.findAvgMinMaxStdevResultsPerFinder();
	}

	public String findRatioFinders(Locale locale) {
		String res = "";
		Double d1 = this.administratorRepository.findCountEmptyFinders();
		Double d2 = this.administratorRepository.findCountFinders();
		Double ans = d1 / (d2 - d1);
		res = ans.toString();
		if (ans.isInfinite() && locale != null)
			res = this.messageSource.getMessage("dashboard.infinity", null, locale);
		else if (ans.isInfinite())
			res = "Infinity";
		return res;
	}

	public Double[] findAvgMinMaxStdevAuditScorePerPosition() {
		return this.administratorRepository.findAvgMinMaxStdevScoreForPositions();
	}

	public Double[] findAvgMinMaxStdevScorePerCompany() {
		return this.administratorRepository.findAvgMinMaxStdevAuditScoreForCompanies();
	}

	public List<Company> findCompaniesHighestScore() {
		return this.administratorRepository.selectCompaniesGreatestAuditScore();
	}

	public Double averageSalaryForHighestScorePositions() {
		return this.administratorRepository.getAvgSalaryForBestRatedPositions();
	}

	public Double[] findAvgMinMaxStdevItemsPerProvider() {
		return this.administratorRepository.avgMinMaxStdevItemsPerProvider();
	}

	public List<Provider> findTop5ProvidersByItemCount() {
		return this.administratorRepository.top5ProvidersByItems(new PageRequest(0, 5)).getContent();
	}

	public Double[] findAvgMinMaxStdevSponsorshipsPerProvider() {
		return this.administratorRepository.findAvgMinMaxStdevSponsorshipsPerProvider();
	}

	public Double[] findAvgMinMaxStdevSponsorshipsPerPosition() {
		return this.administratorRepository.findAvgMinMaxStdevSponsorshipsPerPosition();
	}

	public List<Provider> findProvidersWith10PercentMoreSponsorships() {
		return this.administratorRepository.getProvidersMoreSponsorships();
	}

	public void announceRebranding() {
		if (!this.configurationService.findConfiguration().getUsersHaveBeenNotified()) {
			this.messageService.sendNotification("We have changed our name! - ¡Nos hemos cambiado el nombre!",
				"In our effort to offer our great services to even more demographics, we have changed our name to Acme-Rookies to expand to the US market, but we still offer the services you know and love. Cheers!\n"
					+ "En nuestro empeño por llevar nuestros servicios a nuevos mercados nos hemos cambiado el nombre a Acme-Rookies, pero ¡aún ofrecemos los servicios que conoces! ¡Un saludo!", this.actorService.findAll());
			this.configurationService.triggerUsersNotifiedRebranding();
		}
	}

	public void computeAuditorScore() {
		List<Company> companies = this.companyService.findAll();
		Map<Company, Double> all = new HashMap<Company, Double>();
		double max = 0, min = Double.MAX_VALUE;
		for (Company a : companies) {
			Double val = this.auditService.findAverageAuditScoreForCompanyId(a.getId());
			all.put(a, val);
			if (val != null) {
				if (val > max)
					max = val;
				if (val < min && val != 0)
					min = val;
			}
		}
		for (Company a : companies)
			if (all.get(a) == null || all.get(a) == 0)
				this.companyService.updateAuditScore(a.getId(), null);
			else {
				Double value = ((all.get(a) - min)) / (max - min);
				if (Double.isInfinite(value) || Double.isNaN(value))
					value = null;
				this.companyService.updateAuditScore(a.getId(), value);
			}
	}
}

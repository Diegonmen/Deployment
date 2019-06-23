
package services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.validation.ValidationException;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NyordelRepository;
import domain.Audit;
import domain.Company;
import domain.Nyordel;
import domain.Position;

@Service
@Transactional
public class NyordelService {

	@Autowired
	private NyordelRepository	nyordelRepository;

	@Autowired
	private AuditService		auditService;

	@Autowired
	private CompanyService		companyService;

	@Autowired
	private Validator			validator;


	public void delete(int id) {
		this.nyordelRepository.delete(id);
	}

	public List<Nyordel> findAll() {
		return this.nyordelRepository.findAll();
	}

	public List<Nyordel> findAllMines() {
		Company company = this.companyService.findByPrincipal();
		return this.nyordelRepository.findAllByCompanyId(company.getId());
	}

	public List<Nyordel> findAllByPosition(int positionId) {
		return this.nyordelRepository.findAllByAuditId(positionId);
	}

	public List<Nyordel> findAllByAuditInFinal(int auditId) {
		return this.nyordelRepository.findAllByAuditIdInFinal(auditId);
	}

	public Nyordel findOneWithSameTicker(String ticker) {
		return this.nyordelRepository.findOneWithSameTicker(ticker);
	}

	public Nyordel findOne(final Integer id) {
		return this.nyordelRepository.findOne(id);
	}

	public Nyordel create(int auditId) {

		final Company company = this.companyService.findByPrincipal();
		Audit aud = this.auditService.findOne(auditId);
		Assert.isTrue(company.getPositions().contains(aud.getPosition()));

		Nyordel result = new Nyordel();

		result.setDraftMode(true);
		result.setAudit(aud);
		result.setTicker(this.tickerGenerator());

		return result;
	}

	public Nyordel save(final Nyordel entity) {

		Date date = new Date(System.currentTimeMillis() - 1);

		if (!entity.isDraftMode())
			entity.setMoment(date);

		Nyordel saved = this.nyordelRepository.save(entity);

		return saved;
	}

	public Nyordel reconstruct(final Nyordel nyordel, final BindingResult binding, int positionId) {
		Nyordel result;

		boolean val = false;
		for (Position p : this.companyService.findByPrincipal().getPositions())
			val = val || p.getAudits().contains(nyordel.getAudit());

		Assert.isTrue(val);

		if (nyordel.getId() == 0)
			result = this.create(positionId);
		else
			result = this.nyordelRepository.findOne(nyordel.getId());

		//		if(!nyordel.isDraftMode())
		//			result.setMoment(new Date(System.currentTimeMillis() - 1));

		result.setBody(nyordel.getBody());
		result.setPicture(nyordel.getPicture());
		result.setDraftMode(nyordel.isDraftMode());

		this.validator.validate(result, binding);
		if (binding.hasErrors())
			throw new ValidationException();

		return result;
	}

	public String tickerGenerator() {
		String str = null;
		Calendar date;
		String year, month, day, alphaNum, todayDate;
		boolean unique = false;

		date = Calendar.getInstance();
		date.setTime(LocalDate.now().toDate());
		year = String.valueOf(date.get(Calendar.YEAR));
		year = year.substring(year.length() - 2, year.length());
		month = String.valueOf(date.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1)
			day = "0" + day;
		while (unique == false) {
			alphaNum = this.generateAlphanumeric();
			todayDate = year + month + day;
			str = todayDate + "-" + alphaNum;

			if (this.findOneWithSameTicker(str) == null)
				unique = true;
		}

		return str;
	}

	public String generateAlphanumeric() {
		final Character[] letras = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
		};
		final Random rand = new Random();
		String alpha = "";
		for (int i = 0; i < 5; i++)
			alpha += letras[rand.nextInt(letras.length - 1)];

		return alpha;
	}

	public Nyordel findOneToEdit(int nyordelId) {
		Nyordel n = this.findOne(nyordelId);
		boolean val = false;
		for (Position p : this.companyService.findByPrincipal().getPositions())
			val = val || p.getAudits().contains(n.getAudit());

		Assert.isTrue(val);
		Assert.isTrue(n.isDraftMode());
		return n;
	}
	public Company findByNyordel(Nyordel n) {
		return this.nyordelRepository.findCompanyByNyordelId(n.getId());
	}

}

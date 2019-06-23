
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.Actor;
import domain.CreditCard;
import domain.Position;
import domain.Provider;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	@Autowired
	private ProviderService			providerService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private Validator				validator;


	public Sponsorship save(Sponsorship entity, Position position) {
		
		if (entity.getId() == 0) {
			Assert.isTrue(!position.isDraftMode() && !position.isCancelled());
			CreditCard creditCard = providerService.findByPrincipal().getCreditCard();
			entity.setCreditCard(creditCard);
		}
		
		Sponsorship result = this.sponsorshipRepository.save(entity);
		
		if (entity.getId() == 0) {
			position.getSponsorships().add(result);
			Provider loggedProvider = this.providerService.findByPrincipal();
			loggedProvider.getSponsorships().add(result);
		}
		return result;
	}

	public List<domain.Sponsorship> findAll() {
		return this.sponsorshipRepository.findAll();
	}

	public Sponsorship findOne(Integer id) {
		return this.sponsorshipRepository.findOne(id);
	}

	public Sponsorship findOneToEdit(Integer id) {
		Sponsorship sponsorship = this.sponsorshipRepository.findOne(id);
		Assert.isTrue(providerService.findByPrincipal().getSponsorships().contains(sponsorship), "This is not your sponsorship!");
		return sponsorship;
	}

	public boolean exists(Integer id) {
		return this.sponsorshipRepository.exists(id);
	}

	public Sponsorship create() {
		Sponsorship result = new Sponsorship();
		return result;
	}
	
	public void delete(Integer id) {
		Sponsorship sponsorship = findOneToEdit(id);
		providerService.findByPrincipal().getSponsorships().remove(sponsorship);
		sponsorshipRepository.getPositionBySponsorshipId(id).getSponsorships().remove(sponsorship);
		sponsorshipRepository.delete(id);
	}

	public Sponsorship reconstruct(final Sponsorship sponsorship, final BindingResult binding) {
		
		Sponsorship result;

		if (sponsorship.getId() == 0) {
			result = this.create();
		} else {
			result = this.findOneToEdit(sponsorship.getId());
		}

		result.setBanner(sponsorship.getBanner());
		result.setUrl(sponsorship.getUrl());
		

		this.validator.validate(result, binding);

		if (binding.hasErrors())
			throw new ValidationException();
		
		CreditCard creditCard = providerService.findByPrincipal().getCreditCard();
		Calendar cal = Calendar.getInstance();
		if ((creditCard.getExpirationYear() < cal.get(Calendar.YEAR) % 100) || (creditCard.getExpirationYear() == cal.get(Calendar.YEAR) % 100 && creditCard.getExpirationMonth() < cal.get(Calendar.MONTH) + 1))
			throw new IllegalArgumentException("sponsorship.expired.card");

		return result;
	}

	public Sponsorship getRandomSponsorshipByPositionId(int id) {
		Collection<Sponsorship> sponsorships = this.sponsorshipRepository.getActiveSponsorshipsByPositionId(id);
		ArrayList<Sponsorship> sponsorshipList = new ArrayList<>(sponsorships);
		Sponsorship result = null;
		if (sponsorshipList.size() > 0) {
			result = sponsorshipList.get(ThreadLocalRandom.current().nextInt(sponsorshipList.size()));
			Actor recipient = this.providerService.getProviderBySponsorshipId(result.getId());
			String startingNumber = result.getCreditCard().getNumber().substring(0, 4);
			double fare = this.configurationService.findConfiguration().getSponsorshipFare();
			double vat = this.configurationService.findConfiguration().getVat();
			this.messageService.sendNotification("Your credit card has been charged - Se ha hecho un cargo en tu tarjeta de crédito", "Your credit card starting with " + startingNumber + " has been charged " + fare
				+ " euros (+ " + fare * vat + " VAT) because your sponsorship " + result.getUrl() + " has been shown in a position.\n\n---\n\nSe han cargado " + fare + " euros (+ " + fare * vat + " IVA) en tu tarjeta de crédito que comienza por "
				+ startingNumber + " porque tu patrocinio " + result.getUrl() + " ha sido mostrado en una posición.", recipient);
			this.sponsorshipRepository.flush();
		}

		return result;
	}

}

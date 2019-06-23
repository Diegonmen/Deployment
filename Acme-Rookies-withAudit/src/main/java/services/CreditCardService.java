package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.CreditCard;
import repositories.CreditCardRepository;

@Service
@Transactional
public class CreditCardService {

	@Autowired
	private CreditCardRepository	creditCardRepository;

	public CreditCard save(CreditCard entity) {
		return creditCardRepository.save(entity);
	}

	public List<CreditCard> findAll() {
		return creditCardRepository.findAll();
	}

	public CreditCard findOne(Integer id) {
		return creditCardRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return creditCardRepository.exists(id);
	}

	public void delete(Integer id) {
		creditCardRepository.delete(id);
	}
	
}

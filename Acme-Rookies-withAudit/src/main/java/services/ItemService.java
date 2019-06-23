package services;

import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ItemRepository;
import domain.Item;
import domain.Provider;

@Service
@Transactional
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ProviderService providerService;

	@Autowired
	private Validator validator;


	public List<Item> findAll() {
		return itemRepository.findAll();
	}

	public Item findOne(Integer id) {
		return itemRepository.findOne(id);
	}

	public boolean exists(Integer id) {
		return itemRepository.exists(id);
	}

	public void delete(Integer id){

		Provider creator = this.providerService.findByPrincipal();
		Item item = this.findOne(id);

		creator.getItems().remove(item);
		this.providerService.save(creator);
		//TODO: double check assert principal
		this.itemRepository.delete(id);
	}

	public Item create() {
		final Item res = new Item();
		return res;
	}


	public Item save(final Item entity) {
		Item saved;
		final Provider creator = this.providerService.findByPrincipal();

		saved=this.itemRepository.save(entity);

		if(entity.getId()==0)
			creator.getItems().add(saved);

		return saved;
	}

	public Item reconstruct(final Item item, final BindingResult binding) {
		Item result;

		if (item.getId() == 0) {
			result = this.create();
		} else {
			result = this.itemRepository.findOne(item.getId());

		}
		result.setName(item.getName());
		result.setDescription(item.getDescription());
		result.setLinks(item.getLinks());
		result.setPictures(item.getPictures());

		this.validator.validate(result, binding);

		if(binding.hasErrors())
			throw new ValidationException();


		return result;
	}

	public Item findOneToEdit(int itemId) {
		final Provider creator = this.providerService.findByPrincipal();
		Item mine = this.findOne(itemId);
		Assert.isTrue(creator.getItems().contains(mine));
		return mine;
	}

}

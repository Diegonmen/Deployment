package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.ItemRepository;
import utilities.AbstractTest;
import domain.Item;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ItemServiceTest extends AbstractTest {

	//Test coverage: 100%  // Covered instructions: 335 // Missed instructions: 0 // Total Instructions: 335
	//ItemService coverage: 60.9%  // Covered instructions: 78 // Missed instructions: 50 // Total Instructions: 128

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemRepository itemRepository;

	@Test
	public void createItemDriver() {
		Object testingData[][] = {

			{
				"provider0", "un nombre", "una descripcion", "https://d500.epimg.net/cincodias/imagenes/2018/11/13/lifestyle/1542113135_776401_1542116070_noticia_normal.jpg", "https://www.google.es", null
			},				// Positive test case: create a item
			{
				"provider1", "", "descrip", "http://www.google.es", "", ConstraintViolationException.class
			},				// Negative test case: Create a item with empty title	
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.createItemTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], 
					(String) testingData[i][3], (String) testingData[i][4],(Class<?>) testingData[i][5]);
	}


	public void createItemTemplate(String user, String name, String description, String picture, String link, Class<?> expected) {

		Class<?> caught = null;

		try {
			itemService.findAll();
			Collection<String> links = new ArrayList<>();
			Collection<String> pictures = new ArrayList<>();
			links.add(link);
			pictures.add(picture);
			//TODO: version and ID edit
			
			this.authenticate(user);
			Item item = this.itemService.create();
			Assert.isTrue(item.getId()==0);
			item.setName(name);
			item.setDescription(description);
			item.setPictures(pictures);
			item.setLinks(links);
			
			Item saved = this.itemService.save(item);
			
			Assert.isTrue(saved.getId()!=0&&saved.getVersion()==0);
			this.itemRepository.flush();
			
			Assert.notNull(saved);
			
			this.unauthenticate();
			
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	@Test
	public void deleteItemDriver() {
		Object testingData[][] = {

			{
				"provider0", "item0", null
				//Positive test case: delete a item
			},	
			{
				"provider1", "item0", IllegalArgumentException.class
			},	// Negative test case: delete an item which is not mine

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteItemTemplate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]),(Class<?>) testingData[i][2]);
	}
	
	public void deleteItemTemplate(String username, int itemId, Class<?> expected) {

		Class<?> caught = null;

		try {
			this.authenticate(username);
			Item item = itemService.findOneToEdit(itemId);
			this.itemService.delete(item.getId());
			itemRepository.flush();
			Assert.isNull(itemService.findOne(item.getId()));
			this.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}
	



}

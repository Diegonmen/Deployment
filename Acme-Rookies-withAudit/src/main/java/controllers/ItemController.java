package controllers;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ItemService;
import services.ProviderService;
import domain.Item;
import domain.Provider;

@Controller
@RequestMapping("/item")
public class ItemController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ItemService		itemService;
	@Autowired
	private ProviderService		providerService;

	


	// Constructors -----------------------------------------------------------

	public ItemController() {
		super();
	}
	
	//List
		@RequestMapping(value = "list", method = RequestMethod.GET)
		public ModelAndView list(@RequestParam (required = false) final Integer providerId) {
			ModelAndView result;
			Provider provider = new Provider();
			Collection<Item> items ;
			boolean mines = false;
			
			if (providerId == null) {
				items = itemService.findAll() ;
			} else  {
				provider = providerService.findOne(providerId);
				items = provider.getItems();
			}
			
			result = new ModelAndView("item/list");

			result.addObject("requestURI", "item/list.do");
			result.addObject("items", items);
			result.addObject("mines", mines);
			System.out.println("///");

			return result;
		}
		
		@RequestMapping(value = "provider/list", method = RequestMethod.GET)
		public ModelAndView list() {
			ModelAndView result;
			Provider provider = providerService.findByPrincipal();
			Collection<Item> items = provider.getItems();
			boolean mines = true;
			
			result = new ModelAndView("item/list");

			result.addObject("requestURI", "item/provider/list.do");
			result.addObject("items", items);
			result.addObject("mines", mines);
			
			System.out.println("no///");

			return result;
		}

		
		//Show
		@RequestMapping("/show")
		public ModelAndView display(@RequestParam final int itemId) {
			ModelAndView result;
			result = new ModelAndView("item/show");

			Item item = this.itemService.findOne(itemId);
			
			result.addObject("item", item);

			result.addObject("requestURI", "item/show.do?itemId=" + itemId);

			return result;
		}
		
		@RequestMapping(value = "/provider/create", method = RequestMethod.GET)
		public ModelAndView create() {
			ModelAndView result;
			Item item;

			item = this.itemService.create();
			result = this.createEditModelAndView(item);
			return result;
		}

		@RequestMapping(value = "/provider/edit", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam final int itemId) {
			ModelAndView result;
			Item item;

			item = this.itemService.findOneToEdit(itemId);
			Assert.notNull(item);
			result = this.createEditModelAndView(item);

			return result;
		}
		
		@RequestMapping(value = "/provider/edit", method = RequestMethod.POST, params = "save")
		public ModelAndView save(Item item, final BindingResult binding) {
			ModelAndView result;

			try {
				item = this.itemService.reconstruct(item, binding);
				this.itemService.save(item);
				result = new ModelAndView("redirect:/item/provider/list.do");

			} catch (ValidationException oops) {
				result = this.createEditModelAndView(item);
			} catch (Throwable oops) {
				result = this.createEditModelAndView(item, "commit.error");
			}

			return result;
		}
		
		@RequestMapping(value = "/provider/edit", method = RequestMethod.POST, params = "delete")
		public ModelAndView delete(final Item item, final BindingResult binding) {
			ModelAndView result;

			try {
				this.itemService.delete(item.getId());
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(item, "problem.commit.error");
			}
			return result;
		}
		
		//Ancilliary method
		protected ModelAndView createEditModelAndView(final Item item) {
			ModelAndView result;
			result = this.createEditModelAndView(item, null);
			return result;
		}

		protected ModelAndView createEditModelAndView(final Item item, final String messageCode) {
			ModelAndView result;

			if (item.getId() > 0)
				result = new ModelAndView("item/edit");
			else
				result = new ModelAndView("item/create");

			result.addObject("item", item);
			result.addObject("message", messageCode);

			return result;
		}
}

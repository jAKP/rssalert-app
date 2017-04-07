package org.itemlist.web;

import java.util.List;

import org.itemlist.domain.Item;
import org.itemlist.service.dao.ItemDao;
import org.itemlist.web.exceptions.ItemDeleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for handling Employees.
 */
@Controller
@RequestMapping("/items")
public class ManageListController {

	private ItemDao itemDao;

	@Autowired
	public void setEmployeeDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public ItemDao getItemDao() {
		return itemDao;
	}

	/**
	 * Retrieves employees, puts them in the model and returns corresponding
	 * view
	 * 
	 * @param model
	 *            Model to put employees to
	 * @return employees/list
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showItems(Model model) {
		List<Item> items = itemDao.list();
		model.addAttribute("items", items);

		return "items/list";
	}

	/**
	 * Deletes employee with specified ID
	 * 
	 * @param id
	 *            Employee's ID
	 * @return redirects to employees if everything was ok
	 * @throws EmployeeDeleteException
	 *             When employee cannot be deleted
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteItem(@PathVariable("id") int id) throws ItemDeleteException {

		Item toDelete = itemDao.find(id);
		boolean wasDeleted = itemDao.removeItem(toDelete);

		if (!wasDeleted) {
			throw new ItemDeleteException(toDelete);
		}

		// everything OK, see remaining employees
		return "redirect:/items";
	}

	/**
	 * Handles EmployeeDeleteException
	 * 
	 * @param e
	 *            Thrown exception with employee that couldn't be deleted
	 * @return binds employee to model and returns employees/delete-error
	 */
	@ExceptionHandler(ItemDeleteException.class)
	public ModelAndView handleDeleteException(ItemDeleteException e) {
		ModelMap model = new ModelMap();
		model.put("item", e.getItem());
		return new ModelAndView("items/delete-error", model);
	}

	/**
	 * Returns employee with specified ID
	 * 
	 * @param id
	 *            Employee's ID
	 * @param model
	 *            Model to put employee to
	 * @return employees/view
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getItem(@PathVariable("id") int id, Model model) {
		Item item = itemDao.find(id);
		model.addAttribute("item", item);

		return "items/view";
	}

	/**
	 * Updates employee with specified ID
	 * 
	 * @param id
	 *            Employee's ID
	 * @param employee
	 *            Employee to update (bounded from HTML form)
	 * @return redirects to employees
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String updateItem(@PathVariable("id") int id, Item item) {
		item.setItemId(id);
		itemDao.update(item);

		return "redirect:/items";
	}

	/**
	 * Creates form for new employee
	 * 
	 * @param model
	 *            Model to bind to HTML form
	 * @return employees/new
	 */
	@RequestMapping(params = "new", method = RequestMethod.GET)
	public String createItemForm(Model model) {
		model.addAttribute("item", new Item());
		return "items/new";
	}

	/**
	 * Saves new employee to the database
	 * 
	 * @param employee
	 *            Employee to save
	 * @return redirects to employees
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String addItem(Item item) {
		itemDao.add(item);

		return "redirect:/items";
	}

}
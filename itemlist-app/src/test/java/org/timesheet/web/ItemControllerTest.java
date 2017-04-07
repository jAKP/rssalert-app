package org.timesheet.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.itemlist.DomainAwareBase;
import org.itemlist.domain.Item;
import org.itemlist.domain.Picture;
import org.itemlist.service.dao.ItemDao;
import org.itemlist.web.ManageListController;
import org.itemlist.web.exceptions.ItemDeleteException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@ContextConfiguration(locations = { "/persistence-beans.xml", "/controllers.xml" })
public class ItemControllerTest extends DomainAwareBase {

	@Autowired
	private ItemDao itemDao;

	@Autowired
	private ManageListController controller;

	private Model model; // used for controller

	@Before
	public void setUp() {
		model = new ExtendedModelMap();
	}

	@After
	public void cleanUp() {
		List<Item> items = itemDao.list();
		for (Item Item : items) {
			itemDao.remove(Item);
		}
	}

	@Test
	public void testShowEmployees() {
		// prepare some data
		Item Item = new Item("Cushions", "Kwantum", new ArrayList<Picture>(), 1);
		itemDao.add(Item);

		// use controller
		String view = controller.showItems(model);
		assertEquals("items/list", view);

		List<Item> listFromDao = itemDao.list();
		Collection<?> listFromModel = (Collection<?>) model.asMap().get("items");

		assertTrue(listFromModel.contains(Item));
		assertTrue(listFromDao.containsAll(listFromModel));
	}

	@Test
	public void testDeleteEmployeeOk() throws ItemDeleteException {
		// prepare ID to delete
		Item item = new Item("Cushions", "Kwantum", new ArrayList<Picture>(), 1);
		itemDao.add(item);
		int id = item.getItemId();

		// delete & assert
		String view = controller.deleteItem(id);
		assertEquals("redirect:/items", view);
		assertNull(itemDao.find(id));
	}

	@Test(expected = ItemDeleteException.class)
	public void testDeleteEmployeeThrowsException() throws ItemDeleteException {
		// prepare ID to delete
		Item item = new Item("Cushions", "Kwantum", new ArrayList<Picture>(), 1);
		itemDao.add(item);
		int id = item.getItemId();

		// mock DAO for this call
		ItemDao mockedDao = mock(ItemDao.class);
		when(mockedDao.removeItem(item)).thenReturn(false);

		ItemDao originalDao = controller.getItemDao();
		try {
			// delete & expect exception
			controller.setEmployeeDao(mockedDao);
			controller.deleteItem(id);
		} finally {
			controller.setEmployeeDao(originalDao);
		}
	}

	@Test
	public void testHandleDeleteException() {
		Item item = new Item("Cushions", "Kwantum", new ArrayList<Picture>(), 1);
		ItemDeleteException e = new ItemDeleteException(item);
		ModelAndView modelAndView = controller.handleDeleteException(e);

		assertEquals("items/delete-error", modelAndView.getViewName());
		assertTrue(modelAndView.getModelMap().containsValue(item));
	}

	@Test
	public void testGetItem() {
		// prepare Item
		Item george = new Item("Cushions", "Kwantum", new ArrayList<Picture>(), 1);
		itemDao.add(george);
		int id = george.getItemId();

		// get & assert
		String view = controller.getItem(id, model);
		assertEquals("items/view", view);
		assertEquals(george, model.asMap().get("Item"));
	}

	@Test
	public void testUpdateItem() {
		// prepare Item
		Item item = new Item("Cushions", "Kwantum", new ArrayList<Picture>(), 1);
		itemDao.add(item);
		int id = item.getItemId();

		// user alters Item in HTML form
		item.setItemDescription("Drums");

		// update & assert
		String view = controller.updateItem(id, item);
		assertEquals("redirect:/items", view);
		assertEquals("Drums", itemDao.find(id).getItemDescription());
	}

	@Test
	public void testAddItem() {
		// prepare Item
		Item item = new Item("Cushions", "Kwantum", new ArrayList<Picture>(), 1);

		// save but via controller
		String view = controller.addItem(item);
		assertEquals("redirect:/items", view);

		// Item is stored in DB
		assertEquals(item, itemDao.find(item.getItemId()));
	}
}
package org.timesheet.service.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.itemlist.DomainAwareBase;
import org.itemlist.domain.Item;
import org.itemlist.domain.Picture;
import org.itemlist.service.dao.ItemDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "/persistence-beans.xml")
public class ItemDaoTest extends DomainAwareBase {

	@Autowired
	private ItemDao itemDao;

	@Test
	public void testAdd() {
		int size = itemDao.list().size();
		itemDao.add(new Item("chair", "ikea", new ArrayList<Picture>(), 1));

		assertTrue(size < itemDao.list().size());
	}

	@Test
	public void testUpdate() {
		Item item = new Item("chair", "ikea", new ArrayList<Picture>(), 1);
		itemDao.add(item);
		item.setItemName("updated");

		itemDao.update(item);
		Item found = itemDao.find(item.getItemId());
		assertEquals("updated", found.getItemName());
	}

	@Test
	public void testFind() {
		Item item = new Item("chair", "ikea", new ArrayList<Picture>(), 1);
		itemDao.add(item);

		Item found = itemDao.find(item.getItemId());
		assertEquals(found, item);
	}

	@Test
	public void testList() {
		assertEquals(0, itemDao.list().size());

		List<Item> items = Arrays.asList(new Item("chair", "ikea", new ArrayList<Picture>(), 1),
				new Item("chair1", "ikea1", new ArrayList<Picture>(), 2),
				new Item("chair2", "ikea2", new ArrayList<Picture>(), 3));
		for (Item item : items) {
			itemDao.add(item);
		}

		List<Item> found = itemDao.list();
		assertEquals(3, found.size());
		for (Item item : found) {
			assertTrue(items.contains(item));
		}
	}

	@Test
	public void testRemove() {
		Item item = new Item("chair", "ikea", new ArrayList<Picture>(), 1);
		itemDao.add(item);

		// successfully added
		assertEquals(item, itemDao.find(item.getItemId()));

		// try to remove
		itemDao.remove(item);
		assertNull(itemDao.find(item.getItemId()));
	}

}
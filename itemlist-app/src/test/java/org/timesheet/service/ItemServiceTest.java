package org.timesheet.service;

import java.util.ArrayList;

import org.itemlist.domain.Item;
import org.itemlist.domain.Picture;
import org.itemlist.service.GenericDao;
import org.itemlist.service.impl.InMemoryDao;
import org.junit.Before;

public class ItemServiceTest {

	private GenericDao<Item, Integer> itemDao = new InMemoryDao<Item, Integer>();

	@Before
	public void setUp() {
		for (int i = 0; i < 5; i++) {
			Item e = new Item("Chair " + i, "Ikea", new ArrayList<Picture>(), 1);
			itemDao.add(e);
		}
	}

	//
	// @Test
	// public void testAdd() {
	// int oldSize = employeeDao.list().size();
	// Employee e = new Employee("Bob", "IT");
	// employeeDao.add(e);
	// int newSize = employeeDao.list().size();
	//
	// assertFalse (oldSize == newSize);
	// }
	//
	// @Test
	// public void testRemove() {
	// int oldSize = employeeDao.list().size();
	// Employee e = employeeDao.find(1L);
	// employeeDao.remove(e);
	// int newSize = employeeDao.list().size();
	//
	// assertFalse (oldSize == newSize);
	// }
	//
	// @Test
	// public void testUpdate() {
	// //TODO: need real implementation
	// }
	//
	// @Test
	// public void testList() {
	// List<Employee> list = employeeDao.list();
	// assertNotNull (list);
	// assertFalse (list.isEmpty());
	// }

}
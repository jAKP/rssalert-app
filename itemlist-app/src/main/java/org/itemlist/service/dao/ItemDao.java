package org.itemlist.service.dao;

import java.util.List;

import org.itemlist.domain.Item;
import org.itemlist.service.GenericDao;

public interface ItemDao extends GenericDao<Item, Integer> {

	void add(Item item);

	Item get(Item item);

	boolean removeItem(Item item);
	
	List<Item> list();

}
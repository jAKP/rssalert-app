package org.itemlist.web.editors;

import java.beans.PropertyEditorSupport;

import org.itemlist.domain.Item;
import org.itemlist.service.dao.ItemDao;

/**
 * Will convert ID from combobox to employee's instance.
 */
public class ItemEditor extends PropertyEditorSupport {

	private ItemDao itemDao;

	public ItemEditor(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		int id = Integer.parseInt(text);
		Item item = itemDao.find(id);
		setValue(item);
	}
}
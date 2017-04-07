package org.itemlist.web.exceptions;

import org.itemlist.domain.Item;

/**
 * When employee cannot be deleted.
 */
public class ItemDeleteException extends Exception {

	private static final long serialVersionUID = 7692857849554286468L;

	private Item item;

	public ItemDeleteException(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}
}

package org.itemlist.service.impl;

import java.util.List;

import org.hibernate.Query;
import org.itemlist.domain.Item;
import org.itemlist.service.dao.ItemDao;
import org.springframework.stereotype.Repository;

@Repository("itemDao")
public class ItemDaoImpl extends HibernateDao<Item, Integer> implements ItemDao {

	@Override
	public boolean removeItem(Item item) {
		Query employeeTaskQuery = currentSession().createQuery("from Item t where :id in elements(t.assignedEmployees)");
		employeeTaskQuery.setParameter("id", item.getItemId());
		if (!employeeTaskQuery.list().isEmpty()) {
			return false;
		}
		Query employeeTimesheetQuery = currentSession().createQuery("from Timesheet t where t.who.id = :id");
		employeeTimesheetQuery.setParameter("id", item.getItemId());
		if (!employeeTimesheetQuery.list().isEmpty()) {
			return false;
		}
		removeItem(item);
		return true;
	}

	@Override
	public Item get(Item item) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> list() {
		List<Item> list = currentSession().createCriteria(Item.class).list();
		return list;
	}

}
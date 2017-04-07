package org.itemlist.web;

import java.util.List;

import org.itemlist.domain.Item;
import org.itemlist.service.dao.ItemDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/welcome")
public class WelcomeController {
	Logger logger = LoggerFactory.getLogger(WelcomeController.class);
//
//	@Autowired
//	private EntityGenerator entityGenerator;

	@Autowired
	private ItemDao itemDao;

	@RequestMapping(method = RequestMethod.GET)
	public String showMenu(Model model) {
		List<Item> items = itemDao.list();
		model.addAttribute("itemsList", items);
		return "index";
	}

//	@PostConstruct
//	public void prepareFakeDomain() {
//		entityGenerator.deleteDomain();
//		entityGenerator.generateDomain();
//	}

	
	public ItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

}
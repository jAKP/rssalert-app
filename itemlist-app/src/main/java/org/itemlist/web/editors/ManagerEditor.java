package org.itemlist.web.editors;
//package org.itemlist.web.editors;
//
//import java.beans.PropertyEditorSupport;
//
//import org.itemlist.domain.Manager;
//import org.itemlist.service.dao.ManagerDao;
//
//public class ManagerEditor extends PropertyEditorSupport {
//	 
//    private ManagerDao managerDao;
// 
//    public ManagerEditor(ManagerDao managerDao) {
//        this.managerDao = managerDao;
//    }
// 
//    @Override
//    public void setAsText(String text) throws IllegalArgumentException {
//        long id = Long.parseLong(text);
//        Manager manager = managerDao.find(id);
//        setValue(manager);
//    }
//}

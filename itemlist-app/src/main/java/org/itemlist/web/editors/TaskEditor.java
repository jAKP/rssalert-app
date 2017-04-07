package org.itemlist.web.editors;
//package org.itemlist.web.editors;
//
//import org.itemlist.domain.Task;
//import org.itemlist.service.dao.TaskDao;
// 
//import java.beans.PropertyEditorSupport;
// 
//public class TaskEditor extends PropertyEditorSupport {
// 
//    private TaskDao taskDao;
// 
//    public TaskEditor(TaskDao taskDao) {
//        this.taskDao = taskDao;
//    }
// 
//    @Override
//    public void setAsText(String text) throws IllegalArgumentException {
//        long id = Long.parseLong(text);
//        Task task = taskDao.find(id);
//        setValue(task);
//    }
//}
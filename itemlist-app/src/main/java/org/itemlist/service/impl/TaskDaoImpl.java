package org.itemlist.service.impl;
//package org.itemlist.service.impl;
//
//import java.util.List;
//
//import org.hibernate.Criteria;
//import org.hibernate.Query;
//import org.springframework.stereotype.Repository;
//import org.itemlist.domain.Task;
//import org.itemlist.service.dao.TaskDao;
// 
//@Repository("taskDao")
//public class TaskDaoImpl extends HibernateDao<Task, Long> implements TaskDao {
// 
//    @Override
//    public boolean removeTask(Task task) {
//        Query taskQuery = currentSession().createQuery("from Timesheet t where t.task.id = :id");
//        taskQuery.setParameter("id", task.getId());
// 
//        // task mustn't be assigned to no timesheet
//        if (!taskQuery.list().isEmpty()) {
//            return false;
//        }
// 
//        // ok, remove as usual
//        remove(task);
//        return true;
//    }
// 
//    @Override
//    public List<Task> list() {
//        return currentSession().createCriteria(Task.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
//    }
//}

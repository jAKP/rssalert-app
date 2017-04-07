package org.itemlist.service.impl;
//package org.itemlist.service.impl;
//
//import org.hibernate.Query;
//import org.springframework.stereotype.Repository;
//import org.itemlist.domain.Employee;
//import org.itemlist.service.dao.EmployeeDao;
//
//@Repository("employeeDao")
//public class EmployeeDaoImpl extends HibernateDao<Employee, Long> implements EmployeeDao {
//
//   @Override
//   public boolean removeEmployee(Employee employee) {
//       Query employeeTaskQuery = currentSession().createQuery(
//               "from Task t where :id in elements(t.assignedEmployees)");
//       employeeTaskQuery.setParameter("id", employee.getId());
//
//       // employee mustn't be assigned on no task
//       if (!employeeTaskQuery.list().isEmpty()) {
//           return false;
//       }
//
//       Query employeeTimesheetQuery = currentSession().createQuery(
//               "from Timesheet t where t.who.id = :id");
//       employeeTimesheetQuery.setParameter("id", employee.getId());
//
//       // employee mustn't be assigned to any timesheet
//       if (!employeeTimesheetQuery.list().isEmpty()) {
//           return false;
//       }
//
//       // ok, remove as usualsppande
//       remove(employee);
//       return true;
//
//   }
//}
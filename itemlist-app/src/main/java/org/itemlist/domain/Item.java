package org.itemlist.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ITEM_ID")
	private Integer itemId;

	@Column(name = "ITEM_NAME")
	private String itemName;

	@Column(name = "ITEM_DESC")
	private String itemDescription;

	@Column(name = "ITEM_RANK")
	private Integer itemRank;

	@OneToMany(targetEntity = Picture.class, mappedBy = "item", fetch = FetchType.EAGER)
	private List<Picture> itemPictures;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public List<Picture> getItemPictures() {
		return itemPictures;
	}

	public void setItemPictures(List<Picture> itemPictures) {
		this.itemPictures = itemPictures;
	}

	public Item() {
	}

	public Item(String itemName, String itemDescription, List<Picture> arrayList, Integer itemRank) {
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.itemPictures = arrayList;
		this.itemRank = itemRank;
	}

	public Integer getItemRank() {
		return itemRank;
	}

	public void setItemRank(Integer itemRank) {
		this.itemRank = itemRank;
	}

	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// private Long id;
	//
	// @ManyToMany(fetch = FetchType.EAGER)
	// @JoinTable(name = "task_employee",
	// joinColumns = {@JoinColumn(name = "task_id")},
	// inverseJoinColumns = {@JoinColumn(name = "employee_id")}
	// )
	// private List<Employee> assignedEmployees = new ArrayList<Employee>();
	//
	// @OneToOne
	// @JoinColumn(name = "manager_id")
	// private Manager manager;
	//
	// private String description;
	// boolean completed;
	//
	// public Item() {
	// }
	//
	// public Item(String description, Manager manager, Employee... employees) {
	// this.description = description;
	// this.manager = manager;
	// assignedEmployees.addAll(Arrays.asList(employees));
	// completed = false;
	// }
	//
	// public Manager getManager() {
	// return manager;
	// }
	//
	// public List<Employee> getAssignedEmployees() {
	// return assignedEmployees;
	// }
	//
	// public void addEmployee(Employee e) {
	// assignedEmployees.add(e);
	// }
	//
	// public void removeEmployee(Employee e) {
	// assignedEmployees.remove(e);
	// }
	//
	// public Long getId() {
	// return id;
	// }
	//
	// public void setId(Long id) {
	// this.id = id;
	// }
	//
	// public boolean isCompleted() {
	// return completed;
	// }
	//
	// public void setCompleted(boolean completed) {
	// this.completed = completed;
	// }
	//
	// public void setAssignedEmployees(List<Employee> assignedEmployees) {
	// this.assignedEmployees = assignedEmployees;
	// }
	//
	// public void setManager(Manager manager) {
	// this.manager = manager;
	// }
	//
	// public String getDescription() {
	// return description;
	// }
	//
	// public void setDescription(String description) {
	// this.description = description;
	// }
	//
	// @Override
	// public boolean equals(Object o) {
	// if (this == o) {
	// return true;
	// }
	// if (o == null || getClass() != o.getClass()) {
	// return false;
	// }
	//
	// Item task = (Item) o;
	//
	// if (completed != task.completed) {
	// return false;
	// }
	// if (description != null ? !description.equals(task.description) :
	// task.description != null) {
	// return false;
	// }
	// if (id != null ? !id.equals(task.id) : task.id != null) {
	// return false;
	// }
	// if (manager != null ? !manager.equals(task.manager) : task.manager !=
	// null) {
	// return false;
	// }
	//
	// return true;
	// }
	//
	// @Override
	// public int hashCode() {
	// int result = id != null ? id.hashCode() : 0;
	// result = 31 * result + (manager != null ? manager.hashCode() : 0);
	// result = 31 * result + (description != null ? description.hashCode() :
	// 0);
	// result = 31 * result + (completed ? 1 : 0);
	// return result;
	// }
	//
	// @Override
	// public String toString() {
	// return "Task{" +
	// "id=" + id +
	// ", assignedEmployees=" + assignedEmployees +
	// ", manager=" + manager +
	// ", description='" + description + '\'' +
	// ", completed=" + completed +
	// '}';
	// }
}
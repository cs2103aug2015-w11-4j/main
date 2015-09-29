package objects;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

	// ============ VARIABLES START ================ //
	private int taskID;
	private Date startDate;
	private Date endDate;
	private String taskName;
	private Boolean floating;
	private Boolean completed;
	// ============ VARIABLES END ================ //
	
	
	// ============ GETTER/SETTERS START ================ //
	public int getTaskID() {
		return taskID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Boolean getFloating() {
		return floating;
	}
	public void setFloating(Boolean floating) {
		this.floating = floating;
	}
	public Boolean getCompleted() {
		return completed;
	}
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	// ============ GETTERS/SETTERS END ================ //
	
	// ============ CONSTRUCTOR START ================ //
	
	public Task() {
		super();
	}
	
	public Task(int taskID, Date startDate, Date endDate, String taskName, Boolean floating, Boolean completed) {
		super();
		this.taskID = taskID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.taskName = taskName;
		this.floating = floating;
		this.completed = completed;
	}
	
	// ============ CONSTRUCTOR END ================ //
	
	
}

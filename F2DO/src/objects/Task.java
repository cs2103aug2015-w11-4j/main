package objects;

import java.io.Serializable;
import java.util.Date;
import type.TaskType;
import java.util.Comparator;

@SuppressWarnings({ "serial"})
public class Task implements Serializable {


	// ============ VARIABLES START ================ //
	private int taskID = -1;
	private Date startDate = null;
	private Date endDate = null;
	private String taskName = "";
	private Boolean isFloating = null;
	private Boolean isCompleted = null;
	private int priority = -1;
	private TaskType taskType = null;
	private String cat[];
	
	// ============ VARIABLES END ================ //
	
	// Testing 2
	
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
		return isFloating;
	}
	public void setFloating(Boolean floating) {
		this.isFloating = floating;
	}
	public Boolean getCompleted() {
		return isCompleted;
	}
	public void setCompleted(Boolean completed) {
		this.isCompleted = completed;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public TaskType getTaskType() {
		return taskType;
	}
	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
	public String[] getCat() {
		return cat;
	}
	public void setCat(String cat[]) {
		this.cat = cat;
	}
	
	// ============ GETTERS/SETTERS END ================ //
	
	// ============ CONSTRUCTOR START ================ //
	
	public Task() {
		super();
	}
	
	public Task(int taskID, String taskName, Date startDate, Date endDate) {
		super();
		this.taskID = taskID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.taskName = taskName;
	}
	
	// Floating task
	public Task(int taskID, String taskName, int priority){
		this.taskID = taskID;
		this.taskName = taskName;
		this.priority = priority;
	}
	
	// DeadLine Task
	public Task(int taskID, String taskName, Date deadLine, int priority) {
		this.taskID = taskID;
		this.taskName = taskName;
		this.endDate = deadLine;
		this.priority = priority;
	}
	
	// Event Task
	public Task(int taskID, String taskName, Date startDate, Date endDate, int priority) {
		this.taskID = taskID;
		this.taskName = taskName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
	}

	public Task(String taskName, Date startDate, Date endDate, int priority) {
		this.taskName = taskName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
	}
	
	// ============ CONSTRUCTOR END ================ //

	/**
	 * Comparator
	 * @return the compared value
	 */
	public static Comparator<Task> taskComparator = new Comparator<Task>() {

		@Override
		public int compare(Task task1, Task task2) {
			int returnVal = 1;

			// sort task date in ascending order
			Date task1Date = task1.getStartDate();
			Date task2Date = task2.getStartDate();

			if (task1.isCompleted == task2.isCompleted) {
				if (task1Date.equals(task2Date)) {
					// sort priority in descending order
					// (high > medium > low)
				} else {
					returnVal = task1Date.compareTo(task2Date);
				}

			} else {
				if (task1.isCompleted) {
					returnVal = 1;
				} else {
					returnVal = -1;
				}

			}
			return returnVal;
		}
	};

	
	
	
}

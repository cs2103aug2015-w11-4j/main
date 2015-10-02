package objects;


import java.io.Serializable;
import java.util.Date;
import java.util.Comparator;

@SuppressWarnings({ "serial"})
public class Task implements Serializable {


	// ============ VARIABLES START ================ //
	private int taskID;
	private Date startDate;
	private Date endDate;
	private String taskName;
	private Boolean isFloating;
	private Boolean isCompleted;
	private int priority;
	
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
		this.isFloating = floating;
		this.isCompleted = completed;
	}
	
	// Floating task
	public Task(String taskName, int priority){
		this.taskName = taskName;
		this.priority = priority;
	}
	
	// DeadLine Task
	public Task(String taskName, Date deadLine, int priority) {
		this.taskName = taskName;
		this.endDate = deadLine;
		this.priority = priority;
	}
	
	// Event Task
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

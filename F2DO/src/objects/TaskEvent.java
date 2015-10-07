package objects;

import java.util.Date;

@SuppressWarnings("serial")
public class TaskEvent extends Task {

	/**
	 * Constructor to create the Event task object
	 * @param 
	 * @param 
	 */
	public TaskEvent(int taskID, String title, Date startDate, Date endDate, int i) {
		super(taskID, title, startDate, endDate, i);
	}
	
	/**
	 * Generate the Event task to be displayed
	 * @return String format of the Event task
	 */
	@Override
	public String toString() {
		return "Event Task [getTaskId() = " + getTaskID() + ", getTaskName() = "
				+ getTaskName() + ", getStartDate() = " + getStartDate() + 
				", getEndDate() " + getEndDate() + ", getPriority() = " + getPriority() + "]";
	}

}

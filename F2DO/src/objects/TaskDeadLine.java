package objects;

import java.util.Date;

@SuppressWarnings("serial")
public class TaskDeadLine extends Task {

	/**
	 * Constructor to create the Floating task object
	 * @param taskName
	 * @param priority
	 */
	public TaskDeadLine(String taskName, Date deadLine , int priority){
		super(taskName, deadLine, priority);
	}
	
	/**
	 * Generate the DeadLine task to be displayed
	 * @return String format of the Floating task
	 */
	@Override
	public String toString() {
		return "DeadLine Task [getTaskId() = " + getTaskID() + ", getTaskName() = "
				+ getTaskName() + ", getPriority() = " + getPriority() + "]";
	}
	
	
}

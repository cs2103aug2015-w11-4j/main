package objects;

@SuppressWarnings("serial")
public class TaskFloating extends Task {

	/**
	 * Constructor to create the Floating task object
	 * @param taskName
	 * @param priority
	 */
	public TaskFloating(int taskID, String taskName, int priority){
		super(taskID, taskName, priority);
	}
	
	/**
	 * Generate the Floating task to be displayed
	 * @return String format of the Floating task
	 */
	@Override
	public String toString() {
		return "FloatingTask [getTaskId() = " + getTaskID() + ", getTaskName() = "
				+ getTaskName() + ", getPriority() = " + getPriority() + "]";
	}
	
}

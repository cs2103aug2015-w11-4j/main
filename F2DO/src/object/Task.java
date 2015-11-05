//@@author Yu Ting
package object;

import java.util.ArrayList;
import java.util.Date;

import type.TaskType;

/**
 * 
 * @author 
 *
 */
public class Task {
	private int _taskID = -1;
	private String _taskName = null;
	private Date _startDate = null;
	private Date _endDate = null;
	private TaskType _taskType = null;
	private boolean _isCompleted = false;
	private int _priority = -1;
	private ArrayList<Category> _categories;
	
	/**
	 * Default constructor.
	 */
	public Task() {	}
	
	/**
	 * Constructor for floating task.
	 * @param taskID
	 * @param taskName
	 * @param priority
	 */
	public Task(int taskID, String taskName, TaskType type, int priority){
		_taskID = taskID;
		_taskName = taskName;
		_taskType = type;
		_priority = priority;
	}

	/**
	 * Constructor for deadline task.
	 * @param taskID
	 * @param taskName
	 * @param deadline
	 * @param priority
	 */
	public Task(int taskID, String taskName, TaskType type, 
			Date deadline, int priority) {
		this(taskID, taskName, type, priority);
		_endDate = deadline;
	}

	/**
	 * Constructor for event task.
	 * @param taskID
	 * @param taskName
	 * @param startDate
	 * @param endDate
	 * @param priority
	 */
	public Task(int taskID, String taskName, TaskType type, 
			Date startDate, Date endDate, int priority) {
		this(taskID, taskName, type, priority);
		_startDate = startDate;
		_endDate = endDate;
	}
	
	/**
	 * Constructor for result from parser.
	 * @param result
	 */
	public Task(Result result) {
		this (result.getStorageID(), result.getContent(), result.getType(),
				result.getStartDate(), result.getEndDate(), result.getPriority());
	}
	
	/**
	 * Get task ID.
	 * @return task ID
	 */
	public int getTaskID() {
		return _taskID;
	}
	
	/**
	 * Set task ID.
	 * @param taskID
	 */
	public void setTaskID(int taskID) {
		_taskID = taskID;
	}
	
	/**
	 * Get task name.
	 * @return
	 */
	public String getTaskName() {
		return _taskName;
	}
	
	/**
	 * Set task name.
	 * @param taskName
	 */
	public void setTaskName(String taskName) {
		_taskName = taskName;
	}
	
	/**
	 * Get start date and time.
	 * @return start date and time
	 */
	public Date getStartDate() {
		return _startDate;
	}
	
	/**
	 * Set start date and time.
	 * @param startDate - start date and time
	 */
	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}
	
	/**
	 * Get end date and time.
	 * @return end date and time
	 */
	public Date getEndDate() {
		return _endDate;
	}
	
	/**
	 * Set end date and time.
	 * @param endDate - end date and time
	 */
	public void setEndDate(Date endDate) {
		_endDate = endDate;
	}
	
	/**
	 * Get task type.
	 * @return task type
	 */
	public TaskType getTaskType() {
		return _taskType;
	}
	
	/**
	 * Set task type.
	 * @param taskType
	 */
	public void setTaskType(TaskType taskType) {
		_taskType = taskType;
	}

	/**
	 * Get if the task is completed.
	 * @return true if the task is completed; false otherwise
	 */
	public Boolean getCompleted() {
		return _isCompleted;
	}
	
	/**
	 * Set if the task is completed.
	 * @param completed - true if the task is completed; false otherwise
	 */
	public void setCompleted(Boolean completed) {
		_isCompleted = completed;
	}
	
	/**
	 * Get priority of the task.
	 * @return priority
	 */
	public int getPriority() {
		return _priority;
	}
	
	/**
	 * Set priority of the task.
	 * @param priority
	 */
	public void setPriority(int priority) {
		_priority = priority;
	}

	/**
	 * Get categories.
	 * @return categories
	 */
	public ArrayList<Category> getCategory() {
		return _categories;
	}
	
	/**
	 * Set categories
	 * @param categories
	 */
	public void setCategory(ArrayList<Category> categories) {
		_categories = categories;
	}
}

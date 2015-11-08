package object;

import java.util.Date;

import type.TaskType;

//@@author A0111758E
public class Task {
	private int _taskID = -1;
	private String _taskName = null;
	private Date _startDate = null;
	private Date _endDate = null;
	private TaskType _taskType = null;
	private boolean _isCompleted = false;
	
	/**
	 * Default constructor.
	 */
	public Task() {	}
	
	/**
	 * Constructor of floating task.
	 * @param taskID
	 * @param taskName
	 * @param type
	 */
	public Task(int taskID, String taskName, TaskType type){
		_taskID = taskID;
		_taskName = taskName;
		_taskType = type;
	}

	/**
	 * Constructor for deadline task.
	 * @param taskID
	 * @param taskName
	 * @param type
	 * @param deadline
	 */
	public Task(int taskID, String taskName, TaskType type, 
			Date deadline) {
		this(taskID, taskName, type);
		_endDate = deadline;
	}

	/**
	 * Constructor for event task.
	 * @param taskID
	 * @param taskName
	 * @param type
	 * @param startDate
	 * @param endDate
	 */
	public Task(int taskID, String taskName, TaskType type, 
			Date startDate, Date endDate) {
		this(taskID, taskName, type);
		_startDate = startDate;
		_endDate = endDate;
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
}

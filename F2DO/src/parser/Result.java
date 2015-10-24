package parser;

import java.util.Date;

import type.CommandType;
import type.TaskType;

public class Result {
	private int _displayID = -1;	// remove display ID
	private int _storageID = -1;
	private int _priority = -1;
	private CommandType _cmd = null;
	private TaskType _type = null;
	private String _title = "";
	private Date _startDate = null;
	private Date _endDate = null;
	
	public Result(){
		super();
	}
	
	public Result(int displayID, int storageID, CommandType cmd, String title, TaskType type, Date startDate, Date endDate) {
		_displayID = displayID;
		_storageID = storageID;
		_cmd = cmd;
		_title = title;
		_type = type;
		_startDate = startDate;
		_endDate = endDate;
	}
	
	public Result(String title, Date startDate, Date endDate) {
		_title = title;
		_startDate = startDate;
		_endDate = endDate;
	}
	
	public Result(int displayID, int storageID) {
		_displayID = displayID;
		_storageID = storageID;
	}
	
	/**
	 * Get display ID.
	 * @return display ID
	 */
	public int getDisplayID() {
		return _displayID;
	}
	
	/**
	 * Set storage ID.
	 * @return storage ID
	 */
	public int getStorageID() {
		return _storageID;
	}
	
	/**
	 * Get command.
	 * @return command
	 */
	public CommandType getCmd() {
		return _cmd;
	}
	
	/**
	 * Get title of the event, deadline or floating task.
	 * @return title
	 */
	public String getTitle() {
		return _title;
	}
	
	/**
	 * Get the type whether it is event, deadline or floating task.
	 * @return type 
	 */
	public TaskType getType() {
		return _type;
	}
	
	/**
	 * Get the start date and time of the event or deadline.
	 * @return startDate
	 */
	public Date getStartDate() {
		return _startDate;
	}
	
	/**
	 * Get the end date and time of the event or deadline.
	 * @return endDate
	 */
	public Date getEndDate() {
		return _endDate;
	}
	
	/**
	 * Set the start date and time of the event or deadline.
	 */
	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}
	
	/**
	 * Set the start date and time of the event or deadline.
	 */
	public void setEndDate(Date endDate) {
		_endDate = endDate;
	}
	
	/**
	 * Get priority of the task.
	 * @return priority
	 */
	public int getPriority() {
		return _priority;
	}
}

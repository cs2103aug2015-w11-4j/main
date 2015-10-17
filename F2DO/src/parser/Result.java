package parser;

import java.util.Date;

import type.CommandType;
import type.TaskType;

public class Result {
	private int _displayID = -1;
	private int _storageID = -1;
	private CommandType _cmd = null;
	private TaskType _type = null;
	private String _title = "";
	private Date _startDate = null;
	private Date _endDate = null;
	
	public Result(){
		super();
	}
	
	public Result(int displayID, int storageID, CommandType cmd, String title, TaskType type, Date startDate, Date endDate) {
		this._displayID = displayID;
		this._storageID = storageID;
		this._cmd = cmd;
		this._title = title;
		this._type = type;
		this._startDate = startDate;
		this._endDate = endDate;
	}
	
	public Result(String title, Date startDate, Date endDate) {
		this._title = title;
		this._startDate = startDate;
		this._endDate = endDate;
	}
	
	public Result(int displayID, int storageID) {
		this._displayID = displayID;
		this._storageID = storageID;
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
	 * set the start date and time of the event or deadline.
	 */
	public void setStartDate(Date startDate) {
		this._startDate = startDate;
	}
	
	/**
	 * set the start date and time of the event or deadline.
	 */
	public void setEndDate(Date endDate) {
		this._endDate = endDate;
	}
	
}

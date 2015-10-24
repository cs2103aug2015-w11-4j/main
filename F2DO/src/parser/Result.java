package parser;

import java.util.Date;

import type.CommandType;
import type.TaskType;

public class Result {
	private int _storageID = -1;
	private int _priority = -1;
	private CommandType _cmd = null;
	private TaskType _type = null;
	private String _content = null;
	private Date _startDate = null;
	private Date _endDate = null;
	
	public Result(){
		super();
	}
	
	public Result(int storageID, CommandType cmd, String content, TaskType type, Date startDate, Date endDate) {
		this(content, startDate, endDate);
		_storageID = storageID;
		_cmd = cmd;
		_type = type;
	}
	
	public Result(String content, Date startDate, Date endDate) {
		_content = content;
		_startDate = startDate;
		_endDate = endDate;
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
	public String getContent() {
		return _content;
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

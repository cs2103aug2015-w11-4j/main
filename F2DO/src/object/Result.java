package object;

import java.util.Date;

import type.CommandType;
import type.TaskType;

//@@author A0118005W
public class Result {
	private int _storageID = -1;
	private CommandType _cmd = null;
	private TaskType _type = null;
	private String _content = null;
	private Date _startDate = null;
	private Date _endDate = null;
	
	private boolean _isError = false;
	private String _errorMsg = null;
	
	public Result() {
		
	}
	
	public Result(int storageID, CommandType cmd, String content, 
			TaskType type, Date startDate, Date endDate) {
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
	
	public Result(CommandType cmd, Task task) {
		this(task.getTaskID(), cmd, task.getTaskName(),
				task.getTaskType(), task.getStartDate(), task.getEndDate());
	}
	
	/**
	 * Get storage ID.
	 * @return storage ID
	 */
	public int getStorageID() {
		return _storageID;
	}
	
	public void setStorageID(int ID) {
		_storageID = ID;
	}
	
	/**
	 * Get command.
	 * @return command
	 */
	public CommandType getCommand() {
		return _cmd;
	}
	
	public void setCommand(CommandType cmd) {
		_cmd = cmd;
	}
	
	/**
	 * Get content.
	 * @return title
	 */
	public String getContent() {
		return _content;
	}
	
	/**
	 * Set content. 
	 * @param content
	 */
	public void setContent(String content) {
		_content = content;
	}
	
	/**
	 * Get the type whether it is event, deadline or floating task.
	 * @return type 
	 */
	public TaskType getType() {
		return _type;
	}
	
	public void setType(TaskType taskType) {
		_type = taskType;
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
	 * Set error message
	 * @param msg - error message
	 */
	public void setErrorMsg(String msg) {
		_isError = true;
		_errorMsg = msg;
	}
	
	/**
	 * Check if there is any parsing error.
	 * @return true if there is any error; false otherwise
	 */
	public boolean isError() {
		return _isError;
	}
	
	/**
	 * Get error message.
	 * @return error message
	 */
	public String getErrorMsg() {
		return _errorMsg;
	}
}

package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Task;

/**
 * Feedback class stores the feedback details after the execution of 
 * the command.
 * @author 
 *
 */
public class Feedback {
	private String _message = null;
	private boolean _isSuccessful = false;
	private static ArrayList<Task> _displayList = 
			new ArrayList<Task>();
	private static ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public Feedback(String message, 
			ArrayList<Task> displayList,
			ConcurrentSkipListMap<Integer, Task> updatedList,
			boolean isSuccessful) {
		_message = message;
		_displayList = displayList;
		_taskList = updatedList;
		_isSuccessful = isSuccessful;
	}
	
	public String getMessage() {
		return _message;
	}
	
	public ArrayList<Task> getDisplayList() {
		return _displayList;
	}
	
	public ConcurrentSkipListMap<Integer, Task> getUpdatedTaskList() {
		return _taskList;
	}
	
	public boolean isSuccessful() {
		return _isSuccessful;
	}
}

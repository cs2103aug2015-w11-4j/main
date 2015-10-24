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
	private static ArrayList<Task> _displayList = 
			new ArrayList<Task>();
	private static ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public Feedback(String message, 
			ArrayList<Task> displayList,
			ConcurrentSkipListMap<Integer, Task> updatedList) {
		_message = message;
		_displayList = displayList;
		_taskList = updatedList;
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
}

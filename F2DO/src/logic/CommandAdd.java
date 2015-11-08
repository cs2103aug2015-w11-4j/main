package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;
import type.TaskType;

//@@author A0111758E
public class CommandAdd implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandAdd(Result result, ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	/**
	 * Add task.
	 */
	public Feedback execute() {
		Task task;
		TaskType type = _result.getType();
		int taskID = -1;
		boolean isSuccessful = false;
		String message = String.format(FeedbackHelper.ERROR_ADD, _result.getContent());
		
		if (_taskList.isEmpty()) {
			taskID = 1;
		} else {
			int lastID = _taskList.lastKey();
			taskID = lastID + 1;
		}
		
		switch (type) {
			case FLOATING:
				task = new Task(taskID, 
						_result.getContent(),
						_result.getType());
				break;
			case DEADLINE:
				task = new Task(taskID,
						_result.getContent(),
						_result.getType(),
						_result.getEndDate());
				break;
			case EVENT:
				task = new Task(taskID,
						_result.getContent(),
						_result.getType(),
						_result.getStartDate(),
						_result.getEndDate());
				break;
			case INVALID: default:
				task = null;
		}
		
		if (task != null) {
			_taskList.put(taskID, task);
			Storage.writeTasks(_taskList);
			message = String.format(FeedbackHelper.MSG_ADD, _result.getContent());
			isSuccessful = true;
		}
		
		ArrayList<Task> displayList = 
				new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}
}

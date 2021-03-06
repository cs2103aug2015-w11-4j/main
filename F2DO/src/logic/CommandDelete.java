package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;

//@@author A0111758E
public class CommandDelete implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandDelete(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	/**
	 * Delete task.
	 */
	public Feedback execute() {
		int taskID = _result.getStorageID();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_NO_INDEX;
		
		if (_taskList.containsKey(taskID)) {
			String taskName = _taskList.get(taskID).getTaskName();
			_taskList.remove(taskID);
			Storage.writeTasks(_taskList);
			message = String.format(FeedbackHelper.MSG_DELETE, taskName);
			isSuccessful = true;
		}
		
		ArrayList<Task> displayList = 
				new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}
}

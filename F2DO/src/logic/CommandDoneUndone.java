package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;

//@@author A0111758
public class CommandDoneUndone implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	private boolean _isDone = false;
	
	public CommandDoneUndone(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList,
			boolean isDone) {
		_result = result;
		_taskList = taskList;
		_isDone = isDone;
	}

	public Feedback execute() {
		int taskID = _result.getStorageID();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_NO_INDEX;
		
		if (_taskList.containsKey(taskID)) {
			Task task = _taskList.get(taskID);
			
			if (_isDone) {
				task.setCompleted(true);
				message = String.format(FeedbackHelper.MSG_DONE, task.getTaskName());
				isSuccessful = true;
			} else {
				task.setCompleted(false);
				message = String.format(FeedbackHelper.MSG_UNDONE, task.getTaskName());
				isSuccessful = true;
			}
			
			_taskList.put(taskID, task);
			Storage.writeTasks(_taskList);
		}
		
		ArrayList<Task> displayList = new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}
}

package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Task;
import parser.Result;
import storage.Storage;

/**
 * CommandDelete executes delete function.
 * @author 
 *
 */
public class CommandDelete implements ICommand {
	private Result _result = null;
	private static ConcurrentSkipListMap<Integer, Task> _taskList =
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
		String message = FeedbackHelper.ERROR_DELETE;
		int taskID = _result.getStorageID();
		
		if (_taskList.containsKey(taskID)) {
			_taskList.remove(taskID);
			Storage.writeTasks(_taskList);
			message = String.format(FeedbackHelper.MSG_DELETE, _result.getTitle());
		}
		
		ArrayList<Task> displayList = 
				new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList);
	}
}

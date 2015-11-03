//@@author Sufyan
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;

/**
 * CommandEdit class executes edit function.
 * @author 
 *
 */
public class CommandEdit implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandEdit(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	/**
	 * Edit task.
	 */
	public Feedback execute() {
		int taskID = _result.getStorageID();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_NO_INDEX;
		
		if (_taskList.containsKey(taskID)) {
			Task task = _taskList.get(taskID);
			
			task.setTaskName(_result.getContent());
			task.setTaskType(_result.getType());
			task.setStartDate(_result.getStartDate());
			task.setEndDate(_result.getEndDate());
			
			_taskList.put(taskID, task);
			Storage.writeTasks(_taskList);
			
			message = String.format(FeedbackHelper.MSG_EDIT, _result.getContent());
			isSuccessful = true;
		}
		
		ArrayList<Task> displayList = new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
		
	}
}

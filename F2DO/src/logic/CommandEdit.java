//@@author Sufyan
package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;
import type.TaskType;

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
			String taskName = _result.getContent();
			TaskType taskType = _result.getType();
			Date startDate = _result.getStartDate();
			Date endDate = _result.getEndDate();
			
			if (taskName.trim().isEmpty() && startDate == null && endDate == null) {
				message = FeedbackHelper.ERROR_EDIT_NOTHING;
			} else {
				task.setTaskName(taskName);
				task.setTaskType(taskType);
				task.setStartDate(startDate);
				task.setEndDate(endDate);

				_taskList.put(taskID, task);
				Storage.writeTasks(_taskList);

				message = String.format(FeedbackHelper.MSG_EDIT, _result.getContent());
				isSuccessful = true;
			}
		}
		
		ArrayList<Task> displayList = new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
		
	}
}

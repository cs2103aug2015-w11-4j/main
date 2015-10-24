package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Task;
import parser.Result;

/**
 * CommandSearch class executes search function.
 * @author 
 *
 */
public class CommandSearch implements ICommand {
	private Result _result = null;
	private static ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandSearch(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	/**
	 * Search tasks that contain the search key.
	 */
	public Feedback execute() {
		String searchKey = _result.getTitle();
		ArrayList<Task> taskList = new ArrayList<Task>(_taskList.values());
		ArrayList<Task> displayList = new ArrayList<Task>();
		String message = FeedbackHelper.ERROR_SEARCH;
		
		if (!searchKey.equals("")) {
			for (int i = 0; i < taskList.size(); i++) {
				Task task = taskList.get(i);
				String taskName = task.getTaskName();

				if (taskName.contains(searchKey)) {
					displayList.add(task);
				}
			}
		}
		
		if (!displayList.isEmpty()) {
			message = FeedbackHelper.MSG_SEARCH;
		}
		
		return new Feedback(message, displayList, _taskList);
	}
}

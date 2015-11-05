//@@author Sufyan
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;

/**
 * CommandSearch class executes search function.
 * @author 
 *
 */
public class CommandSearch implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
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
		String searchKey = _result.getContent();
		String[] tokenisedKey = searchKey.split(" ");
		ArrayList<Task> taskList = new ArrayList<Task>(_taskList.values());
		ArrayList<Task> displayList = new ArrayList<Task>();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_SEARCH;
		
		if (!searchKey.equals(null) && !searchKey.equals("")) {
			for (int i = 0; i < taskList.size(); i++) {
				Task task = taskList.get(i);
				String taskName = task.getTaskName().toLowerCase();

				for (int j = 0; j < tokenisedKey.length; j++) {
					String key = tokenisedKey[j].toLowerCase();
					
					if (!taskName.contains(key)) {
						break;
					}
					
					if (j == (tokenisedKey.length-1)) {
						displayList.add(task);
					}
				}
			}
		}
		
		if (displayList.size() == 1) {
			message = String.format(FeedbackHelper.MSG_SEARCH, 1, "");
			isSuccessful = true;
		} else if (!displayList.isEmpty()) {
			message = String.format(FeedbackHelper.MSG_SEARCH, displayList.size(), "s");
			isSuccessful = true;
		}
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}
}

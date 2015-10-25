package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;

public class CommandShow implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	private enum ShowType {
		ALL,
		DONE,
		UNDONE,
		INVALID;
		
		private static ShowType toType(String word) {
			try {
				return valueOf(word); 
			} catch (Exception e) {
				return INVALID; 
			}
		}
	}
	
	public CommandShow(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	public Feedback execute() {
		int size = 0;
		String requestType = _result.getContent();
		ShowType type = ShowType.toType(requestType.toUpperCase());
		ArrayList<Task> displayList = new ArrayList<Task>();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_SHOW_NO_ALL;
		
		switch (type) {
			case ALL:
				displayList = new ArrayList<Task>(_taskList.values());
				size = displayList.size();
				
				if (size > 0) {
					isSuccessful = true;
					if (size > 1) {
						message = String.format(FeedbackHelper.MSG_SHOW_ALL, "s");
					} else {
						message = String.format(FeedbackHelper.MSG_SHOW_ALL, "");;
					}
				}
				break;
			case DONE:
				displayList = searchTasks(true);
				size = displayList.size();
				
				if (size > 0) {
					isSuccessful = true;
					if (size > 1) {
						message = String.format(FeedbackHelper.MSG_SHOW_DONE, size, "s");
					} else {
						message = String.format(FeedbackHelper.MSG_SHOW_DONE, size, "");
					}
				} else {
					message = FeedbackHelper.ERROR_SHOW_NO_DONE;
				}
				
				break;
			case UNDONE:
				displayList = searchTasks(false);
				size = displayList.size();
				
				if (size > 0) {
					isSuccessful = true;
					if (size > 1) {
						message = String.format(FeedbackHelper.MSG_SHOW_UNDONE, size, "s");
					} else {
						message = String.format(FeedbackHelper.MSG_SHOW_UNDONE, size, "");
					}
				} else {
					message = FeedbackHelper.ERROR_SHOW_NO_UNDONE;
				}
				
				break;
			case INVALID: default:
				// DO NOTHING
		}
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}
	
	private ArrayList<Task> searchTasks(boolean isCompleted) {
		ArrayList<Task> taskList = new ArrayList<Task>(_taskList.values());
		ArrayList<Task> searchTasks = new ArrayList<Task>();
		
		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			boolean isDone = task.getCompleted();
			
			if (isDone == isCompleted) {
				searchTasks.add(task);
			}
		}
		return searchTasks;
	}

}

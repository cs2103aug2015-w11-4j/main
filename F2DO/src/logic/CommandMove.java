package logic;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;

//@@author A0111758E
public class CommandMove implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandMove(Result result, ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}

	public Feedback execute() {
		String path = _result.getContent();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_MOVE;
		
		if (path != null) {
			path = path.trim();
			File file = new File(path);
			
			if (file.exists() && file.isDirectory()) {
				isSuccessful = Storage.setFolder(path);
				message = FeedbackHelper.MSG_MOVE;
			}
			
			if (!file.isDirectory()) {
				message = FeedbackHelper.ERROR_MOVE_NO_DIRECTORY;
			}
		}
		
		ArrayList<Task> displayList = 
				new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}

}

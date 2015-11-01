package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Task;

public class CommandHome implements ICommand {
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandHome(ConcurrentSkipListMap<Integer, Task> taskList) {
		_taskList = taskList;
	}
	public Feedback execute() {
		ArrayList<Task> displayList = new ArrayList<Task>(_taskList.values());
		String message = FeedbackHelper.MSG_HOME;
		
		return new Feedback(message, displayList, _taskList, true);
	}

}

package logic;

import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;

/**
 * CommandEdit class executes edit function.
 * @author 
 *
 */
public class CommandEdit implements ICommand {

	public CommandEdit(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList) {
		
	}
	
	/**
	 * Edit task.
	 */
	public Feedback execute() {
		return null;
	}
}

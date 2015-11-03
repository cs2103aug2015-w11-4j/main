//@@author Sufyan
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Category;
import object.Result;
import object.Task;
import type.CommandType;

public interface ICommand {
	/**
	 * Get respective class according to the command.
	 * @param result - result parsed by Parser
	 * @param taskList - task list
	 * @param _categoryList 
	 * @return class to execute
	 */
	public static ICommand getCommand(Result result, 
			ConcurrentSkipListMap<Integer, Task> taskList, 
			ArrayList<Category> categoryList) {
		CommandType commandType = result.getCmd();
		
		switch (commandType) {
			case ADD:
				return new CommandAdd(result, taskList);
			case DELETE:
				return new CommandDelete(result, taskList);
			case EDIT:
				return new CommandEdit(result, taskList);
			case DONE:
				return new CommandDoneUndone(result, taskList, true);
			case UNDONE:
				return new CommandDoneUndone(result, taskList, false);
			case SEARCH:
				return new CommandSearch(result, taskList);
			case SHOW:
				return new CommandShow(result, taskList);
			case HELP:
				return new CommandHelp(taskList);
			case MOVE:
				return new CommandMove(result);
			case HOME:
				return new CommandHome(taskList);
			case INVALID: default:
				return new CommandDefault(taskList);
		}
	}
	
	/**
	 * Execute the command.
	 * @return feedback
	 */
	public Feedback execute();
}

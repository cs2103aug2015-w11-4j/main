package logic;

import java.util.concurrent.ConcurrentSkipListMap;

import object.Task;
import parser.Result;
import type.CommandType;

public interface ICommand {
	/**
	 * Get respective class according to the command.
	 * @param result - result parsed by Parser
	 * @param taskList - task list
	 * @return class to execute
	 */
	public static ICommand getCommand(Result result, 
			ConcurrentSkipListMap<Integer, Task> taskList) {
		CommandType commandType = result.getCmd();
		
		switch (commandType) {
			case ADD:
				return new CommandAdd(result, taskList);
			case DELETE:
				return new CommandDelete(result, taskList);
			case EDIT:
				return new CommandEdit(result, taskList);
			case SEARCH:
				return new CommandSearch(result, taskList);
			case SHOW:
				return new CommandShow(result, taskList);
			case DONE:
				return new CommandDoneUndone(result, taskList, true);
			case UNDONE:
				return new CommandDoneUndone(result, taskList, false);
			case HELP:
				return null;
			case HOME:
				return null;
			case INVALID: default:
				return null;
		}
	}
	
	/**
	 * Execute the command.
	 * @return feedback
	 */
	public Feedback execute();
}

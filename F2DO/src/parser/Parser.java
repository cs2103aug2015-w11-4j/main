package parser;

import java.util.Date;
import java.util.ArrayList;
import java.lang.NumberFormatException;

import type.CommandType;
import type.TaskType;
import object.Result;
import object.Task;

public class Parser {
	private static String _removeCmdInput = "";
	
	/**
	 * Parse the command.
	 * @param input - command input
	 * @return the parsed result
	 */
	public static Result parse(String input, ArrayList<Task> taskList) {
		int storageID = -1;
		_removeCmdInput = "";
		
		CommandType cmd = analyzeCmd(input);
		
		if (cmd == CommandType.DELETE || 
				cmd == CommandType.EDIT ||
				cmd == CommandType.DONE ||
				cmd == CommandType.UNDONE) {
			storageID = analyzeID(_removeCmdInput, taskList);
		}
		
		Result tempResult = analyzeDateTitle(_removeCmdInput);
		
		String title = tempResult.getContent();
		Date startDate = tempResult.getStartDate();
		Date endDate = tempResult.getEndDate();
		TaskType type = analyzeTask(title, startDate, endDate);
		
		Result result = new Result(storageID, cmd, title, type, startDate, endDate);
		return result;
	}
	
	/**
	 * Determine type of the command.
	 * @param word - parsed word
	 * @return command type
	 */
	private static CommandType analyzeCmd(String input) {
		String[] splitWords = input.split(" ");
		CommandType cmd = CommandType.INVALID;
		
		for (int i = 0; i < splitWords.length; i++) {
			if (cmd == CommandType.INVALID) {
				cmd = CommandType.toCmd(splitWords[i].toUpperCase());
				
				if (cmd != CommandType.INVALID) {
					continue;
				}
			}
			_removeCmdInput += splitWords[i] + " ";
		}

		return cmd;
	}
	
	/**
	 * Determine the ID in storage corresponding to UI ID.
	 * @param input - input after removing command
	 * @param taskList - task list displayed in UI
	 * @return ID in storage if exists, otherwise -1
	 */
	private static int analyzeID(String input, ArrayList<Task> taskList) {
		String[] splitWords = input.split(" ");
		int storageID = -1;
		
		if (splitWords.length > 0) {
			try {
				int displayID = Integer.parseInt(splitWords[0]) - 1;

				if (displayID < taskList.size() && displayID >= 0) {
					storageID = taskList.get(displayID).getTaskID();
				}

				if (splitWords.length > 1) {
					_removeCmdInput = "";
					for (int i = 1; i < splitWords.length; i++) {
						_removeCmdInput += splitWords[i] + " ";
					}
				}
				
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		
		return storageID;
	}
	
	/**
	 * Determine type of the task.
	 * @param title - title of the task
	 * @param startDate - start date and time
	 * @param endDate - end date and time
	 * @return task type
	 */
	private static TaskType analyzeTask(String title, Date startDate, Date endDate) {
		if (title == null && startDate == null && endDate == null) {
			return TaskType.INVALID;
		}
		
		if (startDate == null && endDate == null) {
			return TaskType.FLOATING;
		} else if (startDate == null && endDate != null) {
			return TaskType.DEADLINE;
		} else {
			return TaskType.EVENT;
		}
	}
	
	/**
	 * Determine the dates and title of the task.
	 * @param input - input after removing command
	 * @return title, start date and end date
	 */
	private static Result analyzeDateTitle(String input) {
		IKeyword function = IKeyword.parsedPreposition(input);
		return function.analyze();
	}
}

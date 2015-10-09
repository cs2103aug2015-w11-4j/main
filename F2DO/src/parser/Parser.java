package parser;

import java.util.Date;
import java.util.ArrayList;
import java.lang.NumberFormatException;

import type.CommandType;
import type.TaskType;

import objects.Task;
//import storage.Storage;
import storage.Storage;

public class Parser {
	private static String _removeCmdInput = "";
	
	/**
	 * Parse the command.
	 * @param input - command input
	 * @return the parsed result
	 */
	public static Result parse(String input, ArrayList<Task> taskList) {
		int displayID = -1;
		int storageID = -1;
		_removeCmdInput = "";
		
		CommandType cmd = analyzeCmd(input);
		
		if (cmd == CommandType.DELETE || cmd == CommandType.EDIT) {
			Result idResult = analyzeID(_removeCmdInput, taskList);
			displayID = idResult.getDisplayID();
			storageID = idResult.getStorageID();
		}
		
		Result tempResult = analyzeDateTitle(_removeCmdInput);
		
		String title = tempResult.getTitle();
		Date startDate = tempResult.getStartDate();
		Date endDate = tempResult.getEndDate();
		TaskType type = analyzeTask(title, startDate, endDate);
		
		Result result = new Result(displayID, storageID, cmd, title, type, startDate, endDate);
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
	private static Result analyzeID(String input, ArrayList<Task> taskList) {
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
				
				return new Result(displayID, storageID);
			} catch (NumberFormatException e) {
				return new Result(-1, storageID);
			}
		}
		
		return new Result(-1, storageID);
	}
	
	/**
	 * Determine type of the task.
	 * @param title - title of the task
	 * @param startDate - start date and time
	 * @param endDate - end date and time
	 * @return task type
	 */
	private static TaskType analyzeTask(String title, Date startDate, Date endDate) {
		if (title == null) {
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
	
	private static Result analyzeDateTitle(String input) {
		IKeyword function = IKeyword.parsedPreposition(input);
		return function.analyze();
	}
	
	public static void main(String[] args) {
		String input = "edit 1 task one at 18:00";
		Result result = Parser.parse(input, Storage.getTaskList());
		
		print("input", input);
		print("display id", result.getDisplayID());
		print("storage id", result.getStorageID());
		print("cmd", result.getCmd());
		print("title", result.getTitle());
		print("type", result.getType());
		print("startDate", result.getStartDate());
		print("endDate", result.getEndDate());
		System.out.println();
		
		String input2 = "add two on 4 Sep from 4pm to 6pm";
		Result result2 = Parser.parse(input2, Storage.getTaskList());
		
		print("input", input2);
		print("display id", result2.getDisplayID());
		print("storage id", result2.getStorageID());
		print("cmd", result2.getCmd());
		print("title", result2.getTitle());
		print("type", result2.getType());
		print("startDate", result2.getStartDate());
		print("endDate", result2.getEndDate());
		System.out.println();
		
		String input3 = "add assignment in 3 days";
		Result result3 = Parser.parse(input3, Storage.getTaskList());
		
		print("input", input3);
		print("display id", result3.getDisplayID());
		print("storage id", result3.getStorageID());
		print("cmd", result3.getCmd());
		print("title", result3.getTitle());
		print("type", result3.getType());
		print("startDate", result3.getStartDate());
		print("endDate", result3.getEndDate());

	}
	
	private static void print(String indicator, Object obj) {
		System.out.println(indicator + ": " + obj);
	}
}

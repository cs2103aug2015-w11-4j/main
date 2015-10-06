package parser;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.lang.NumberFormatException;

import type.CommandType;
import type.TaskType;

import objects.Task;
import storage.Storage;

public class Parser {
	private static String _removeCmdInput = "";
	/**
	 * Parse the command.
	 * @param input - command input
	 * @return the parsed result
	 */
	public static Result parse(String input, ArrayList<Task> taskList) {
		int id = -1;
		_removeCmdInput = "";
		
		CommandType cmd = analyzeCmd(input);
		
		if (cmd == CommandType.EDIT || cmd == CommandType.EDIT) {
			id = analyzeID(_removeCmdInput, taskList);
		}
		
		Result tempResult = analyzeDateTitle(_removeCmdInput);
		
		String title = tempResult.getTitle();
		Date startDate = tempResult.getStartDate();
		Date endDate = tempResult.getEndDate();
		TaskType type = analyzeTask(title, startDate, endDate);
		
		Result result = new Result(id, cmd, title, type, startDate, endDate);
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
				int displayID = Integer.parseInt(splitWords[0]);

				if (displayID < taskList.size()) {
					storageID = taskList.get(displayID - 1).getTaskID();
				}

				if (splitWords.length > 1) {
					_removeCmdInput = "";
					for (int i = 1; i < splitWords.length; i++) {
						_removeCmdInput += splitWords[i] + " ";
					}
				}
				
				return storageID;
			} catch (NumberFormatException e) {
				return storageID;
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
		IPreposition function = IPreposition.parsedPreposition(input);
		return function.analyze();
	}
	
	public static void main(String[] args) {
		String input = "add two from 4pm to 6pm on Nov 4";
		Result result = Parser.parse(input, Storage.getTaskList());
		
		print("input", input);
		print("id", result.getID());
		print("cmd", result.getCmd());
		print("title", result.getTitle());
		print("type", result.getType());
		print("startDate", result.getStartDate());
		print("endDate", result.getEndDate());
		print("", "");
		
		/*String input2 = "add two";
		Result result2 = Parser.parse(input2, Storage.getTaskList());
		
		print("input", input2);
		print("id", result2.getID());
		print("cmd", result2.getCmd());
		print("title", result2.getTitle());
		print("type", result2.getType());
		print("startDate", result2.getStartDate());
		print("endDate", result2.getEndDate());*/

	}
	
	private static void print(String indicator, Object obj) {
		System.out.println(indicator + ": " + obj);
	}
}

//@@author Yu Ting
package parser;

import java.util.Date;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.NumberFormatException;

import type.CommandType;
import type.KeywordType;
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
		TaskType type = analyzeTask(cmd, title, startDate, endDate);
		
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
				cmd = CommandType.toCmd(splitWords[i]);
				
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

				if (splitWords.length >= 1) {
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
	private static TaskType analyzeTask(CommandType type, String title, Date startDate, Date endDate) {
		if (title == null && startDate == null && endDate == null) {
			return TaskType.INVALID;
		}
		
		if (type.equals(CommandType.ADD) && (title == null || title.equals("") || title.isEmpty())) {
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
		ArrayList<String> splitWords = new ArrayList<String>(Arrays.asList(input.split(" ")));
		TreeMap<Integer, KeywordType> keywordIndices = KeywordHelper.getKeywordIndex(splitWords);
		ArrayList<Integer> indexList = new ArrayList<Integer>(keywordIndices.keySet());
		int listSize = indexList.size();
		
		// If the input contains keyword
		for (int i = 0; i < listSize; i++) {
			int index = indexList.get(i);
			KeywordType keyword = keywordIndices.get(index);
			String parseInput = "";
			
			if (i < (listSize - 1)) {
				int nextIndex = indexList.get(i + 1);
				
				for (int j = 0; j < nextIndex; j++) {
					parseInput += splitWords.get(j) + " ";
				}
			} else {
				parseInput = input;
			}
			
			IKeyword function = IKeyword.parseKeyword(keyword, parseInput);
			Result result = function.analyze();
			
			if (isDateFound(result.getStartDate(), result.getEndDate())) {
				return result;
			}
		}
		
		// If the input does not contain keyword
		IKeyword function = IKeyword.parseKeyword(null, input);
		return function.analyze();
	}
	
	/**
	 * Check if any of start date or end date is found.
	 * @param startDate - start date
	 * @param endDate - end date
	 * @return true if any date is found; false otherwise
	 */
	private static boolean isDateFound(Date startDate, Date endDate) {
		if (startDate == null && endDate == null) {
			return false;
		}
		return true;
	}
}

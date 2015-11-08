package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
//import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Logger;

import date.DatePair;
import object.Result;
import object.Task;
import type.CommandType;
import type.KeywordType;
import type.TaskType;

//@@author A0118005W
public class Parser {
	private static final String JOIN_DELIMITER = " ";
	private static final String SPLIT_DELIMITER = "\\s+";
	private static final String REPLACE_DELIMITER = "";
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Parse the input string. 
	 * The result includes command type, corresponding taskID,
	 * content, task type, start date and end date. Any parsing
	 * error is included as well.
	 * @param input - input string
	 * @param displayList - display list
	 * @return the parsing result
	 */
	public static Result parse(String input, ArrayList<Task> displayList) {
		String[] splitWords = input.split(SPLIT_DELIMITER);
		ArrayList<String> words = new ArrayList<>(Arrays.asList(splitWords));
		
		Result result = new Result();
		CommandType cmd = analyzeCommand(words);
		int taskID = -1;
		String content = null;
		TaskType taskType = TaskType.INVALID;
		Date startDate = null;
		Date endDate = null;
		
		if (cmd != CommandType.INVALID) {
			words.remove(0);			// remove command from input string
			
			if (isIDNeeded(cmd)) {
				taskID = analyzeID(words, displayList);
				
				if (taskID == -1) {
					result.setErrorMsg(ParserHelper.ERROR_TASK_ID);
				} else {
					words.remove(0);	// remove display from input string
				}
			}
			
			ContentDate contentDate = analyzeContentDate(words);
			
			content = contentDate.getContent();
			startDate = contentDate.getStartDate();
			endDate = contentDate.getEndDate();
			
			if (contentDate.isError()) {
				result.setErrorMsg(contentDate.getErrorMsg());
			}
			
			taskType = analyzeTask(content, startDate, endDate);
			
		} else {
			//result.setErrorMsg(ParserHelper.ERROR_INVALID_COMMAND);
			content = input;
		}
		
		result.setCommand(cmd);
		result.setStorageID(taskID);
		result.setContent(content);
		result.setType(taskType);
		result.setStartDate(startDate);
		result.setEndDate(endDate);
		
		return result;
	}
	
	/**
	 * Check if getting of ID is need according to the input command.
	 * @param cmd - input command
	 * @return true if getting ID is need; false otherwise
	 */
	private static boolean isIDNeeded(CommandType cmd) {
		return cmd == CommandType.DELETE || 
				cmd == CommandType.EDIT ||
				cmd == CommandType.DONE ||
				cmd == CommandType.UNDONE;
	}
	
	/**
	 * Determine type of the command.
	 * @param words - words to be analyzed
	 * @return command type
	 */
	private static CommandType analyzeCommand(ArrayList<String> words) {
		if (words.size() > 0) {
			return CommandType.toCmd(words.get(0));
		}
		return CommandType.INVALID;
	}
	
	/**
	 * Determine the ID in storage corresponding to UI ID.
	 * @param words - words to be analyzed
	 * @param displayList - task list displayed in UI
	 * @return task ID in storage if exists; -1 otherwise
	 */
	private static int analyzeID(ArrayList<String> words, ArrayList<Task> displayList) {
		int taskID = -1;
		
		try {
			if (words.size() > 0) {
				int displayID = Integer.parseInt(words.get(0)) - 1;
				
				if (displayID >= 0 && displayID < displayList.size()) {
					taskID = displayList.get(displayID).getTaskID();
				}
			}
		} catch (NumberFormatException e) {
			logger.warning("Unable to retrieve task ID in storage.");
			return -1;
		}
		
		return taskID;
	}
	
	/**
	 * Determine the dates and title of the task.
	 * @param words - words to be analyzed
	 * @return title, start date and end date
	 */
	private static ContentDate analyzeContentDate(ArrayList<String> words) {
		TreeMap<Integer, KeywordType> keywordIndices = ParserHelper.getKeywordIndex(words);
		ArrayList<Integer> indexList = new ArrayList<Integer>(keywordIndices.keySet());
		int listSize = indexList.size();
		String content = String.join(" ", words);
		Date startDate = null;
		Date endDate = null;
		boolean isError = false;
		String errorMsg = null;
		
		for (int i = 0; i < listSize; i++) {
			int index = indexList.get(i);
			KeywordType keyword = keywordIndices.get(index);
			String parseInput = "";
			isError = false;
			errorMsg = null;
			
			if (i < (listSize - 1)) {
				int nextIndex = indexList.get(i + 1);
				KeywordType nextKeyword = keywordIndices.get(nextIndex);
				
				// Include next keyword
				if (shouldInclude(nextKeyword)) {
					if ((i+1) < (listSize - 1)) {
						int nextNextIndex = indexList.get(i + 2);
						parseInput = String.join(JOIN_DELIMITER, words.subList(index, nextNextIndex));
					} else {
						parseInput = String.join(JOIN_DELIMITER, words.subList(index, words.size()));
					}
				} else {
					parseInput = String.join(JOIN_DELIMITER, words.subList(index, nextIndex));
				}
			} else {
				parseInput = String.join(JOIN_DELIMITER, words.subList(index, words.size()));
			}
			
			IParseDateTime function = IParseDateTime.getFunction(keyword, parseInput);
			DatePair result = function.analyze();
			
			// If date is found, remove the date time string from the input string
			if (isDateFound(result.getStartDate(), result.getEndDate())) {
				if (result.isError()) {
					isError = true;
					errorMsg = result.getErrorMsg();
				} else {
					String dateString = result.getDateString();
					startDate = result.getStartDate();
					endDate = result.getEndDate();
					content = String.join(" ", words);

					assert (dateString != null);

					content = content.replace(dateString, REPLACE_DELIMITER);

					String[] splitWords = content.split(SPLIT_DELIMITER);
					ArrayList<String> contentList = new ArrayList<>(Arrays.asList(splitWords));

					content = String.join(JOIN_DELIMITER, contentList);

					if (startDate != null && endDate != null) {
						if (startDate.compareTo(endDate) > 0) {
							isError = true;
							errorMsg = ParserHelper.ERROR_END_DATE_EARLIER;
						}
					}
				}
				
				break;
			}
		}
		
		ContentDate contentDate = new ContentDate(content, startDate, endDate);
		
		if (isError) {
			contentDate.setErrorMsg(errorMsg);
		}
		
		return contentDate;
	}
	
	/**
	 * Check if the keyword should be included in the next sentence.
	 * @param keyword
	 * @return true if the keyword should be include; false otherwise
	 */
	private static boolean shouldInclude(KeywordType keyword) {
		return keyword == KeywordType.ON ||
				keyword == KeywordType.TODAY ||
				keyword == KeywordType.TOMORROW;
	}
	
	/**
	 * Check if any of start date or end date is found.
	 * @param startDate - start date
	 * @param endDate - end date
	 * @return true if any date is found; false otherwise
	 */
	private static boolean isDateFound(Date startDate, Date endDate) {
		if (startDate == null && endDate == null) {
			logger.info("Both the desired start and end date are not found.");
			return false;
		}
		return true;
	}
	
	/**
	 * Determine type of the task.
	 * @param title - title of the task
	 * @param startDate - start date and time
	 * @param endDate - end date and time
	 * @return task type
	 */
	private static TaskType analyzeTask(String title, Date startDate, Date endDate) {
		if (title == null || title.trim().isEmpty()) {
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
	
	/*public static void main(String[] args) {
		while (true) {
			System.out.print("Input: ");
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			ArrayList<Task> displayList = new ArrayList<Task>();
			Task task1 = new Task(1, "lala", TaskType.FLOATING);
			Task task2 = new Task(3, "haha", TaskType.EVENT);

			displayList.add(task1);
			displayList.add(task2);

			Result result = Parser.parse(input, displayList);

			if (result.isError()) {
				System.out.println("Error: " + result.getErrorMsg());
			}

			System.out.println("Command: " + result.getCommand());
			System.out.println("Task ID: " + result.getStorageID());
			System.out.println("Content: " + result.getContent());
			System.out.println("TaskType: " + result.getType());
			System.out.println("StartDate: " + result.getStartDate());
			System.out.println("EndDate: " + result.getEndDate());
		}
	}*/
}

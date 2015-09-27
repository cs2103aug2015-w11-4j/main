package parser;

import java.util.Date;

import type.CommandType;
import type.TaskType;

public class Parser {
	private static String _removeCmdInput = "";
	/**
	 * Parse the command.
	 * @param input - command input
	 * @return the parsed result
	 */
	public static Result Parse(String input) {
		
		CommandType cmd = analyzeCmd(input);
		Result tempResult = analyzeDateTitle(_removeCmdInput);
		
		String title = tempResult.getTitle();
		Date startDate = tempResult.getStartDate();
		Date endDate = tempResult.getEndDate();
		TaskType type = analyzeTask(title, startDate, endDate);
		
		Result result = new Result(cmd, title, type, startDate, endDate);
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
		String input = "add cs2103 meeting";
		Result result = Parser.Parse(input);
		
		print("input", input);
		print("cmd", result.getCmd());
		print("title", result.getTitle());
		print("type", result.getType());
		print("startDate", result.getStartDate());
		print("endDate", result.getEndDate());
	}
	
	private static void print(String indicator, Object obj) {
		System.out.println(indicator + ": " + obj);
	}
}

package logic;

import java.util.ArrayList;

import objects.Task;
import parser.Parser;
import parser.Result;
import storage.Storage;

public class LogicController {
	
	// take in parser, return result, 
	// determine command
	// execute command
	// pass to storage
	private static ArrayList<Task> taskList =  new ArrayList<Task>();
	
	private static String MSG_ADD = "Feedback: %1$s has been successfully added!";
	private static String MSG_EDIT = "Feedback: Task description has been modified!";
	private static String MSG_DELETE = "Feedback: %1$s has been deleted!";
	private static String MSG_SEARCH = "";
	private static String MSG_NO_ACTION = "Feedback: No action is done!";
	
	
	public boolean initialize(){
		// load any existing task to logic
		return true;
	}
	
	
	public static String process(String input) {	
		Result result = Parser.Parse(input);
		
		switch (result.getCmd()) {
			case ADD: {
				//taskList = LogicAdd.add(taskList, result);
				return String.format(MSG_ADD, result.getTitle());
			}
			case DELETE: {
				LogicDelete.delete(taskList, result);
				return String.format(MSG_DELETE, result.getTitle());
			} 
			case EDIT: {
				return MSG_EDIT;
			}
			case SEARCH: {
				LogicSearch.search(taskList, result);
				return MSG_SEARCH;
			} 
			default: {
				return MSG_NO_ACTION;
			} 
		}
		//return false;
	}
	
	public static ArrayList<Task> getTaskList() {
		return Storage.getTaskList();
	}
}

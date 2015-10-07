package logic;

import java.util.ArrayList;

import objects.Task;
import parser.Parser;
import parser.Result;
import storage.Storage;

public class LogicController {
	
	private static ArrayList<Task> _taskList =  new ArrayList<Task>();
	private static int taskID = 0;
	
	private static String MSG_ADD = "Feedback: %1$s has been successfully added!";
	private static String MSG_EDIT = "Feedback: Task description has been modified!";
	private static String MSG_DELETE = "Feedback: %1$s has been deleted!";
	private static String MSG_SEARCH = "";
	private static String MSG_NO_ACTION = "Feedback: No action is done!";
	
	// Initialize LogicController class
	static {
		initialize();
	}
	
	private static void initialize() {
		_taskList = Storage.getTaskList();
		if (_taskList.isEmpty()){
			// If taskList is empty, set taskID to 1
			taskID = 1;
		} else {
			// Get taskID of last object in taskList
			// Then increment that ID by 1 and store to local var taskID
			taskID = _taskList.get(_taskList.size()-1).getTaskID() + 1;
		}
	}
	
	public static String process(String input, ArrayList<Task> taskList) {	
		Result result = Parser.parse(input, taskList);
		
		System.out.println("displayID: " + result.getDisplayID());
		System.out.println("storageID: " + result.getStorageID());
		
		switch (result.getCmd()) {
			case ADD: {
				taskList = LogicAdd.add(taskID,result,taskList);
				taskID++;
				return String.format(MSG_ADD, result.getTitle());
			}
			case DELETE: {
				taskList = LogicDelete.delete(taskID, taskList, result);
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
		return _taskList;
	}
}

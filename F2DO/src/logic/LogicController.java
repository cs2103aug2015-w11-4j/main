package logic;

import java.util.ArrayList;
import objects.Task;
import parser.Parser;
import parser.Result;
import storage.Storage;

public class LogicController {

	private static ArrayList<Task> _taskList =  new ArrayList<Task>();
	private static ArrayList<Task> _searchList =  new ArrayList<Task>();
	private static int taskID = 0;

	private static String MSG_ADD = "Feedback: %1$s has been successfully added!";
	private static String MSG_EDIT = "Feedback: %1$s has been modified!";
	private static String MSG_DELETE = "Feedback: %1$s has been deleted!";
	private static String MSG_SEARCH = "Feedback: Returning results!";
	private static String MSG_NO_ACTION = "Feedback: No action is done!";
	private static String ERROR_SEARCH = "Feedback: No results found!";
	private static String ERROR_EDIT = "Feedback: %1$s cannot be modified!";
	private static String ERROR_DELETE = "Feedback: %1$s cannot be deleted!";

	// Initialize LogicController class, Only runs once
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

	public static String process(String input) {	
		Result result = Parser.parse(input, _taskList);

		System.out.println("LogicController = TaskID: " + taskID);
		System.out.println("LogicController = DisplayID: " + result.getDisplayID());
		System.out.println("LogicController = StorageID: " + result.getStorageID());

		switch (result.getCmd()) {
			case ADD: {
				/*	
				Task task = new Task(taskID, result.getType(), result.getTitle(), result.getStartDate(), result.getEndDate(), 0);
				 Storage.addTask(task);
				 */
	
				LogicAdd.add(taskID, result, _taskList);
				taskID++;
				Storage.saveToFile2(_taskList);
				return String.format(MSG_ADD, result.getTitle());
			}
			case DELETE: {
				/*
				if (result.getDisplayID() != -1 && result.getStorageID() == getTaskList().get(result.getDisplayID()).getTaskID()) 
				Storage.deleteTask(result.getDisplayID());
				 */
				LogicDelete.delete(result, _taskList);
				taskID = _taskList.get(_taskList.size()-1).getTaskID() + 1;
				Storage.saveToFile2(_taskList);
				return String.format(MSG_DELETE, result.getTitle());
			} 
			case EDIT: {
				if (LogicEdit.edit(result,_taskList)){
					Storage.saveToFile2(_taskList);
					return String.format(MSG_EDIT, result.getTitle());
				} else {
					return String.format(ERROR_EDIT, result.getTitle());
				}
			}
			case SEARCH: {
				_searchList = LogicSearch.search(result, _taskList);
				if (_searchList.size() == 0){
					return ERROR_SEARCH;
				} else {
					return MSG_SEARCH;
				}
			} 
			default: {
				return MSG_NO_ACTION;
			} 
		}
	}

	public static ArrayList<Task> getTaskList() {
		return _taskList;
	}

	public static ArrayList<Task> getSearchList() {
		return _searchList;
	}
}

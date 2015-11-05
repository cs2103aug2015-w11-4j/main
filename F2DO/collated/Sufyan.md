# Sufyan
###### src\logic\CommandAdd.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;
import type.TaskType;

/**
 * CommandAdd class executes add function.
 * @author 
 *
 */
public class CommandAdd implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandAdd(Result result, ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	/**
	 * Add task.
	 */
	public Feedback execute() {
		Task task;
		TaskType type = _result.getType();
		int taskID = -1;
		boolean isSuccessful = false;
		String message = String.format(FeedbackHelper.ERROR_ADD, _result.getContent());
		
		if (_taskList.isEmpty()) {
			taskID = 1;
		} else {
			int lastID = _taskList.lastKey();
			taskID = lastID + 1;
		}
		
		switch (type) {
			case FLOATING:
				task = new Task(taskID, 
						_result.getContent(),
						_result.getType(),
						_result.getPriority());
				break;
			case DEADLINE:
				task = new Task(taskID,
						_result.getContent(),
						_result.getType(),
						_result.getEndDate(),
						_result.getPriority());
				break;
			case EVENT:
				task = new Task(taskID,
						_result.getContent(),
						_result.getType(),
						_result.getStartDate(),
						_result.getEndDate(),
						_result.getPriority());
				break;
			case INVALID: default:
				task = null;
		}
		
		if (task != null) {
			_taskList.put(taskID, task);
			Storage.writeTasks(_taskList);
			message = String.format(FeedbackHelper.MSG_ADD, _result.getContent());
			isSuccessful = true;
		}
		
		ArrayList<Task> displayList = 
				new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}
}
```
###### src\logic\CommandDefault.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Task;

public class CommandDefault implements ICommand {
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandDefault(ConcurrentSkipListMap<Integer, Task> taskList) {
		_taskList = taskList;
	}

	public Feedback execute() {
		ArrayList<Task> displayList = new ArrayList<Task>(_taskList.values());
		String message = FeedbackHelper.ERROR_INVALID_COMMAND;
		
		return new Feedback(message, displayList, _taskList, false);
	}
}
```
###### src\logic\CommandDelete.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;

/**
 * CommandDelete executes delete function.
 * @author 
 *
 */
public class CommandDelete implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandDelete(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	/**
	 * Delete task.
	 */
	public Feedback execute() {
		int taskID = _result.getStorageID();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_NO_INDEX;
		
		if (_taskList.containsKey(taskID)) {
			String taskName = _taskList.get(taskID).getTaskName();
			_taskList.remove(taskID);
			Storage.writeTasks(_taskList);
			message = String.format(FeedbackHelper.MSG_DELETE, taskName);
			isSuccessful = true;
		}
		
		ArrayList<Task> displayList = 
				new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}
}
```
###### src\logic\CommandDoneUndone.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;

public class CommandDoneUndone implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	private boolean _isDone = false;
	
	public CommandDoneUndone(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList,
			boolean isDone) {
		_result = result;
		_taskList = taskList;
		_isDone = isDone;
	}

	public Feedback execute() {
		int taskID = _result.getStorageID();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_NO_INDEX;
		
		if (_taskList.containsKey(taskID)) {
			Task task = _taskList.get(taskID);
			
			if (_isDone) {
				task.setCompleted(true);
				message = String.format(FeedbackHelper.MSG_DONE, task.getTaskName());
				isSuccessful = true;
			} else {
				task.setCompleted(false);
				message = String.format(FeedbackHelper.MSG_UNDONE, task.getTaskName());
				isSuccessful = true;
			}
			
			_taskList.put(taskID, task);
			Storage.writeTasks(_taskList);
		}
		
		ArrayList<Task> displayList = new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}
}
```
###### src\logic\CommandEdit.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;

/**
 * CommandEdit class executes edit function.
 * @author 
 *
 */
public class CommandEdit implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandEdit(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	/**
	 * Edit task.
	 */
	public Feedback execute() {
		int taskID = _result.getStorageID();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_NO_INDEX;
		
		if (_taskList.containsKey(taskID)) {
			Task task = _taskList.get(taskID);
			
			task.setTaskName(_result.getContent());
			task.setTaskType(_result.getType());
			task.setStartDate(_result.getStartDate());
			task.setEndDate(_result.getEndDate());
			
			_taskList.put(taskID, task);
			Storage.writeTasks(_taskList);
			
			message = String.format(FeedbackHelper.MSG_EDIT, _result.getContent());
			isSuccessful = true;
		}
		
		ArrayList<Task> displayList = new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
		
	}
}
```
###### src\logic\CommandHelp.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Task;

public class CommandHelp implements ICommand {
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandHelp(ConcurrentSkipListMap<Integer, Task> taskList) {
		_taskList = taskList;
	}
	
	public Feedback execute() {
		ArrayList<Task> displayList = new ArrayList<Task>(_taskList.values());
		String message = FeedbackHelper.MSG_HELP;
		
		return new Feedback(message, displayList, _taskList, true);
	}
}
```
###### src\logic\CommandHome.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Task;

public class CommandHome implements ICommand {
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandHome(ConcurrentSkipListMap<Integer, Task> taskList) {
		_taskList = taskList;
	}
	public Feedback execute() {
		ArrayList<Task> displayList = new ArrayList<Task>(_taskList.values());
		String message = FeedbackHelper.MSG_HOME;
		
		return new Feedback(message, displayList, _taskList, true);
	}

}
```
###### src\logic\CommandMove.java
``` java
package logic;

import object.Result;

public class CommandMove implements ICommand {

	
	
	public CommandMove(Result result) {
		// TODO Auto-generated constructor stub
	}

	public Feedback execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
```
###### src\logic\CommandSearch.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;

/**
 * CommandSearch class executes search function.
 * @author 
 *
 */
public class CommandSearch implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandSearch(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	/**
	 * Search tasks that contain the search key.
	 */
	public Feedback execute() {
		String searchKey = _result.getContent();
		String[] tokenisedKey = searchKey.split(" ");
		ArrayList<Task> taskList = new ArrayList<Task>(_taskList.values());
		ArrayList<Task> displayList = new ArrayList<Task>();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_SEARCH;
		
		if (!searchKey.equals(null) && !searchKey.equals("")) {
			for (int i = 0; i < taskList.size(); i++) {
				Task task = taskList.get(i);
				String taskName = task.getTaskName().toLowerCase();

				for (int j = 0; j < tokenisedKey.length; j++) {
					String key = tokenisedKey[j].toLowerCase();
					
					if (!taskName.contains(key)) {
						break;
					}
					
					if (j == (tokenisedKey.length-1)) {
						displayList.add(task);
					}
				}
			}
		}
		
		if (displayList.size() == 1) {
			message = String.format(FeedbackHelper.MSG_SEARCH, 1, "");
			isSuccessful = true;
		} else if (!displayList.isEmpty()) {
			message = String.format(FeedbackHelper.MSG_SEARCH, displayList.size(), "s");
			isSuccessful = true;
		}
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}
}
```
###### src\logic\CommandShow.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import type.ShowType;

public class CommandShow implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandShow(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	public Feedback execute() {
		int size = 0;
		String requestType = _result.getContent();
		ShowType type = ShowType.toType(requestType.toUpperCase());
		ArrayList<Task> displayList = new ArrayList<Task>();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_SHOW_NO_ALL;
		
		switch (type) {
			case ALL: 
				displayList = new ArrayList<Task>(_taskList.values());
				size = displayList.size();

				if (size > 0) {
					isSuccessful = true;
					if (size > 1) {
						message = String.format(FeedbackHelper.MSG_SHOW_ALL, "s");
					} else {
						message = String.format(FeedbackHelper.MSG_SHOW_ALL, "");;
					}
				}
				break;
			case DONE:
				displayList = searchTasks(true);
				size = displayList.size();
				
				if (size > 0) {
					isSuccessful = true;
					if (size > 1) {
						message = String.format(FeedbackHelper.MSG_SHOW_DONE, size, "s");
					} else {
						message = String.format(FeedbackHelper.MSG_SHOW_DONE, size, "");
					}
				} else {
					message = FeedbackHelper.ERROR_SHOW_NO_DONE;
				}
				
				break;
			case UNDONE: case INVALID: default:
				displayList = searchTasks(false);
				size = displayList.size();
				
				if (size > 0) {
					isSuccessful = true;
					if (size > 1) {
						message = String.format(FeedbackHelper.MSG_SHOW_UNDONE, size, "s");
					} else {
						message = String.format(FeedbackHelper.MSG_SHOW_UNDONE, size, "");
					}
				} else {
					message = FeedbackHelper.ERROR_SHOW_NO_UNDONE;
				}
		}
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
	}
	
	private ArrayList<Task> searchTasks(boolean isCompleted) {
		ArrayList<Task> taskList = new ArrayList<Task>(_taskList.values());
		ArrayList<Task> searchTasks = new ArrayList<Task>();
		
		for (int i = 0; i < taskList.size(); i++) {
			Task task = taskList.get(i);
			boolean isDone = task.getCompleted();
			
			if (isDone == isCompleted) {
				searchTasks.add(task);
			}
		}
		return searchTasks;
	}
	
	

}
```
###### src\logic\Feedback.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Task;

/**
 * Feedback class stores the feedback details after the execution of 
 * the command.
 * @author 
 *
 */
public class Feedback {
	private String _message = null;
	private boolean _isSuccessful = false;
	private static ArrayList<Task> _displayList = 
			new ArrayList<Task>();
	private static ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public Feedback(String message, 
			ArrayList<Task> displayList,
			ConcurrentSkipListMap<Integer, Task> updatedList,
			boolean isSuccessful) {
		_message = message;
		_displayList = displayList;
		_taskList = updatedList;
		_isSuccessful = isSuccessful;
	}

	public String getMessage() {
		return _message;
	}
	
	public ArrayList<Task> getDisplayList() {
		return _displayList;
	}
	
	public ConcurrentSkipListMap<Integer, Task> getUpdatedTaskList() {
		return _taskList;
	}
	
	public boolean isSuccessful() {
		return _isSuccessful;
	}
}
```
###### src\logic\FeedbackHelper.java
``` java
package logic;

public class FeedbackHelper {
	public static final String MSG_ADD = "Feedback: %1$s has been successfully added!";
	public static final String MSG_CAT = "Feedback: Category %1$s has been successfully added!";
	public static final String MSG_EDIT = "Feedback: %1$s has been modified!";
	public static final String MSG_DELETE = "Feedback: %1$s has been deleted!";
	public static final String MSG_SEARCH = "Feedback: Return %1$s result%2$s!";
	public static final String MSG_SHOW_ALL = "Feedback: Show all task%1$s!";
	public static final String MSG_SHOW_DONE = "Feedback: Show %1$s completed task%2$s!";
	public static final String MSG_SHOW_UNDONE = "Feedback: Show %1$s incomplete task%2$s!";
	public static final String MSG_DONE = "Feedback: %1$s has been marked as completed!";
	public static final String MSG_UNDONE = "Feedback: %1$s has been marked as incomplete!";
	public static final String MSG_HELP = "Feedback: Show HELP!";
	public static final String MSG_HOME = "Feedback: Show home page!";
	
	public static final String ERROR_ADD = "Feedback: %1$s cannot be added!";
	public static final String ERROR_CAT = "Feedback: Category %1$s cannot be added!";
	public static final String ERROR_NO_INDEX = "Feedback: The entered number does not exist!";
	public static final String ERROR_SEARCH = "Feedback: No results found!";
	public static final String ERROR_EDIT = "Feedback: %1$s cannot be modified!";
	public static final String ERROR_SHOW_NO_ALL = "Feedback: No task!";
	public static final String ERROR_SHOW_NO_DONE = "Feedback: No completed task!";
	public static final String ERROR_SHOW_NO_UNDONE = "Feedback: No incomplete task!";
	public static final String ERROR_NO_ACTION = "Feedback: No action is done!";
	public static final String ERROR_INVALID_COMMAND = "Feedback: The command entered does not exist!";
}
```
###### src\logic\ICommand.java
``` java
package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Category;
import object.Result;
import object.Task;
import type.CommandType;

public interface ICommand {
	/**
	 * Get respective class according to the command.
	 * @param result - result parsed by Parser
	 * @param taskList - task list
	 * @param _categoryList 
	 * @return class to execute
	 */
	public static ICommand getCommand(Result result, 
			ConcurrentSkipListMap<Integer, Task> taskList, 
			ArrayList<Category> categoryList) {
		CommandType commandType = result.getCmd();
		
		switch (commandType) {
			case ADD:
				return new CommandAdd(result, taskList);
			case DELETE:
				return new CommandDelete(result, taskList);
			case EDIT:
				return new CommandEdit(result, taskList);
			case DONE:
				return new CommandDoneUndone(result, taskList, true);
			case UNDONE:
				return new CommandDoneUndone(result, taskList, false);
			case SEARCH:
				return new CommandSearch(result, taskList);
			case SHOW:
				return new CommandShow(result, taskList);
			case HELP:
				return new CommandHelp(taskList);
			case MOVE:
				return new CommandMove(result);
			case HOME:
				return new CommandHome(taskList);
			case INVALID: default:
				return new CommandDefault(taskList);
		}
	}
	
	/**
	 * Execute the command.
	 * @return feedback
	 */
	public Feedback execute();
}
```
###### src\logic\LogicController.java
``` java
package logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;

import history.History;
import object.Category;
import object.Result;
import object.Task;
import parser.Parser;
import storage.Storage;
import type.CommandType;
import type.TaskType;

public class LogicController {
	private static final String ERROR_NO_UNDO = "Feedback: No undo operation!";
	private static final String ERROR_NO_REDO = "Feedback: No redo operation!";
	
	private static final int NON_FLOATING_DISPLAY_SIZE = 5;
	private static CommandType _currentCmd = CommandType.INVALID;
	
	private static ArrayList<Task> _displayList = 
			new ArrayList<Task>();
	private static ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	private static ArrayList<Category> _categoryList =
			new ArrayList<Category>();
	/**
	 * Initialize logic class.
	 */
	static {
		_taskList = Storage.readTasks();
		_displayList = new ArrayList<Task>(_taskList.values());
		_displayList = setDisplayList(false);
	}
	
	/**
	 * Process the command.
	 * @param input - command to be executed
	 * @param displayList - task list that is being displayed
	 * @return feedback message
	 */
	public static String process(String input, ArrayList<Task> displayList) {
		// Set up the parsing result
		Result result = Parser.parse(input, displayList);
		CommandType cmd = result.getCmd();
		Task befExeTask = null;
		int resultStorageID = result.getStorageID();
		
		if (result.getStorageID() != -1) {
			befExeTask = _taskList.get(resultStorageID);
		}
		
		// Execute parsing result
		Feedback feedback = execute(result);
		String message = feedback.getMessage();
		
		// Initialize current command
		_currentCmd = CommandType.INVALID;
		
		// Store successful execution
		if (feedback.isSuccessful()) {
			_currentCmd = result.getCmd();
			Task aftExeTask = _taskList.get(resultStorageID);
			recordExecution(cmd, result.getContent(), befExeTask, aftExeTask);
		}
		
		return message;
	}
	
	/**
	 * Undo the previous operations.
	 * @return feedback message
	 */
	public static String undo() {
		Result result = History.undo();
		if (result != null) {
			Feedback feedback = execute(result);
			return feedback.getMessage();
		} else {
			return ERROR_NO_UNDO;
		}
	}
	
	/**
	 * Redo the operations.
	 * @return feedback message
	 */
	public static String redo() {
		Result result = History.redo();
		if (result != null) {
			Feedback feedback = execute(result);
			return feedback.getMessage();
		} else {
			return ERROR_NO_REDO;
		}
	}
	
	/**
	 * Execute the input according to the parsing result.
	 * @param result - parsing result
	 * @return feedback
	 */
	private static Feedback execute(Result result) {
		ICommand command = ICommand.getCommand(result, _taskList,_categoryList);
		Feedback feedback = command.execute();
		
		_taskList = feedback.getUpdatedTaskList();
		_displayList = feedback.getDisplayList();
		_displayList = setDisplayList(result.getCmd() == CommandType.SHOW);
		return feedback;
	}

	/**
	 * Record the execution for undo and redo purposes
	 * @param cmd - command type
	 * @param content - content of the input
	 * @param befExeTask - task before execution
	 * @param aftExeTask - task after execution
	 */
	private static void recordExecution(CommandType cmd, String content,
			Task befExeTask, Task aftExeTask) {
		switch(cmd) {
			case ADD:
				int index = _taskList.lastKey();
				Task newTask = _taskList.get(index);
				History.push(cmd, newTask);
				break;
			case DELETE:
				History.push(cmd, befExeTask);
				break;
			case EDIT:
				History.push(cmd, befExeTask, aftExeTask);
				break;
			case DONE: case UNDONE:
				History.push(cmd, aftExeTask);
				break;
			default:
				History.push(cmd, content);
		}
	}
	
	/**
	 * Remove done task(s) for display purpose except for command "show".
	 * @param isShowCommand - true if the command is "show"; false otherwise
	 * @return display task list
	 */
	private static ArrayList<Task> setDisplayList(boolean isShowCommand) {
		ArrayList<Task> displayList = new ArrayList<Task>();
		if (isShowCommand) {
			displayList = _displayList;
		} else {
			for (int i = 0; i < _displayList.size(); i++) {
				Task task = _displayList.get(i);
				if (!task.getCompleted()) {
					displayList.add(task);
				}
			}
		}
		return displayList;
	}
	
	/**
	 * Get the task list in ConcurrentSkipListMap<Integer, Task> form.
	 * @return tasklist
	 */
	public static ConcurrentSkipListMap<Integer, Task> getTaskList() {
		return _taskList;
	}
	
	/**
	 * Get the task list for display.
	 * @return display task list
	 */
	public static ArrayList<Task> getDisplayList() {
		Collections.sort(_displayList, taskComparator);
		return _displayList;
	}
	
	/**
	 * Get the floating task list for display.
	 * @return display floating task list
	 */
	public static ArrayList<Task> getFloatingList() {
		ArrayList<Task> floatingList = new ArrayList<Task>();
		
		for (int i = 0; i < _displayList.size(); i++) {
			Task task = _displayList.get(i);
			if (task.getTaskType() == TaskType.FLOATING) {
				floatingList.add(task);
			}
		}
		return floatingList;
	}
	
	/**
	 * Get the non-floating task list for display.
	 * @return display non-floating task list
	 */
	public static ArrayList<Task> getNonFloatingList() {
		ArrayList<Task> nonFloatingList = new ArrayList<Task>();
		
		for (int i = 0; i < _displayList.size(); i++) {
			Task task = _displayList.get(i);
			if (task.getTaskType() != TaskType.FLOATING) {
				nonFloatingList.add(task);
			}
		}
		
		Collections.sort(nonFloatingList, taskComparator);
		
		// Show today's tasks.
		// If today's tasks are fewer than 5, add up to 5 tasks.
		if (_currentCmd != CommandType.SHOW) {
			ArrayList<Task>	displayList = new ArrayList<Task>();
			Calendar todayCalendar = Calendar.getInstance();
			
			// Initialize today
			todayCalendar.set(Calendar.HOUR_OF_DAY, 23);
			todayCalendar.set(Calendar.MINUTE, 59);
			todayCalendar.set(Calendar.SECOND, 59);
			Date today = todayCalendar.getTime();
			
			for (Task task: nonFloatingList) {
				Date startDate = task.getStartDate();
				Date endDate = task.getEndDate();
				int displaySize = displayList.size();
				boolean isAdded = false;
				
				if (startDate != null) {		// Compare start date
					if (startDate.compareTo(today) <= 0) {
						displayList.add(task);
						isAdded = true;
					}
				} else if (endDate != null) {	// Compare end date
					if (endDate.compareTo(today) <= 0) {
						displayList.add(task);
						isAdded = true;
					}
				} 
				
				if (!isAdded) {
					if (displaySize < NON_FLOATING_DISPLAY_SIZE) {
						displayList.add(task);
					} else {
						break;
					}
				}
			}
			
			return displayList;
		} 
		
		return nonFloatingList;
	}
	
	private static Comparator<Task> taskComparator = new Comparator<Task>() {
		@Override
		/**
		 * Overriding comparator to compare by start/end dates.
		 * 
		 * Custom comparator will set tasks with dates allocated 
		 * (deadlines etc) based on chronological order first,
		 * followed by floating tasks (in alphabetical order).
		 */
		public int compare(Task t1, Task t2) {

			if (t1.getEndDate() != null && t2.getEndDate() != null) {
				return t1.getEndDate().compareTo(t2.getEndDate());
			} else if ((t1.getStartDate() == null && 
					t1.getEndDate() != null && t2.getStartDate() != null)
					|| (t1.getEndDate() != null && t2.getEndDate() == null && t2.getStartDate() != null)) {
				return t1.getEndDate().compareTo(t2.getStartDate());
			} else if ((t1.getStartDate() != null && t2.getStartDate() == null && t2.getEndDate() != null)
					|| (t1.getEndDate() == null && t1.getStartDate() != null && t2.getEndDate() != null)) {
				return t1.getStartDate().compareTo(t2.getEndDate());
			} else if (t1.getStartDate() != null && t2.getStartDate() != null) {
				return t1.getStartDate().compareTo(t2.getStartDate());
			} else if (t1.getStartDate() == null && t1.getEndDate() == null && t2.getStartDate() != null
					|| t2.getEndDate() != null) {
				return 1;
			} else if (t1.getStartDate() != null || t1.getEndDate() != null && t2.getStartDate() == null
					&& t2.getEndDate() == null) {
				return -1;
			} else if (t1.getStartDate() == null && t1.getEndDate() == null && t2.getStartDate() == null
					&& t2.getEndDate() == null && t1.getTaskName() != null && t2.getTaskName() != null) {
				return t1.getTaskName().compareTo(t2.getTaskName());
			}

			return 0;
		}
	};
	
	// For testing purpose
/*	public static void main(String[] args) {
		System.out.print("Command: ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		scanner.close();
		
		String feedback = LogicController.process(input, _displayList);
		System.out.println(feedback);
		ArrayList<Task> displayList = LogicController.getDisplayList();
		
		for (int i = 0; i < displayList.size(); i++) {
			System.out.println((i+1) + ". " + displayList.get(i).getTaskName());
		}
	}*/
}
```
###### src\object\Category.java
``` java
package object;

public class Category {

	private String _category = null;

	public Category(String content) {
		this._category = content;
	}

	public String getCategory() {
		return _category;
	}

	public void setCategory(String category) {
		this._category = category;
	}	
}
```
###### src\object\Result.java
``` java
package object;

import java.util.Date;

import type.CommandType;
import type.TaskType;

public class Result {
	private int _storageID = -1;
	private int _priority = -1;
	private CommandType _cmd = null;
	private TaskType _type = null;
	private String _content = null;
	private Date _startDate = null;
	private Date _endDate = null;
	
	public Result() {
		
	}
	
	public Result(int storageID, CommandType cmd, String content, 
			TaskType type, Date startDate, Date endDate) {
		this(content, startDate, endDate);
		_storageID = storageID;
		_cmd = cmd;
		_type = type;
	}
	
	public Result(String content, Date startDate, Date endDate) {
		_content = content;
		_startDate = startDate;
		_endDate = endDate;
	}
	
	public Result(CommandType cmd, Task task) {
		this(task.getTaskID(), cmd, task.getTaskName(),
				task.getTaskType(), task.getStartDate(), task.getEndDate());
		_priority = task.getPriority();
	}
	
	/**
	 * Get storage ID.
	 * @return storage ID
	 */
	public int getStorageID() {
		return _storageID;
	}
	
	/**
	 * Get command.
	 * @return command
	 */
	public CommandType getCmd() {
		return _cmd;
	}
	
	/**
	 * Set command.
	 * @param cmd
	 */
	public void setCommand(CommandType cmd) {
		_cmd = cmd;
	}
	
	/**
	 * Get content.
	 * @return title
	 */
	public String getContent() {
		return _content;
	}
	
	/**
	 * Set content. 
	 * @param content
	 */
	public void setContent(String content) {
		_content = content;
	}
	
	/**
	 * Get the type whether it is event, deadline or floating task.
	 * @return type 
	 */
	public TaskType getType() {
		return _type;
	}
	
	/**
	 * Get the start date and time of the event or deadline.
	 * @return startDate
	 */
	public Date getStartDate() {
		return _startDate;
	}
	
	/**
	 * Get the end date and time of the event or deadline.
	 * @return endDate
	 */
	public Date getEndDate() {
		return _endDate;
	}
	
	/**
	 * Set the start date and time of the event or deadline.
	 */
	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}
	
	/**
	 * Set the start date and time of the event or deadline.
	 */
	public void setEndDate(Date endDate) {
		_endDate = endDate;
	}
	
	/**
	 * Get priority of the task.
	 * @return priority
	 */
	public int getPriority() {
		return _priority;
	}
}
```
###### tests\logic\LogicControllerTest.java
``` java
	@Test
	public void testComparator() {
		
		logger.info("Start of comparator tests");

		//t1 has end no start; t2 has end no start.
		LogicController.process("add 2103 Tutorial homework by today", LogicController.getDisplayList());
		LogicController.process("add 2334 revision by next Sunday", LogicController.getDisplayList());
		
		//t1 has end no start; t2 has start no end.
		LogicController.process("add 3230 Homework by Friday", LogicController.getDisplayList());
		LogicController.process("add 2101 Presentation on Friday", LogicController.getDisplayList());
		
		//t1 has end no start, t2 has neither.
		LogicController.process("add Finish revision by next Sunday", LogicController.getDisplayList());
		LogicController.process("add Learn make-up", LogicController.getDisplayList());
		
		//t1 has end no start, t2 has start and end.
		LogicController.process("add Stock up on drinks by 08/11", LogicController.getDisplayList());
		LogicController.process("add Conduct online course from Friday to Sunday", LogicController.getDisplayList());

		//t1 has start no end, t2 has end no start.
		LogicController.process("add Dinner with parents on 08/11", LogicController.getDisplayList());
		LogicController.process("add 2103 Website by next Sunday", LogicController.getDisplayList());
		
		//t1 has start no end, t2 has start no end.
		LogicController.process("add Watch documentary about anime at 07/11", LogicController.getDisplayList());
		LogicController.process("add Facial appointment on Sunday", LogicController.getDisplayList());

		//t1 has start no end, t2 has end and start.
		LogicController.process("add Cousin's Wedding on next Sunday", LogicController.getDisplayList());
		LogicController.process("add Finals from 24/11 to 03/12", LogicController.getDisplayList());

		//t1 has start no end, t2 has neither.
		LogicController.process("add Shop for clothes on 20/11", LogicController.getDisplayList());
		LogicController.process("add Learning Japanese", LogicController.getDisplayList());

		//t1 has both, t2 has start no end.
		LogicController.process("add 2103 Project Finalisation from Wednesday to Sunday", LogicController.getDisplayList());
		LogicController.process("add 3235 Steps preparation at Thursday", LogicController.getDisplayList());
		
		//t1 has both, t2 has end no start.
		LogicController.process("add 2334 tutorials from Thursday to Saturday", LogicController.getDisplayList());
		LogicController.process("add Do supermarket shopping by Monday", LogicController.getDisplayList());
		
		//t1 has both, t2 has neither.
		LogicController.process("add 2103 Handout and Slides from Monday to Wednesday", LogicController.getDisplayList());
		LogicController.process("add Prepare for cosplay", LogicController.getDisplayList());
		
		//t1, t2 have both.
		LogicController.process("add AFA from 28/11 to 29/11", LogicController.getDisplayList());
		LogicController.process("add 7Steps from 18/11 6pm to 18/11 10pm", LogicController.getDisplayList());
		
		//t1 has neither, t2 has end no start.
		LogicController.process("add Exercise", LogicController.getDisplayList());
		LogicController.process("add Complete 2334 webcast in 4 days", LogicController.getDisplayList());
				
		//t1 has neither, t2 has start no end.
		LogicController.process("add Reorganise nendoroids", LogicController.getDisplayList());
		LogicController.process("add 2101 Presentation on Saturday", LogicController.getDisplayList());
		
		//t1 has neither, t2 has both.
		LogicController.process("add Enjoy Life", LogicController.getDisplayList());
		LogicController.process("add Holiday from 25/12 to 30/12", LogicController.getDisplayList());
						
		//t1, t2 have neither.
		LogicController.process("add Clean up room", LogicController.getDisplayList());
		LogicController.process("add Pack wardrobe", LogicController.getDisplayList());
		
		int length = LogicController.getDisplayList().size();
		for (int i = 0; i < length; i++) {
			LogicController.process("delete 1", LogicController.getDisplayList());
		}

		logger.info("End of comparator tests");
	}
	
}


```

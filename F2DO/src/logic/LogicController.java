package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;

import date.DateTime;
import history.History;
import object.Result;
import object.Task;
import parser.Parser;
import storage.Storage;
import type.CommandType;
import type.TaskType;

//@@author A0118005W
public class LogicController {
	private static final String ERROR_NO_UNDO = "Feedback: No undo operation!";
	private static final String ERROR_NO_REDO = "Feedback: No redo operation!";
	
	//private static final int NON_FLOATING_DISPLAY_SIZE = 5;
	private static CommandType _currentCmd = CommandType.INVALID;
	private static int _nonFloatingDisplaySize = 5;
	
	private static ArrayList<Task> _displayList = 
			new ArrayList<Task>();
	private static ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
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
		CommandType cmd = result.getCommand();
		Task befExeTask = null;
		int resultStorageID = result.getStorageID();
		String message = null;
		
		if (result.isError()) {
			message = result.getErrorMsg();
		} else {
			if (result.getStorageID() != -1) {
				befExeTask = _taskList.get(resultStorageID);
			}

			// Execute parsing result
			Feedback feedback = execute(result);
			message = feedback.getMessage();

			// Initialize current command
			_currentCmd = CommandType.INVALID;

			// Store successful execution
			if (feedback.isSuccessful()) {
				_currentCmd = result.getCommand();
				Task aftExeTask = _taskList.get(resultStorageID);
				recordExecution(cmd, result.getContent(), befExeTask, aftExeTask);
			}
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
		ICommand command = ICommand.getCommand(result, _taskList);
		Feedback feedback = command.execute();
		
		_taskList = feedback.getUpdatedTaskList();
		_displayList = feedback.getDisplayList();
		_displayList = setDisplayList(result.getCommand() == CommandType.SHOW);
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
			Date today = DateTime.getToday();
			
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
					if (displaySize < _nonFloatingDisplaySize) {
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
	
	public static void setNonFloatingDisplaySize(int size) {
		_nonFloatingDisplaySize = size;
	}
	
	//@@author A0108511U
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
}

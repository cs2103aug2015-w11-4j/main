package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Task;
import parser.Parser;
import parser.Result;
import storage.Storage;

public class LogicController {
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
	}
	
	/**
	 * Process the command.
	 * @param input - command to be executed
	 * @param displayList - task list that is being displayed
	 * @return feedback message
	 */
	public static String process(String input, ArrayList<Task> displayList) {
		Result result = Parser.parse(input, displayList);
		ICommand command = ICommand.getCommand(result, _taskList);
		Feedback feedback = command.execute();
		String message = feedback.getMessage();
		
		_taskList = feedback.getUpdatedTaskList();
		_displayList = feedback.getDisplayList();
		
		return message;
	}
	
	/**
	 * Get the task list for display.
	 * @return display task list
	 */
	public static ArrayList<Task> getDisplayList() {
		Collections.sort(_displayList, taskComparator);
		return _displayList;
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
	public static void main(String[] args) {
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
	}
}

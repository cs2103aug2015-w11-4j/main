package storage;

import static org.junit.Assert.*;

import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.*;

import objects.Task;

@SuppressWarnings("serial")
public class Storage implements Serializable {
	
	private static final String DEFAULT_DIRECTORY = "F2DO";
	private static final String FILENAME = "F2DO.json";
	private static final String SAVED_DIRECTORY = "%s\\F2DO";
	private static final String CHANGE_DIRECTORY = "user.dir";
	
	//private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static ConcurrentSkipListMap<Integer, Task> _taskList = new ConcurrentSkipListMap<Integer, Task>();
	private static String saveFolder;
	protected static String filePath;
	@SuppressWarnings("unused")
	private static Logger Logger = java.util.logging.Logger.getLogger("Storage");
	
	// Initialize storage class
	static {
		saveFolder = DEFAULT_DIRECTORY;
		filePath = DEFAULT_DIRECTORY + "/" + FILENAME;
		createSaveDir(DEFAULT_DIRECTORY);
		createSaveFile();
		//taskList = StorageHelper.readFromjsonFile();
		_taskList = StorageHelper.readJsonFile();
		//readFromFile();
	}
		
	public Storage(String directory) {
		saveFolder = String.format(SAVED_DIRECTORY, directory);
		createSaveDir(saveFolder);
	}
	
	public Storage() {
		saveFolder = DEFAULT_DIRECTORY;
		createSaveDir(DEFAULT_DIRECTORY);
	}
	
	public static ArrayList<Task> getTaskList() {
		//return taskList;
		ArrayList<Task> taskList = new ArrayList<Task>(_taskList.values());
		return taskList;
	}
	
	public static String getFilePath() {
		return filePath;
	}
	
	private static void createSaveDir(String directory) {
		File dir = new File(directory);
		
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		System.setProperty(CHANGE_DIRECTORY, directory);
		createSaveFile();
	}
	
	private static void createSaveFile() {
		File file = new File(filePath);
		if (!file.exists()) {
			StorageHelper.createJsonFile();
		}
	}
	
	// Takes in task object created by logic and adds it to ArrayList
	public static boolean addTask(Task newTask) {
		assertNotNull(newTask);
		//taskList.add(newTask);
		
		try {
			if (_taskList.isEmpty()) {
				_taskList.put(1, newTask);
			} else {
				int lastIndex = _taskList.lastKey();
				_taskList.put(lastIndex + 1, newTask);
			}
			//assertTrue(sortTaskList());
			return saveToFile();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Deletes the task object located at the index supplied by the user
	public static boolean deleteTask(int taskNumber) {
		//taskList.remove(taskNumber);
		_taskList.remove(taskNumber);
		
		//assertTrue(saveToFile(taskList));
		try {
			//assertTrue(saveToFile());
			return saveToFile();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Updates the desired task with the new information
	public static boolean updateTask(int taskNumber, String newTitle, 
			               Date newStartDate, Date newEndDate) {
		
		try {
			Task task = _taskList.get(taskNumber);
			task.setTaskName(newTitle);
			task.setStartDate(newStartDate);
			task.setEndDate(newEndDate);

			//assertTrue(saveToFile(taskList));
			//assertTrue(saveToFile());
			
			_taskList.put(taskNumber, task);
			
			return saveToFile();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean updateTask(int taskNumber, Task task) {

		try {
			_taskList.put(taskNumber, task);

			//assertTrue(saveToFile(taskList));
			//assertTrue(saveToFile());
			return saveToFile();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
		
	// Checks if the file contains any data.
	private static boolean isEmptyFile() {
		FileReader fr = null;
		
		try {
			fr = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			if (fr.read() == -1) {
				return true;
			}
		} catch (IOException e) {
			return true;
		}
		return false;
	}
	
	/*public static boolean saveToFile(ArrayList<Task> taskList) {		
		boolean isSaveSuccess = true;		

		ArrayList<JSONObject> jsonList = StorageHelper.jsonList(taskList);		
		assertTrue(StorageHelper.saveTojsonFile(jsonList));		
		StorageHelper.readFromjsonFile();		

		return isSaveSuccess;	
	}*/
	
	private static boolean saveToFile() {
		try {
			StorageHelper.writeJsonFile(_taskList);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	// Testing driver to show existing saved tasks
	public static void displayTaskList() {
		ArrayList<Task> taskList = new ArrayList<Task>(_taskList.values());
		for (int i = 0; i < taskList.size(); i++) {
			
			System.out.println("LOL "+ taskList.size() + " int " + i);
			System.out.println("Task ID: " + taskList.get(i).getTaskID() + 
					"\nTask Name: " + taskList.get(i).getTaskName() + 
					"\nStart Date: " + taskList.get(i).getStartDate() + 
					"\nEnd Date: " + taskList.get(i).getEndDate());
		}
	}
	
	// Organizes the tasks to optimize displaying
	public static boolean sortTaskList(ArrayList<Task> taskList) {
		Collections.sort(taskList, taskComparator);
		//assertTrue(saveToFile(taskList));
		return true;
	}

	/* Overriding comparator to compare by start/end dates.
	 * Custom comparator will set tasks with dates allocated (deadlines etc) based on chronological order first,
	 * followed by floating tasks (in alphabetical order).
	 */
	public static Comparator<Task> taskComparator = new Comparator<Task>() {
	    
        @Override
		public int compare(Task t1, Task t2) {
			
        	if (t1.getEndDate() != null && t2.getEndDate() != null) {
				return t1.getEndDate().compareTo(t2.getEndDate());
			} else if ((t1.getStartDate() == null && t1.getEndDate() != null && t2.getStartDate() != null)
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
	
	public static void main(String[] args) {		
		saveFolder = DEFAULT_DIRECTORY;
		createSaveDir(DEFAULT_DIRECTORY);
		createSaveFile();
		//taskList.clear();
		_taskList.clear();
		
		if (!(isEmptyFile())) {
			StorageHelper.readJsonFile();
		}
		
		displayTaskList();

		/*if (sortTaskList()) {
			System.out.println("\n\n");
			displayTaskList();
		}*/
	}
}
package storage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import objects.Task;

@SuppressWarnings("serial")
public class Storage implements Serializable {
	
	private static final String DEFAULT_DIRECTORY = "F2DO";
	private static final String FILENAME = "F2DO.xml";
	private static final String SAVED_DIRECTORY = "%s\\F2DO";
	private static final String CHANGE_DIRECTORY = "user.dir";
	
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static String saveFolder;
	private static String filePath;
	
	// Initialize storage class
	static {
		saveFolder = DEFAULT_DIRECTORY;
		filePath = DEFAULT_DIRECTORY + "/" + FILENAME;
		createSaveDir(DEFAULT_DIRECTORY);
		readFromFile();
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
		return taskList;
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
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Takes in task object created by parser and adds it to ArrayList
	public static boolean addTask(Task newTask) {
		nullRemovalCheck();
		taskList.add(newTask);
		
		if (saveToFile()) {
			return true;
		}
		
		return false;
	}
	
	// Deletes the task object located at the index supplied by the user
	public static boolean deleteTask(int taskNumber) {
		taskList.remove(taskNumber);
		nullRemovalCheck();
		
		if (saveToFile()) {
			return true;
		}
		
		return false;
	}
	
	// Updates the desired task with the new information
	public static boolean updateTask(int taskNumber, String newTitle, 
			               Date newStartDate, Date newEndDate) {
		nullRemovalCheck();
		taskList.get(taskNumber).setTaskName(newTitle);
		taskList.get(taskNumber).setStartDate(newStartDate);
		taskList.get(taskNumber).setEndDate(newEndDate);
		
		if (saveToFile()) {
			return true;
		}
		
		return false;
	}
	
	// Saves the ArrayList into an XML file.
	public static boolean saveToFile() {
		boolean isSaveSuccess = true;
		nullRemovalCheck();
		XMLEncoder encoder = null;
		
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			encoder = new XMLEncoder(bos);
		} catch (FileNotFoundException e) {
			isSaveSuccess = false;
		}
		
		encoder.writeObject(taskList);
		encoder.close();
		
		return isSaveSuccess;
	}
	
	// Loads the ArrayList with data from the XML file.
	@SuppressWarnings("unchecked")
	private static boolean readFromFile() {

		if (isEmptyFile()) {
			return false;
		}
		
		boolean isReadSuccess = true;
		nullRemovalCheck();
		XMLDecoder decoder = null;
				
		try {
			FileInputStream fis = new FileInputStream(filePath);
			BufferedInputStream bis = new BufferedInputStream(fis);
			decoder = new XMLDecoder(bis);
		} catch (FileNotFoundException e) {
			isReadSuccess = false;
		}
		
		taskList = (ArrayList<Task>)decoder.readObject();
		
		return isReadSuccess;
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
	
	
	
	// Testing driver to show existing saved tasks
	public static void displayTaskList() {
		nullRemovalCheck();
		
		for (int i = 0; i < taskList.size(); i++) {
			System.out.println("Task ID: " + taskList.get(i).getTaskID() + 
					"\nTask Name: " + taskList.get(i).getTaskName() + 
					"\nStart Date: " + taskList.get(i).getStartDate() + 
					"\nEnd Date: " + taskList.get(i).getEndDate());
		}
	}
	
	// Organizes the tasks to optimize displaying
	public static boolean sortTaskList() {
		nullRemovalCheck();
		Collections.sort(taskList, taskComparator);

		if (saveToFile()) {
			return true;
		}
	
		return false;
	}

	// Overriding comparator to compare by start/end dates.
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
			}
        	
        	// If either t1 or t2 is a floating task, or both.
        	return t1.getTaskName().compareTo(t2.getTaskName());
		}
	};
	
	// Ensures that no empty tasks are in the ArrayList at any point in time.
	private static void nullRemovalCheck() {
		
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskName() == null || taskList.get(i).getTaskName().equals("")
					|| taskList.get(i).getTaskName().isEmpty()) {
				taskList.remove(i); 
				i--;
			}
		}
	}
	
	
	public static void main(String[] args) {		
		saveFolder = DEFAULT_DIRECTORY;
		createSaveDir(DEFAULT_DIRECTORY);
		createSaveFile();
		taskList.clear();
		
		if (!(isEmptyFile())) {
			readFromFile();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dateInput1 = "24/12/2015";
		
		Task testTask1 = new Task();
		taskList.add(testTask1);
		taskList.get(0).setTaskID(1);
		try {
			taskList.get(0).setStartDate(sdf.parse(dateInput1));
			taskList.get(0).setEndDate(sdf.parse(dateInput1));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		taskList.get(0).setTaskName("z testing Task 1");
		
		String dateInput2 = "23/12/2015";
		
		Task testTask2 = new Task();
		taskList.add(testTask2);
		taskList.get(1).setTaskID(2);
		try {
			taskList.get(1).setStartDate(sdf.parse(dateInput2));
			taskList.get(1).setEndDate(sdf.parse(dateInput2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		taskList.get(1).setTaskName("y testing Task 2");
		displayTaskList();

		if (sortTaskList()) {
			System.out.println("\n\n");
			displayTaskList();
		}
	
/*		if (deleteTask(1)) {
			System.out.println("\n\n");
			displayTaskList();
		}
*/
	}
}
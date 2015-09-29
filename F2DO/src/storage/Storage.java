package storage;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import objects.Task;

public abstract class Storage implements Serializable, Comparator<Task> {
	
    private static final String DEFAULT_DIRECTORY = "F2DO";
	private static final String FILENAME = "F2DO.txt";
	private static final String SAVED_DIRECTORY = "%s\\F2DO";
	private static final String CHANGE_DIRECTORY = "user.dir";
	
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private String saveFolder;
	
	public Storage(String directory) {
		saveFolder = String.format(SAVED_DIRECTORY, directory);
		createSaveDir(saveFolder);
	}
	
	public Storage() {
		saveFolder = DEFAULT_DIRECTORY;
		createSaveDir(DEFAULT_DIRECTORY);
	}
	
	private void createSaveDir(String directory) {
		File dir = new File(directory);
		
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		System.setProperty(CHANGE_DIRECTORY, directory);
		createSaveFile();
	}
	
	private static void createSaveFile() {
		File file = new File(FILENAME);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Takes in task object created by parser and adds it to ArrayList
	public static void addTask(Task newTask) {
		nullRemovalCheck();
		taskList.add(newTask);
		saveToFile();
	}
	
	// Deletes the task object located at the index supplied by the user
	public static void deleteTask(int taskNumber) {
		taskList.remove(taskNumber);
		nullRemovalCheck();
		
		saveToFile();
	}
	
	// Updates the desired task with the new information
	public static void updateTask(int taskNumber, String newTitle, 
			               Date newStartDate, Date newEndDate) {
		nullRemovalCheck();
		taskList.get(taskNumber).setTaskName(newTitle);
		taskList.get(taskNumber).setStartDate(newStartDate);
		taskList.get(taskNumber).setEndDate(newEndDate);
//		taskList.get(taskNumber).setCategory(newCategory);
		saveToFile();
	}
	
	private static void saveToFile() {
		nullRemovalCheck();
		
		try {
			FileOutputStream fout = new FileOutputStream(FILENAME);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(taskList);
			fout.flush();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readFromFile() {
		try {
			FileInputStream fin = new FileInputStream(FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fin);
			try {
				taskList = (ArrayList<Task>)ois.readObject();
			} catch (EOFException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			fin.close();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void displayTaskList() {
		nullRemovalCheck();
		
		for (int i = 0; i < taskList.size(); i++) {
			System.out.println("Task ID: " + taskList.get(i).getTaskID() + 
					"\nTask Name: " + taskList.get(i).getTaskName() + 
					"\nStart Date: " + taskList.get(i).getStartDate() + 
					"\nEnd Date: " + taskList.get(i).getEndDate());
		}
	}

	// This method ensures that no empty tasks (after removal/updating) remain
	private static void nullRemovalCheck() {
		for (int i = 0; i < taskList.size(); i++) {
			if (taskList.get(i).getTaskName() == null) {
				taskList.remove(i); 
			}
		}
	}

    public static Comparator<Task> taskComparator = new Comparator<Task>() {
    
    	@Override
    	public int compare(Task t1, Task t2) {
			if (t1.getEndDate() == null || t2.getEndDate() == null) {
				return 0;
			}
			
			return t1.getEndDate().compareTo(t2.getEndDate());
		}
    };
   
	public static void sortTaskList() {
		Collections.sort(taskList, taskComparator);
		saveToFile();
	}
	
	public static void main(String[] args) {		
		createSaveFile();
		taskList.clear();
		readFromFile();
		
		String dateInput1 = "24/12/2015";
		Date date1 = null;
		try {
			date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateInput1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Task testTask1 = new Task();
		taskList.add(testTask1);
		taskList.get(0).setTaskID(1);
		taskList.get(0).setStartDate(date1);
		taskList.get(0).setEndDate(date1);
		taskList.get(0).setTaskName("z testing Task 1");
		
		String dateInput2 = "23/12/2015";
		Date date2 = null;
		try {
			date2 = new SimpleDateFormat("dd/MM/yyyy").parse(dateInput2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Task testTask2 = new Task();
		taskList.add(testTask2);
		taskList.get(1).setTaskID(2);
		taskList.get(1).setStartDate(date2);
		taskList.get(1).setEndDate(date2);
		taskList.get(1).setTaskName("y testing Task 2");
		
		displayTaskList();
		updateTask(0, "zzz testing Task 1", date1, date1);
		System.out.println("\n\n");
		displayTaskList();
		sortTaskList();
		System.out.println("\n\n");
		displayTaskList();
		deleteTask(1);
		System.out.println("\n\n");
		displayTaskList();

	}
}
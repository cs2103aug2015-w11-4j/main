package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import objects.Task;

public class Storage {
	
    private static final String DEFAULT_DIRECTORY = "F2DO";
	private static final String FILENAME = "F2DO.txt";
	private static final String SAVED_DIRECTORY = "%s\\F2DO";
	private static final String CHANGE_DIRECTORY = "user.dir";
	
	private ArrayList<Task> taskList = new ArrayList<Task>();
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
	
	private void createSaveFile() {
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
	public void addTask(Task newTask) {
		taskList.add(newTask);
	}
	
	// Deletes the task object located at the index supplied by the user
	public void deleteTask(int taskNumber) {
		taskList.remove(taskNumber);
	}
	
	// Updates the desired task with the new information
	public void updateTask(int taskNumber, String newTitle, 
			               Date newStartDate, Date newEndDate, String newCategory) {
		taskList.get(taskNumber).setTaskName(newTitle);
		taskList.get(taskNumber).setStartDate(newStartDate);
		taskList.get(taskNumber).setEndDate(newEndDate);
		taskList.get(taskNumber).setCategory(newCategory);
	}
	
	public void saveToFile() {
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
	
	public void readFromFile() {
		try {
			FileInputStream fin = new FileInputStream(FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fin);
			try {
				taskList = (ArrayList<Task>) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

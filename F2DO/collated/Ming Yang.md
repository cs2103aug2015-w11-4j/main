# Ming Yang
###### src\storage\Storage.java
``` java
package storage;

import object.Task;
import java.io.File;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Storage class maintains file input and output related matters.
 */
public class Storage {
	private static final String DEFAULT_DIRECTORY = "F2DO";
	private static final String FILENAME = "F2DO.json";
	private static final String SAVED_DIRECTORY = "%s\\F2DO";
	private static final String CHANGE_DIRECTORY = "user.dir";
	
	private static File _saveFolder = null;
	private static File _saveFile = null;
	
	/**
	 * Initialize storage class.
	 */
	static {
		// TO BE IMPLEMENTED
		// Read setting file for custom folder path
		
		// If custom folder path does not exist, use default path
		String filePath = DEFAULT_DIRECTORY + "/" + FILENAME;
		_saveFolder = new File(DEFAULT_DIRECTORY);
		_saveFile = new File(filePath);
		
		if (createFolder()) {
			createFile();
		}
	}
	
	/**
	 * Set new folder.
	 * @param newFolderPath - path of new folder
	 * @return true if the new folder is set successfully; false otherwise
	 */
	public static boolean setFolder(String newFolderPath) {
		File prevFolder = _saveFolder;
		File prevFile = _saveFile;
		boolean isSuccessful = false;
		ConcurrentSkipListMap<Integer, Task> taskList = readTasks();
		
		try{
			String folderPath = String.format(SAVED_DIRECTORY, newFolderPath);
			String filePath = folderPath + "/" + FILENAME;
			_saveFolder = new File(folderPath);
			_saveFile = new File(filePath);

			if (createFolder() && createFile()) {
				isSuccessful = true;
				if ((prevFile != null) && (prevFile.exists()) ) {
					writeTasks(taskList);		// Copy the task list into new file
					prevFile.delete();
				}
				
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isSuccessful) {
				_saveFolder = prevFolder;
				_saveFile = prevFile;
			}
		}
		return isSuccessful;
	}

	/**
	 * Create folder if it does not exist.
	 * @return true if the folder is created successfully; false otherwise
	 */
	private static boolean createFolder() {
		try {
			if (!_saveFolder.exists()) {
				_saveFolder.mkdir();
			}
			System.setProperty(CHANGE_DIRECTORY, _saveFolder.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Create file if it does not exist.
	 * @return true if the file is created successfully; false otherwise
	 */
	private static boolean createFile() {
		try {
			if (!_saveFile.exists()) {
				StorageHelper.createJsonFile(_saveFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Read task list from the file.
	 * @return task list
	 */
	public static ConcurrentSkipListMap<Integer, Task> readTasks() {
		return StorageHelper.readJsonFile(_saveFile);
	}
	
	/**
	 * Write task list into the file.
	 * @param taskList - task list to be written into the file
	 * @return true if it is written into the file successfully; false otherwise
	 */
	public static boolean writeTasks(ConcurrentSkipListMap<Integer, Task> taskList) {
		return StorageHelper.writeJsonFile(_saveFile, taskList);
	}
	
}
```
###### src\storage\StorageHelper.java
``` java
package storage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentSkipListMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import object.Task;

/**
 * StorageHelper class maintains reading and writing of JSON file.
 */
public class StorageHelper {
	private static SimpleDateFormat _dateFormat = 
			new SimpleDateFormat("EEE dd MMM HH:mm:ss zzz yyyy");

	/**
	 * Write the task list into JSON file.
	 * @param file - file to be written
	 * @param taskList - task list
	 * @return true if the task list is written into the file successfully; false otherwise
	 */
	public static boolean writeJsonFile(File file, 
			ConcurrentSkipListMap<Integer, Task> taskList) {

		try {
			ObjectMapper objMapper = new ObjectMapper();

			objMapper.setDateFormat(_dateFormat);
			objMapper.writerWithDefaultPrettyPrinter().writeValue(file, taskList);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}

	/**
	 * Read the task list stored from the JSON file.
	 * @param file - file to be retrieved
	 * @return the task list
	 */
	public static ConcurrentSkipListMap<Integer, Task> readJsonFile(File file) {
		ConcurrentSkipListMap<Integer, Task> taskList = 
				new ConcurrentSkipListMap<Integer, Task>();

		try {
			ObjectMapper objMapper = new ObjectMapper();
			TypeReference<ConcurrentSkipListMap<Integer, Task>> typeRef = 
					new TypeReference<ConcurrentSkipListMap<Integer, Task>>(){};

					objMapper.setDateFormat(_dateFormat);
					taskList = objMapper.readValue(file, typeRef);

		} catch (Exception e) {
			e.printStackTrace();
			return taskList;
		}
		return taskList;
	}

	/**
	 * Create empty JSON file.
	 * @param file - file to be created
	 * @return true if the file is created successfully; false otherwise
	 */
	public static boolean createJsonFile(File file) {
		try {
			ConcurrentSkipListMap<Integer, Task> taskList = 
					new ConcurrentSkipListMap<Integer, Task>();
			ObjectMapper objMapper = new ObjectMapper();

			objMapper.writeValue(file, taskList);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	
}

```
###### tests\history\HistoryTest.java
``` java
package history;

import static org.junit.Assert.*;


import java.util.logging.Logger;

import org.junit.Test;
import object.Task;
import type.CommandType;

public class HistoryTest {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Test
	public void test() {
		Task task1 = new Task();
		task1.setTaskID(1);
		task1.setStartDate(null);
		task1.setEndDate(null);
		task1.setTaskName("first task");
		CommandType add = CommandType.ADD;
		CommandType del = CommandType.DELETE;
		CommandType done = CommandType.DONE;
		CommandType undone = CommandType.UNDONE;
		
		logger.info("Starting tests");
		assertTrue(History.push(add, task1));
		assertTrue(History.push(done, task1));
		assertTrue(History.push(undone, task1));
		assertTrue(History.push(del, task1));
	    logger.info("Successful end of tests");
	}
}
```
###### tests\logic\LogicControllerTest.java
``` java
package logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.junit.Test;

import object.Task;
import parser.DateTime;

public class LogicControllerTest {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Test
	public final void testProcess() {
	    
		ArrayList<Task> testDisplayList = new ArrayList<Task>();
		Task sampleTaskOne = new Task();
		sampleTaskOne.setTaskID(1);
		sampleTaskOne.setTaskName("Meeting with Boss");
		sampleTaskOne.setStartDate(null);
		sampleTaskOne.setEndDate(null);
		testDisplayList.add(sampleTaskOne);
		
		
		//Starting the logger
		logger.info("Starting general logic tests");
	    
		//Testing floating tasks
		assertEquals("Feedback: Meeting with Boss has been successfully added!",
					(LogicController.process("add Meeting with Boss", LogicController.getDisplayList())));
		
		//Checking if the adding works fine
		assertNotEquals(testDisplayList.get(0), LogicController.getDisplayList().get(0));
		assertEquals(testDisplayList.get(0).getTaskName(), LogicController.getDisplayList().get(0).getTaskName());
		assertEquals(testDisplayList.get(0).getStartDate(), LogicController.getDisplayList().get(0).getStartDate());
		assertEquals(testDisplayList.get(0).getEndDate(), LogicController.getDisplayList().get(0).getEndDate());
		assertEquals(LogicController.getDisplayList().size(), testDisplayList.size());
		
		//Testing if task can be added with "from-to" keywords
	    assertEquals("Feedback: Holiday with wife has been successfully added!", 
					(LogicController.process("add Holiday with wife from friday to saturday", LogicController.getDisplayList())));
	    logger.info("Adding task with from-to works.");

		//Check for floating list
		assertEquals(LogicController.getDisplayList().get(1).getTaskName(), LogicController.getFloatingList().get(0).getTaskName());
		assertEquals(LogicController.getDisplayList().get(1).getStartDate(), LogicController.getFloatingList().get(0).getStartDate());
		assertEquals(LogicController.getDisplayList().get(1).getEndDate(), LogicController.getFloatingList().get(0).getEndDate());
		
		//Check for non-floating list
		assertEquals(LogicController.getDisplayList().get(0).getTaskName(), LogicController.getNonFloatingList().get(0).getTaskName());
		assertEquals(LogicController.getDisplayList().get(0).getStartDate(), LogicController.getNonFloatingList().get(0).getStartDate());
		assertEquals(LogicController.getDisplayList().get(0).getEndDate(), LogicController.getNonFloatingList().get(0).getEndDate());
		
		//Testing "del" shortform
		assertEquals("Feedback: Holiday with wife has been deleted!",
					(LogicController.process("del 1", LogicController.getDisplayList())));
	    
	    //Testing delete function
		assertEquals("Feedback: Meeting with Boss has been deleted!",
					(LogicController.process("delete 1", LogicController.getDisplayList())));
	    logger.info("Deleting task works");
	    
	    //Testing adding tasks with "by"
		assertEquals("Feedback: Homework has been successfully added!", 
					(LogicController.process("add Homework by saturday", LogicController.getDisplayList())));
	    logger.info("Adding task with by works");

	    //Test Edit
	    assertNotEquals("Feedback: Homework has been edited!",
	    			(LogicController.process("edit 1 by Sunday", LogicController.getDisplayList())));
	   	    
	    //Testing completion marking
		assertEquals("Feedback: Homework has been marked as completed!",
					(LogicController.process("done 1", LogicController.getDisplayList())));
	    logger.info("Marking task as done works.");

	    //Testing complete task display
		assertEquals("Feedback: Show 1 completed task!",
					(LogicController.process("show done", LogicController.getDisplayList())));
	    logger.info("Showing completed tasks works");
	    
	    //Clearing list for next test
		assertEquals("Feedback: Homework has been deleted!",
					(LogicController.process("delete 1", LogicController.getDisplayList())));
	    logger.info("Deleting task works.");

	    //Testing incomplete task listing
		assertEquals("Feedback: No incomplete task!",
					(LogicController.process("show undone", LogicController.getDisplayList())));
	    logger.info("Showing undone works (if there's no task)");
	    
	    //Testing out of bound deletion index
	    assertEquals("Feedback: The entered number does not exist!",
    			(LogicController.process("delete 0", LogicController.getDisplayList())));
	    assertEquals("Feedback: The entered number does not exist!",
    			(LogicController.process("delete 1", LogicController.getDisplayList())));
	    assertEquals("Feedback: The entered number does not exist!",
    			(LogicController.process("delete 2", LogicController.getDisplayList())));
	    logger.info("Deleting task numbers not acceptable throws the correct error feedback");
	    
	    //Testing invalid adds
		assertEquals("Feedback:  cannot be added!", LogicController.process("add        ", LogicController.getDisplayList()));
	    logger.info("Adding blank task not acceptable throws the correct error feedback");
	    
	    Task sampleTaskTwo = new Task();
		sampleTaskTwo.setTaskID(1);
		sampleTaskTwo.setTaskName("Meeting with Boss");
		sampleTaskTwo.setStartDate(DateTime.parse("01/12/2015"));
		sampleTaskTwo.setEndDate(DateTime.parse("31/12/2015"));
		testDisplayList.add(sampleTaskOne);
		LogicController.process("add Project from 00/12 to 32/12", LogicController.getDisplayList());
		assertNotEquals(testDisplayList.get(0).getStartDate(), LogicController.getDisplayList().get(0).getStartDate());
		assertNotEquals(testDisplayList.get(0).getEndDate(), LogicController.getDisplayList().get(0).getEndDate());
			    
	    //Finishing tests. Clears the test list.
		int length = LogicController.getDisplayList().size();
		if (length >= 0) {
			for (int i = 0; i < length; i++) {
				LogicController.process("delete 1", LogicController.getDisplayList());
			}
		}
		logger.info("End of general logic tests");

	}

```
###### tests\storage\StorageTest.java
``` java
package storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import logic.LogicController;

public class StorageTest {

	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

/*	@Test
	public void testSetFolder() {
		logger.info("Starting set folder test");
		assertFalse(Storage.setFolder("randomfile"));
		assertFalse(Storage.setFolder("*"));
	    logger.info("End of set folder test successfully");
	}*/
	
	@Test
	public void testWriteToFile() {
		logger.info("Starting write to file test");
		assertTrue(Storage.writeTasks(LogicController.getTaskList()));
		assertFalse(StorageHelper.writeJsonFile(null, LogicController.getTaskList()));
		logger.info("Ending write to file test successfully");
	}
	
	@SuppressWarnings("resource")
	@Test
	public void testReadFromFile() throws IOException {
		logger.info("Starting read from file test");
		Storage.readTasks();
		File testFailFile = new File("wat");
		BufferedWriter output = new BufferedWriter(new FileWriter(testFailFile));
        output.write("null");
		StorageHelper.readJsonFile(testFailFile);
		logger.info("Ending read from file test successfully");
	}

	@Test
	public void testCreateJsonFile() {
		File testFailFile = new File("wat");
		assertTrue(StorageHelper.createJsonFile(testFailFile));
		assertFalse(StorageHelper.createJsonFile(null));
	}

	/*@SuppressWarnings("resource")
	@Test
	public void testReadCatFile() throws IOException {
		File abc = new File("abc");
		FileWriter fileWriter = new FileWriter(abc);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write("test");
		StorageHelper.readCatFile(abc);
		
		File abc2 = new File("abc2.txt");
		ArrayList<Category> failList = new ArrayList<Category>();
		Category testCat = new Category("test");
		failList.add(testCat);
		StorageHelper.writeCatFile(abc2, failList);
		StorageHelper.readCatFile(abc2);
		
		abc2.delete();
		StorageHelper.readCatFile(abc2);
		
		File testFailFile = new File("wat.txt");
		RandomAccessFile raFile = new RandomAccessFile(testFailFile, "rw");
		raFile.getChannel().lock();
		StorageHelper.readCatFile(testFailFile);
	}*/
	
	/*@SuppressWarnings("resource")
	@Test
	public void testWriteCatFile() throws IOException {
		File abc = new File("abc.txt");
		ArrayList<Category> failList = new ArrayList<Category>();
		Category testCat = new Category("test");
		failList.add(testCat);
		assertTrue(StorageHelper.writeCatFile(abc, failList));

		RandomAccessFile raFile = new RandomAccessFile(abc, "rw");
		raFile.getChannel().lock();
		assertFalse(StorageHelper.writeCatFile(abc, failList));
		
		File abc2 = new File("");
		assertFalse(StorageHelper.writeCatFile(abc2, failList));
		
	}*/
}

```

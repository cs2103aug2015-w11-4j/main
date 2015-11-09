package storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import logic.LogicController;
import object.Task;

//@@author A0108511U
public class StorageTest {

	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	private static String _testFileName = "wat.txt";
	private static File _testFailFile = new File(_testFileName);
	
	@BeforeClass
	public static void setUp() {
		_taskList = LogicController.getTaskList().clone();
		Storage.writeTasks(new ConcurrentSkipListMap<Integer, Task>());
	}
	
	@AfterClass
	public static void tearDown() {
		Storage.writeTasks(_taskList);
		
		if (_testFailFile.exists()) {
			_testFailFile.delete();
		}
	}
	
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
		BufferedWriter output = new BufferedWriter(new FileWriter(_testFailFile));
        output.write("null");
		StorageHelper.readJsonFile(_testFailFile);
		logger.info("Ending read from file test successfully");
	}

	@Test
	public void testCreateJsonFile() {
		assertTrue(StorageHelper.createJsonFile(_testFailFile));
		assertFalse(StorageHelper.createJsonFile(null));
	}
}


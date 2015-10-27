package storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Before;
import org.junit.Test;

import logic.LogicController;

public class StorageTest {

	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Before
	public void loggerSetup() {
		FileHandler fileTxt = null;
		
	    try {
			fileTxt = new FileHandler("StorageLogging.txt");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

	    logger.addHandler(fileTxt);
	    SimpleFormatter formatterTxt = new SimpleFormatter();
	    fileTxt.setFormatter(formatterTxt);
	}
	
	@Test
	public void testSetFolder() {
		logger.info("Starting set folder test");
		assertTrue(Storage.setFolder("randomfile"));
		assertFalse(Storage.setFolder("*"));
	    logger.info("End of set folder test successfully");
	}
	
	@Test
	public void testWriteToFile() {
		logger.info("Starting write to file test");
		assertTrue(Storage.writeTasks(LogicController.getTaskList()));
		logger.info("Ending write to file test successfully");
	}
	
	@Test
	public void testReadFromFile() {
		logger.info("Starting read from file test");
		Storage.readTasks();
		logger.info("Ending read from file test successfully");
	}

}

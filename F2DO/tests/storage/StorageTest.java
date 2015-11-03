//@@author Ming Yang
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


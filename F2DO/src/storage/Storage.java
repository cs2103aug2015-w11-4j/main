package storage;

import object.Task;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;

//@@author A0108511U
public class Storage {
	private static final String DEFAULT_DIRECTORY = "F2DO";
	private static final String FILENAME = "F2DO.json";
	private static final String SAVED_DIRECTORY = "%s/F2DO";
	private static final String CHANGE_DIRECTORY = "user.dir";
	private static final String DEFAULT_FILE_PATH = DEFAULT_DIRECTORY + "/" + FILENAME; 
	private static final String PROPERTIES_FILE_PATH = DEFAULT_DIRECTORY + "/config.properties";
	private static final String PROPERTIES_SAVE_FILE = "filepath";

	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private static File _saveFolder = new File(DEFAULT_DIRECTORY);
	private static File _saveFile =  new File(DEFAULT_FILE_PATH);
	private static File _propertiesFile = new File(PROPERTIES_FILE_PATH);
	
	private static Properties properties = new Properties();
	
	/**
	 * Initialize storage class.
	 */
	static {
		if(createPropertiesFile()) {
			readPropertiesFile();
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
				isSuccessful = writePropertiesFile(filePath);
				if ((prevFile != null) && (prevFile.exists()) ) {
					writeTasks(taskList);		// Copy the task list into new file
					prevFile.delete();
				}
			} 
		} catch (Exception e) {
			logger.log(Level.WARNING, "An exception was thrown when setting folder path.", e);
		} finally {
			if (!isSuccessful) {
				_saveFolder = prevFolder;
				_saveFile = prevFile;
			}
		}
		return isSuccessful;
	}
	
	/**
	 * Create properties file.
	 * @return true if properties file is created successfully; false otherwise
	 */
	private static boolean createPropertiesFile() {
		try {
			if (!_propertiesFile.exists()) {
				_saveFolder = new File(DEFAULT_DIRECTORY);
				_saveFile =  new File(DEFAULT_FILE_PATH);
				
				if (createFolder()) {
					createFile();
				}
				
				FileOutputStream fos = new FileOutputStream(PROPERTIES_FILE_PATH);
				
				properties.setProperty(PROPERTIES_SAVE_FILE, DEFAULT_FILE_PATH);
				properties.store(fos, "Properties file of F2DO");
				fos.close();
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "An exception was thrown when creating properties file.", e);
			return false;
		}
		return true;
	}
	
	/**
	 * Read the stored file path from properties file.
	 * @return true if the file path is read successfully; false otherwise
	 */
	private static boolean readPropertiesFile() {
		try {
			FileInputStream fis = new FileInputStream(PROPERTIES_FILE_PATH);
			properties.load(fis);
			
			String filePath = properties.getProperty(PROPERTIES_SAVE_FILE);
			_saveFile = new File(filePath);
			fis.close();
			
			if (!_saveFile.exists()) {
				_propertiesFile.delete();
				createPropertiesFile();
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "An exception was thrown when reading properties file.", e);
			return false;
		}
		return true;
	}
	
	/**
	 * Write customized save folder to properties file.
	 * @param path - customized path
	 * @return true if the property is written successfully; false otherwise
	 */
	private static boolean writePropertiesFile(String path) {
		try {
			FileOutputStream fos = new FileOutputStream(PROPERTIES_FILE_PATH);

			properties.setProperty(PROPERTIES_SAVE_FILE, path);
			properties.store(fos, "Properties file of F2DO");
			fos.close();
		} catch (Exception e) {
			logger.log(Level.WARNING, "An exception was thrown when writing properties file.", e);
			return false;
		}
		return true;
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
			logger.log(Level.WARNING, "An exception was thrown when creating the folder.", e);
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
			logger.log(Level.WARNING, "An exception was thrown when creating the file.", e);
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
		if (!_saveFile.exists()) {
			_propertiesFile.delete();
			createPropertiesFile();
		}
		return StorageHelper.writeJsonFile(_saveFile, taskList);
	}
	
}

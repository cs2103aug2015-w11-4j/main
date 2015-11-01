package storage;

import object.Category;
import object.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Storage class maintains file input and output related matters.
 * @author 
 *
 */
public class Storage {
	private static final String DEFAULT_DIRECTORY = "F2DO";
	private static final String FILENAME = "F2DO.json";
	private static final String CATFILENAME = "CAT.txt";
	private static final String SAVED_DIRECTORY = "%s\\F2DO";
	private static final String CHANGE_DIRECTORY = "user.dir";
	
	private static File _saveFolder = null;
	private static File _saveFile = null;
	private static File _catFile = null;
	
	/**
	 * Initialize storage class.
	 */
	static {
		// TO BE IMPLEMENTED
		// Read setting file for custom folder path
		
		// If custom folder path does not exist, use default path
		String filePath = DEFAULT_DIRECTORY + "/" + FILENAME;
		String catFilePath = DEFAULT_DIRECTORY + "/" + CATFILENAME;
		_saveFolder = new File(DEFAULT_DIRECTORY);
		_saveFile = new File(filePath);
		_catFile = new File(catFilePath);
		
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
		File prevCatFile = _catFile;
		boolean isSuccessful = false;
		ConcurrentSkipListMap<Integer, Task> taskList = readTasks();
		ArrayList<Category> catList = readCats();
		
		try{
			String folderPath = String.format(SAVED_DIRECTORY, newFolderPath);
			String filePath = folderPath + "/" + FILENAME;
			String catFilePath = folderPath + "/" + CATFILENAME;
			_saveFolder = new File(folderPath);
			_saveFile = new File(filePath);
			_catFile = new File(catFilePath);

			if (createFolder() && createFile() && createCatFile(_catFile)) {
				isSuccessful = true;
				if ((prevFile != null) && (prevFile.exists()) ) {
					writeTasks(taskList);		// Copy the task list into new file
					prevFile.delete();
				}
				if ((prevCatFile != null) && (prevCatFile.exists()) ) {
					writeCatTasks(catList);		// Copy the cat list into new file
					prevCatFile.delete();
				}
				
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!isSuccessful) {
				_saveFolder = prevFolder;
				_saveFile = prevFile;
				_catFile = prevCatFile;
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
			if (!_catFile.exists()){
				createCatFile(_catFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * @param file - Category file to be made
	 * @return true if the file is created successfully; false otherwise
	 */
	private static boolean createCatFile(File file){
		try {
			file.createNewFile();
		} catch (IOException e) {
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
	 * Read task list from the file.
	 * @return cat list
	 */
	private static ArrayList<Category> readCats() {
		return StorageHelper.readCatFile(_catFile);
	}
	
	/**
	 * Write task list into the file.
	 * @param taskList - task list to be written into the file
	 * @return true if it is written into the file successfully; false otherwise
	 */
	public static boolean writeTasks(ConcurrentSkipListMap<Integer, Task> taskList) {
		return StorageHelper.writeJsonFile(_saveFile, taskList);
	}
	
	/**
	 * @param catList - category list to be written into the file
	 * @return true of it is written into the file successfully; false otherwise
	 */
	public static boolean writeCatTasks(ArrayList<Category> catList){
		return StorageHelper.writeCatFile(_catFile, catList);
	}
}

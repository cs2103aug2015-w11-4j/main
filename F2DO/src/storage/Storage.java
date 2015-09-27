package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Storage {
	
	private static final String DEFAULT_DIRECTORY = "F2DO";
	private static final String FILENAME = "F2DO.txt";
	private static final String SAVED_DIRECTORY = "%s\\F2DO";
	private static final String CHANGE_DIRECTORY = "user.dir";
	private static final String MESSAGE_FILE_NOT_FOUND = "F2DO file not found.";\

	
	private String saveFolder;
	
	public Storage(String directory) {
		saveFolder = String.format(SAVED_DIRECTORY, directory);
		createSaveDir(saveFolder);
	}
	
	public Storage() {
		saveFolder = DEFAULT_DIRECTORY;
		createSaveDir(DEFAULT_DIRECTORY);
	}
	
	public void createSaveDir(String directory) {
		File dir = new File(directory);
		
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		System.setProperty(CHANGE_DIRECTORY, directory);
		createSaveFile();
	}
	
	public void createSaveFile() {
		File file = new File(FILENAME);
		
		if (!file.exists()) {
			file.createNewFile();
		}
	}
	
}

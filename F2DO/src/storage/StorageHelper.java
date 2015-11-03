package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import object.Category;
import object.Task;

/**
 * StorageHelper class maintains reading and writing of JSON file.
 * @author 
 *
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

	/**
	 * @param _catFile - Path of the category file
	 * @return category file list
	 */
	public static ArrayList<Category> readCatFile(File _catFile) {
		ArrayList<Category> catFile = new ArrayList<Category>();
		String line = null;
		
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(_catFile);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                Category cat = new Category(line);
                catFile.add(cat);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                _catFile + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + _catFile + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
		return catFile;
	}
	
	/**
	 * @param file - path of the category file
	 * @param catList - category list to be written to file
	 * @return true if the operation is successful, false if the operation fails
	 */
	public static boolean writeCatFile(File file, ArrayList<Category> catList){
		
		Writer fileWriter = null;
		BufferedWriter bufferedWriter = null;

		try {

			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);

			// Write the lines one by one
			for (int i = 0; i<catList.size(); i++) {
				bufferedWriter.write(catList.get(i).getCategory());
				bufferedWriter.newLine();
				// alternatively add bufferedWriter.newLine() to change line
			}

		} catch (IOException e) {
			System.err.println("Error writing the file : ");
			e.printStackTrace();
			return false;
		} finally {

			if (bufferedWriter != null && fileWriter != null) {
				try {
					bufferedWriter.close();
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		
		return true;
	}
	
}

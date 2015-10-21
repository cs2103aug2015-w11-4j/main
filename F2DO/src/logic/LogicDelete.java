package logic;

import java.util.ArrayList;

import objects.Task;
import parser.Result;
import storage.Storage;

/**
 * Deletes tasks from the list based on the result.
 * @param taskList
 * @param result
 * @return boolean result whether delete operation is successful or not
 * @author A0111758
 */
public class LogicDelete {

	public static boolean delete(Result result, ArrayList<Task> taskList) {
		if (result.getDisplayID() != -1 && result.getStorageID() == taskList.get(result.getDisplayID()).getTaskID()) {
			try {
				//taskList.remove(result.getDisplayID());
				//Storage.saveToFile(taskList);
				Storage.deleteTask(result.getStorageID());
				return true;
			} catch (NullPointerException e) {
				System.out.println("LogicDelete = Error Removing Task");
				return false;
			} catch (Exception e){
				return false;
			}
		} else { 
			return false;
		}
	}
}


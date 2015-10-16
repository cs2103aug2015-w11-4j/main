package logic;

import java.util.ArrayList;
import objects.*;
import parser.Result;
import storage.Storage;

public class LogicAdd {

	/**
	 * Adds tasks to the list based on the result.
	 * @param taskList
	 * @param result
	 * @return boolean result whether add operation is successful or not
	 */
	public static boolean add(int taskID, Result result,ArrayList<Task> taskList) {
		switch (result.getType()){
			case EVENT: {
				TaskEvent ft = new TaskEvent(taskID, result.getTitle(),result.getStartDate(),result.getEndDate(),0);
				ft.setTaskType(result.getType());
				taskList.add(ft);
				Storage.saveToFile();
				return true;
			} 
			case DEADLINE: {
				TaskDeadLine ft = new TaskDeadLine(taskID, result.getTitle(),result.getEndDate(),0);
				ft.setTaskType(result.getType());
				taskList.add(ft);
				Storage.saveToFile();
				return true;
			} 
			case FLOATING: {
				TaskFloating ft = new TaskFloating(taskID, result.getTitle(),0);
				ft.setTaskType(result.getType());
				taskList.add(ft);
				Storage.saveToFile();
				return true;
			} 
			case INVALID: default: {
				return false;
			} 
		}
	}

}

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
	 * @author A0111758
	 */
	public static boolean add(int taskID, Result result,ArrayList<Task> taskList) {
		switch (result.getType()){
			case EVENT: {
				TaskEvent ft = new TaskEvent();
				ft.setTaskID(taskID);
				ft.setTaskName(result.getTitle());
				ft.setStartDate(result.getStartDate());
				ft.setEndDate(result.getEndDate());
				ft.setTaskType(result.getType());
				
				taskList.add(ft);
				Storage.saveToFile(taskList);
				return true;
			} 
			case DEADLINE: {
				TaskDeadLine ft = new TaskDeadLine();
				ft.setTaskID(taskID);
				ft.setTaskName(result.getTitle());
				ft.setEndDate(result.getEndDate());
				ft.setTaskType(result.getType());
				
				taskList.add(ft);
				Storage.saveToFile(taskList);
				return true;
			} 
			case FLOATING: {
				TaskFloating ft = new TaskFloating();
				ft.setTaskID(taskID);
				ft.setTaskName(result.getTitle());
				ft.setTaskType(result.getType());
				
				taskList.add(ft);
				Storage.saveToFile(taskList);
				return true;
			} 
			case INVALID: default: {
				return false;
			} 
		}
	}

}

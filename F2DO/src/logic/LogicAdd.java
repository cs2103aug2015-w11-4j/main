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
	 * @return
	 */
	public static ArrayList<Task> add(int taskID, Result result,ArrayList<Task> taskList) {
		switch (result.getType()){
			case EVENT: {
				TaskEvent ft = new TaskEvent(taskID, result.getTitle(),result.getStartDate(),result.getEndDate(),0);
				taskList.add(ft);
				Storage.saveToFile();
				//return taskList;
			} break;
			case DEADLINE: {
				TaskDeadLine ft = new TaskDeadLine(taskID, result.getTitle(),result.getEndDate(),0);
				taskList.add(ft);
				Storage.saveToFile();
				//return taskList;
			} break;
			case FLOATING: {
				TaskFloating ft = new TaskFloating(taskID, result.getTitle(),0);
				taskList.add(ft);
				Storage.saveToFile();
				//return taskList;
			} break;
			case INVALID: default: {
				//same as invalid
			}
		}

		return taskList;
	}

}

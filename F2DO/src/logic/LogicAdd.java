package logic;

import java.util.ArrayList;

import objects.*;
import parser.Result;

public class LogicAdd {

	/**
	 * Adds tasks to the list based on the result.
	 * @param taskList
	 * @param result
	 * @return
	 */
	public static ArrayList<Task> add(ArrayList<Task> taskList, Result result) {
		switch (result.getType()){
			case EVENT: {
				TaskEvent ft = new TaskEvent(result.getTitle(),result.getStartDate(),result.getEndDate(),0);
				taskList.add(ft);
				return taskList;
			}
			case DEADLINE: {
				TaskDeadLine ft = new TaskDeadLine(result.getTitle(),result.getEndDate(),0);
				taskList.add(ft);
				return taskList;
			} 
			case FLOATING: {
				TaskFloating ft = new TaskFloating(result.getTitle(),0);
				taskList.add(ft);
				return taskList;
			}
			case INVALID: {
	
			}break;
			default: {
				//same as invalid
			}
		}

		return taskList;
	}

}

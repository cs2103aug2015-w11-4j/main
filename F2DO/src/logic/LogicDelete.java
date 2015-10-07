package logic;

import java.util.ArrayList;

import objects.Task;
import objects.TaskDeadLine;
import objects.TaskEvent;
import objects.TaskFloating;
import parser.Result;
import storage.Storage;

public class LogicDelete {
	
	private static int taskID;

	public static ArrayList<Task> delete(ArrayList<Task> taskList, Result result) {
		switch (result.getType()){
			case EVENT: {
			    TaskEvent ft = new TaskEvent(taskID, result.getTitle(),result.getStartDate(),result.getEndDate(),0);
			    taskID = ft.getTaskID();
			    taskList.remove(taskID);
			    Storage.saveToFile();
		     } break;
			case DEADLINE: {
				TaskDeadLine ft = new TaskDeadLine(taskID, result.getTitle(),result.getEndDate(),0);
				taskID = ft.getTaskID();
				taskList.remove(taskID);
				Storage.saveToFile();
			} break;
			case FLOATING: {
				TaskFloating ft = new TaskFloating(taskID, result.getTitle(),0);
				taskID = ft.getTaskID();
				taskList.remove(taskID);
			} break;
			case INVALID: default: {
			//same as invalid
			}
		}
		return taskList;
	}
}


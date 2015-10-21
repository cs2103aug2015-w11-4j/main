package logic;

import java.util.ArrayList;
import objects.*;
import parser.Result;
import storage.Storage;
import type.TaskType;

public class LogicEdit {

	/**
	 * Handles Edit operation by user based on parsed result
	 * @param result
	 * @param taskList
	 * @return boolean value whether operation is successful or not
	 * @author A0111758
	 */
	public static boolean edit(Result result, ArrayList<Task> taskList) {

		//if (result.getDisplayID() != -1 && 
				//result.getStorageID() == taskList.get(result.getDisplayID()).getTaskID()) 
		{

			Task task = taskList.get(result.getDisplayID());
			// original task type

			switch(task.getTaskType()){
				case FLOATING: {
					switch(result.getType()){
						case FLOATING:{
							// If both tasks are Floating
							editTaskName(result, task);
							Storage.updateTask(result.getStorageID(), task);
							// edit priority and category too
							// remove previous task;
							//taskList.remove(result.getDisplayID());
							// replace with new floating task
							//taskList.add(result.getDisplayID(),(TaskFloating)task);
							
							//return true;
							//Storage.updateTask(result.getStorageID(), result.getTitle(), newStartDate, newEndDate)
						}
						case DEADLINE: {
							// If original task is Floating and new task is DeadLine
							editTaskName(result, task);
							editEndDate(result, task);
							// edit priority and category too
							// remove previous task;
							//taskList.remove(result.getDisplayID());
							// replace with new floating task
							TaskDeadLine td = new TaskDeadLine();
							td.setTaskID(task.getTaskID());
							td.setTaskName(task.getTaskName());
							td.setEndDate(task.getEndDate());
							td.setStartDate(null);
							td.setTaskType(TaskType.DEADLINE);
							Storage.updateTask(result.getStorageID(), task);
							//taskList.add(result.getDisplayID(),td);
							return true;
						}
						case EVENT: {
							// If original task is Floating and new task is Event 
							editTaskName(result, task);
							editEndDate(result, task);
							editStartDate(result, task);
							// edit priority and category too
							// remove previous task;
							//taskList.remove(result.getDisplayID());
							// replace with new floating task
							TaskEvent te = new TaskEvent();
							te.setTaskType(TaskType.EVENT);
							te.setTaskID(task.getTaskID());
							te.setTaskName(task.getTaskName());
							te.setEndDate(task.getEndDate());
							te.setStartDate(task.getStartDate());
							//taskList.add(result.getDisplayID(),te);
							Storage.updateTask(result.getStorageID(), task);
							return true;
						}
						case INVALID: default: {
							return false;
						}
					}
				}
				case DEADLINE: {
					switch (result.getType()) {
						// If original task is DeadLine and new task is Floating
						case FLOATING: {
							editTaskName(result, task);
							// edit priority and category too
							// remove previous task;
							//taskList.remove(result.getDisplayID());
							// replace with new floating task
							TaskFloating tf = new TaskFloating();
							tf.setTaskID(task.getTaskID());
							tf.setTaskName(task.getTaskName());
							tf.setEndDate(null);
							tf.setStartDate(null);
							tf.setTaskType(TaskType.FLOATING);
							//taskList.add(result.getDisplayID(),tf);
							Storage.updateTask(result.getStorageID(), task);
							return true;
						}
						// If both types are deadline
						case DEADLINE: {
							editTaskName(result, task);
							editEndDate(result, task);
							// edit priority and category too
							// remove previous task;
							//taskList.remove(result.getDisplayID());
							// replace with new floating task
							//taskList.add(result.getDisplayID(),(TaskDeadLine)task);	
							Storage.updateTask(result.getStorageID(), task);
							return true;
						}
						// If original task is DeadLine and new task is Event
						case EVENT: {
							editTaskName(result, task);
							editEndDate(result, task);
							editStartDate(result, task);
							// edit priority and category too
							// remove previous task;
							//taskList.remove(result.getDisplayID());
							// replace with new floating task
							TaskEvent te = new TaskEvent();
							te.setTaskType(TaskType.EVENT);
							te.setTaskID(task.getTaskID());
							te.setTaskName(task.getTaskName());
							te.setEndDate(task.getEndDate());
							te.setStartDate(task.getStartDate());
							//taskList.add(result.getDisplayID(),te);
							Storage.updateTask(result.getStorageID(), task);
							return true;
						}
						case INVALID: default: {
							return false;
						}
					}

				} 
				case EVENT: {
					switch (result.getType()) {
						// If original task is Event and new task is Floating
						case FLOATING: {
							editTaskName(result, task);
							// edit priority and category too
							// remove previous task;
							//taskList.remove(result.getDisplayID());
							// replace with new floating task
							TaskFloating tf = new TaskFloating();
							tf.setTaskID(task.getTaskID());
							tf.setTaskName(task.getTaskName());
							tf.setEndDate(null);
							tf.setStartDate(null);
							tf.setTaskType(TaskType.FLOATING);
							//taskList.add(result.getDisplayID(),tf);
							Storage.updateTask(result.getStorageID(), task);
							return true;
						}
						case DEADLINE: {
							// If original task is Event and new task is Floating
							editTaskName(result, task);
							editEndDate(result, task);
							// edit priority and category too
							// remove previous task;
							//taskList.remove(result.getDisplayID());
							// replace with new floating task
							task.setStartDate(null);
							TaskDeadLine td = new TaskDeadLine();
							td.setTaskID(task.getTaskID());
							td.setTaskName(task.getTaskName());
							td.setStartDate(null);
							td.setEndDate(task.getEndDate());
							td.setTaskType(TaskType.DEADLINE);
							//taskList.add(result.getDisplayID(),td);
							Storage.updateTask(result.getStorageID(), task);
						}
						case EVENT: {
							editTaskName(result, task);
							editEndDate(result, task);
							editStartDate(result, task);
							// edit priority and category too
							// remove previous task;
							//taskList.remove(result.getDisplayID());
							// replace with new floating task
							//taskList.add(result.getDisplayID(),(TaskEvent)task);
							Storage.updateTask(result.getStorageID(), task);
							return true;
						}
						case INVALID: default: {
							return false;
						}
					}

				}
				case INVALID:	default:{
					return false;
					// then both tasks are different type.
				}
			}	
		}
		//return false;
	}

	private static void editStartDate(Result result, Task task) {
		if (result.getStartDate()!=null){
			task.setStartDate(result.getStartDate());
		}
	}

	private static void editEndDate(Result result, Task task) {
		if (result.getEndDate()!=null){
			task.setEndDate(result.getEndDate());
		}
	}

	private static void editTaskName(Result result, Task task) {
		String taskName = result.getTitle();
		if (!taskName.trim().equals("")){
			task.setTaskName(result.getTitle());
		}
	}

}

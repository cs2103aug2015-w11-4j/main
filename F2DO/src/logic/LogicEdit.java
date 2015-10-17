package logic;

import java.util.ArrayList;
import objects.*;
import parser.Result;
import type.TaskType;

public class LogicEdit {

	public static boolean edit(Result result, ArrayList<Task> taskList) {

		if (result.getDisplayID() != -1 && 
				result.getStorageID() == taskList.get(result.getDisplayID()).getTaskID()) {

			Task task = taskList.get(result.getDisplayID());
			// original task type
			switch (task.getTaskType()) {
				case FLOATING:{
					if (result.getType() == task.getTaskType()){
						task.setTaskName(result.getTitle());
						// and also set priority when available
					} else {
						// check title, if its changed, then change it.
						try {
							if (result.getTitle().isEmpty() || result.getTitle().trim().equals("") || result.getTitle() == null){
								// do nothing
							} else {
								task.setTaskName(result.getTitle());
							}	
						} catch (NullPointerException e ){
							// do nothing	
						}
						
						// then add the end date for deadline task
						task.setEndDate(result.getEndDate());
						// set priority if any
						// if new task is type event add start date
	
	
						if (result.getType() == TaskType.EVENT){
							// add start date
							task.setStartDate(result.getStartDate());
	
							// create new task obj
							TaskEvent taskEvent = new TaskEvent(task.getTaskID(), task.getTaskName(), task.getStartDate(), task.getEndDate(), 0);
							// remove old task
							taskEvent.setTaskType(TaskType.EVENT);
							taskList.remove(result.getDisplayID());
							// replace with new one
							taskList.add(result.getDisplayID(),taskEvent);
	
	
						} else {
							// create new task obj
							TaskDeadLine taskDead = new TaskDeadLine(task.getTaskID(), task.getTaskName(), task.getEndDate(), 0);
							// remove old task
							taskDead.setTaskType(TaskType.DEADLINE);
							taskList.remove(result.getDisplayID());
							// replace with new one
							taskList.add(result.getDisplayID(),taskDead);
	
						}
					}
	
					return true;
				} 
				case DEADLINE: {
					// check the task description and change if necessary
					if (result.getTitle().isEmpty() || result.getTitle().trim().equals("")){
						// do nothing cause task description still same
					} else {
						task.setTaskName(result.getTitle());
					}
	
					// if new task is same as deadline task then check the enddate
					if (result.getType() == task.getTaskType()){
						if (result.getEndDate() == task.getEndDate()){
							// do nothing cause end dates still same
						} else if (task.getEndDate()!=null && result.getEndDate()==null){
							// do nothing cause end date still same
						} else {
							// change date
							task.setEndDate(result.getEndDate());
						}
					} else {
						// check whether new task is floating
						if (result.getType() == TaskType.FLOATING){
							// create new floating task
							TaskFloating tf = new TaskFloating(task.getTaskID(), task.getTaskName(), task.getPriority());
							// remove old task
							taskList.remove(result.getDisplayID());
							// replace with new floating task
							taskList.add(result.getDisplayID(),tf);
						} else {
							// new task is event type then add start date
							task.setStartDate(result.getStartDate());
							// create new event task
							TaskEvent te = new TaskEvent(task.getTaskID(), task.getTaskName(), task.getStartDate(), task.getEndDate(), 0);
							// remove old task
							taskList.remove(result.getDisplayID());
							// replace with new event task
							taskList.add(result.getDisplayID(),te);
						}
					}
	
					return true;
				} 
				case EVENT: {
					// check the task description and change if necessary
					try {
						if (result.getTitle().isEmpty() || result.getTitle().trim().equals("") || result.getTitle() == null){
							// do nothing cause task description still same
						} else {
							task.setTaskName(result.getTitle());
						}
					} catch (NullPointerException e){
						// do nothing
						//task.setTaskName(result.getTitle());
					}
					
	
					// if new task same as current task
					if (result.getType() == task.getTaskType()){
						if (result.getEndDate() == task.getEndDate()){
							// do nothing cause end dates still same
						} else if (task.getEndDate()!=null && result.getEndDate()==null){
							// do nothing cause end date still same
						} else {
							// change date
							task.setEndDate(result.getEndDate());
						}
	
						if (result.getStartDate() == task.getStartDate()){
							// do nothing cause start dates still same
						} else if (task.getStartDate()!=null && result.getStartDate()==null){
							// do nothing cause start date still same
						} else {
							// change date
							task.setStartDate(result.getStartDate());
						}
						// change priority too if necessary	
					} else {
						// if new task is floating task
						if (result.getType()==TaskType.FLOATING){
							// create new floating task
							TaskFloating tf = new TaskFloating(task.getTaskID(), task.getTaskName(), task.getPriority());
							// remove old task
							taskList.remove(result.getDisplayID());
							// replace with new floating task
							taskList.add(result.getDisplayID(),tf);
						} else {
							if (result.getEndDate() == task.getEndDate()){
								// do nothing cause end dates still same
							} else if (task.getEndDate()!=null && result.getEndDate()==null){
								// do nothing cause end date still same
							} else {
								// change date
								task.setEndDate(result.getEndDate());
							}
							// deadline tasks
							TaskDeadLine taskDead = new TaskDeadLine(task.getTaskID(), task.getTaskName(), task.getEndDate(), 0);
							// remove old task
							taskList.remove(result.getDisplayID());
							// replace with new one
							taskList.add(result.getDisplayID(),taskDead);
						}
					}
	
	
					return true;
				} 
				case INVALID: default:{
					return false;
				} 
			} 
		}

		return false;
	}

}

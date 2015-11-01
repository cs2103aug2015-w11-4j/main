package logic;

import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Result;
import object.Task;
import storage.Storage;
import type.TaskType;

/**
 * CommandEdit class executes edit function.
 * @author 
 *
 */
public class CommandEdit implements ICommand {
	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	
	public CommandEdit(Result result,
			ConcurrentSkipListMap<Integer, Task> taskList) {
		_result = result;
		_taskList = taskList;
	}
	
	/**
	 * Edit task.
	 */
	public Feedback execute() {
		int taskID = _result.getStorageID();
		boolean isSuccessful = false;
		String message = FeedbackHelper.ERROR_NO_INDEX;
		
		if (_taskList.containsKey(taskID)) {
			Task task = _taskList.get(taskID);
			
			switch(_result.getType()){
				case FLOATING:{
					switch(task.getTaskType()) {
					case FLOATING:{
						task.setTaskName(_result.getContent());
					}break;
					case DEADLINE: case EVENT:{
						String content = _result.getContent();
						if (!(content == null || content.equals(null) || content.trim().equals(""))){
							task.setTaskName(_result.getContent());
						}
					}break;
					case INVALID: default:{
						
					} break;
					}
				} break;
				case DEADLINE:{
					switch(task.getTaskType()) {
					case FLOATING:{
						//convert floating to deadline
						String content = _result.getContent();
						if (!(content == null || content.equals(null) || content.trim().equals(""))){
							task.setTaskName(_result.getContent());
						}
						task.setEndDate( _result.getEndDate());
						task.setTaskType(TaskType.DEADLINE);
						
					}break;
					case DEADLINE:{
						String content = _result.getContent();
						if (!(content == null || content.equals(null) || content.trim().equals(""))){
							task.setTaskName(_result.getContent());
						}
						Date date = _result.getEndDate();
						if (date != null){
							task.setEndDate( _result.getEndDate());
						}
					}break;
					case EVENT:{
						// just change the end date
						String content = _result.getContent();
						if (!(content == null || content.equals(null) || content.trim().equals(""))){
							task.setTaskName(_result.getContent());
						}
						Date date = _result.getEndDate();
						if (date != null){
							task.setEndDate( _result.getEndDate());
						}
						
					}break;
					case INVALID: default:{
						
					} break;
					}
				}break; 
				case EVENT:{
					switch(task.getTaskType()) {
					case FLOATING:{
						//convert floating to event
						String content = _result.getContent();
						if (!(content == null || content.equals(null) || content.trim().equals(""))){
							task.setTaskName(_result.getContent());
						}
						task.setEndDate( _result.getEndDate());
						task.setStartDate(_result.getStartDate());
						task.setTaskType(TaskType.EVENT);
						
					}break;
					case DEADLINE:{
						String content = _result.getContent();
						if (!(content == null || content.equals(null) || content.trim().equals(""))){
							task.setTaskName(_result.getContent());
						}
						task.setEndDate( _result.getEndDate());
						task.setStartDate(_result.getStartDate());
						task.setTaskType(TaskType.EVENT);
						
					}break;
					case EVENT:{
						String content = _result.getContent();
						if (!(content == null || content.equals(null) || content.trim().equals(""))){
							task.setTaskName(_result.getContent());
						}
						task.setEndDate( _result.getEndDate());
						task.setStartDate(_result.getStartDate());
						task.setTaskType(TaskType.EVENT);
						
					}break;
					case INVALID: default:{
						
					} break;
					} 
				} 
				case INVALID: default:{
					
				} break;
			}
			
			
			
			
			
			_taskList.remove(taskID);
			_taskList.put(taskID, task);
			Storage.writeTasks(_taskList);
			message = String.format(FeedbackHelper.MSG_EDIT, _result.getContent());
			isSuccessful = true;
		}
		
		ArrayList<Task> displayList = 
				new ArrayList<Task>(_taskList.values());
		
		return new Feedback(message, displayList, _taskList, isSuccessful);
		
	}
}

package logic;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import object.Category;
import object.Result;
import object.Task;
import storage.Storage;

public class CommandCat implements ICommand {

	private Result _result = null;
	private ConcurrentSkipListMap<Integer, Task> _taskList =
			new ConcurrentSkipListMap<Integer, Task>();
	private ArrayList<Category> _categoryList =
			new ArrayList<Category>();
	
	public CommandCat(Result result, ConcurrentSkipListMap<Integer, Task> taskList, ArrayList<Category> categoryList) {
		_result = result;
		_taskList = taskList;
		_categoryList = categoryList;
	}
	
	public Feedback execute() {
		boolean isSuccessful = false;
		String message = String.format(FeedbackHelper.ERROR_CAT, _result.getContent());
		
		
		Category cat = new Category(_result.getContent());
		
		
		if (_result != null) {
			_categoryList.add(cat);
			Storage.writeCatTasks(_categoryList);
			message = String.format(FeedbackHelper.MSG_CAT, _result.getContent());
			isSuccessful = true;
		}
		
		ArrayList<Task> displayList = 
				new ArrayList<Task>(_taskList.values());
		
		
		return new Feedback(message, displayList,  _taskList ,isSuccessful);
	}

}

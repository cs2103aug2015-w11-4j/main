package logic;

import java.util.ArrayList;

import objects.Task;
import parser.Result;

public class LogicEdit {

	public static void edit(ArrayList<Task> taskList, Result result) {
		Task task = taskList.get(result.getDisplayID());
		task.setTaskName(result.getTitle());
		task.setStartDate(result.getStartDate());
		task.setEndDate(result.getEndDate());
	}

}

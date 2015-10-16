package logic;

import java.util.ArrayList;

import objects.Task;
import parser.Result;

public class LogicDelete {
	
	public static boolean delete(Result result, ArrayList<Task> taskList) {
		
		try {
			taskList.remove(result.getDisplayID());
			return true;
		} catch (Exception e) {
			System.out.println("LogicDelete = Error Removing Task");
			return false;
		}	
	}
}


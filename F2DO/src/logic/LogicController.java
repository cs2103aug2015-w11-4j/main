package logic;

import java.util.ArrayList;

import objects.Task;
import parser.Parser;
import parser.Result;

public class LogicController {
	
	// take in parser, return result, 
	// determine command
	// execute command
	// pass to storage
	ArrayList<Task> taskList =  new ArrayList<Task>();
	
	
	public boolean initialize(){
		// load any existing task to logic
		return true;
	}
	
	
	public void process(String input){
		
		Result result = Parser.Parse(input);
		
		switch (result.getCmd()) {
		case ADD: {
			taskList = LogicAdd.add(taskList, result);
		} break;
		case DELETE: {
			LogicDelete.delete(taskList, result);
		} break;
		case SEARCH: {
			LogicSearch.search(taskList, result);
		} break;
		default: {
			
		} break;
		
		}
		//return false;
	}
	
}

package logic;

import java.util.Stack;

import object.Result;
import object.Task;
import type.CommandType;

public class History {
	private static Stack<Result> _undoStack = new Stack<Result>();
	private static Stack<Result> _redoStack = new Stack<Result>();
	
	public static boolean push(Result result, Task task) {
		try {
			//_undoStack.push(item)
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static CommandType getOppositeCmd(CommandType cmd) {
		if (cmd == CommandType.ADD) {
			return CommandType.DELETE;
		} else if (cmd == CommandType.DELETE) {
			return CommandType.ADD;
		} else if (cmd == CommandType.DONE) {
			return CommandType.UNDONE;
		} else if (cmd == CommandType.UNDONE) {
			return CommandType.DONE;
		} else {
			return cmd;
		}
	}
}

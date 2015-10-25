package logic;

import java.util.Stack;

import object.Result;
import object.Task;
import object.ExecutionPair;
import type.CommandType;

public class History {
	private static Stack<ExecutionPair> _undoStack = new Stack<ExecutionPair>();
	private static Stack<ExecutionPair> _redoStack = new Stack<ExecutionPair>();
	
	/**
	 * Push for ADD, DELETE, DONE and UNDONE functions.
	 * @param cmd - command type
	 * @param task - saved task
	 * @return true if it is pushed successful; false otherwise
	 */
	public static boolean push(CommandType cmd, Task task) {
		try {
			ExecutionPair pair = new ExecutionPair(cmd, task, task);
			_undoStack.push(pair);
			_redoStack.clear();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Push for EDIT function.
	 * @param cmd - command type
	 * @param oldTask - saved old task
	 * @param newTask - saved new task
	 * @return true if it is pushed successful; false otherwise
	 */
	public static boolean push(CommandType cmd, Task oldTask, Task newTask) {
		try {
			ExecutionPair pair = new ExecutionPair(cmd, oldTask, newTask);
			_undoStack.push(pair);
			_redoStack.clear();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Push non-editable functions.
	 * @param cmd - command type
	 * @param content - content of the input
	 * @return true if it is pushed successful; false otherwise
	 */
	public static boolean push(CommandType cmd, String content) {
		try {
			ExecutionPair pair = new ExecutionPair(cmd, content);
			_undoStack.push(pair);
			_redoStack.clear();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Get parsing result for undo.
	 * Return null if there is no more undo operation.
	 * @return undo result
	 */
	public static Result undo() {
		if (_undoStack.isEmpty()) {
			return null;
		} else {
			ExecutionPair pair = _undoStack.pop();
			_redoStack.push(pair);
			return pair.getUndo();
		}
	}
	
	/**
	 * Get parsing result for redo.
	 * Return null if there is no more redo operation.
	 * @return redo result
	 */
	public static Result redo() {
		if (_redoStack.isEmpty()) {
			return null;
		} else {
			ExecutionPair pair = _redoStack.pop();
			_undoStack.push(pair);
			return pair.getRedo();
		}
	}
}

//@@author Yu Ting
package history;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.logging.Logger;

import object.Result;
import object.Task;
import object.ExecutionPair;
import type.CommandType;

public class History {
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
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
			logger.info("Unsuccessful pushing for command Add, Delete, Done and Undone.");
			return false;
		}
		return true;
	}
	
	/**
	 * Push for EDIT function.
	 * @param cmd - command type
	 * @param oldTask - task before execution
	 * @param newTask - task after execution
	 * @return true if it is pushed successful; false otherwise
	 */
	public static boolean push(CommandType cmd, Task befExeTask, Task aftExeTask) {
		try {
			ExecutionPair pair = new ExecutionPair(cmd, befExeTask, aftExeTask);
			_undoStack.push(pair);
			_redoStack.clear();
		} catch (Exception e) {
			logger.info("Unsuccessful pushing for command Edit.");
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
			logger.info("Unsucessful pushing for non-editable functions.");
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
		try {
			ExecutionPair pair = _undoStack.pop();
			Result result = pair.getUndo();
			_redoStack.push(pair);
			
			// Get undo again if the command type is not editable
			if (!isEditableType(result.getCommand())) {
				pair = _undoStack.pop();
				result = pair.getUndo();
				_redoStack.push(pair);
			}
			
			return result;
		} catch (EmptyStackException e) {
			logger.info("Stack for undo operation is empty.");
			return null;
		}
	}
	
	/**
	 * Get parsing result for redo.
	 * Return null if there is no more redo operation.
	 * @return redo result
	 */
	public static Result redo() {
		try {
			ExecutionPair pair = _redoStack.pop();
			_undoStack.push(pair);
			return pair.getRedo();
		} catch (EmptyStackException e) {
			logger.info("Stack for redo operation is empty.");
			return null;
		}
	}
	
	/**
	 * Determine if the command type is editable.
	 * @param cmd - command type
	 * @return true if command is editable type; false otherwise
	 */
	private static boolean isEditableType(CommandType cmd) {
		return cmd == CommandType.ADD ||
				cmd == CommandType.DELETE ||
				cmd == CommandType.EDIT ||
				cmd == CommandType.DONE ||
				cmd == CommandType.UNDONE;
	}
}

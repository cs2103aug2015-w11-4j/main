package object;

import type.CommandType;

public class ExecutionPair {
	private CommandType _cmd = CommandType.INVALID;
	private String _content = null;
	private Task _befExeTask = null;
	private Task _aftExeTask = null;
	
	/**
	 * Constructor for editable command type.
	 * For example, ADD, DELETE, EDIT, DONE and UNDONE.
	 * @param cmd - editable command type
	 * @param befExeTask - task before execution
	 * @param aftExeTask - task after execution
	 */
	public ExecutionPair(CommandType cmd, Task befExeTask, Task aftExeTask) {
		_cmd = cmd;
		_befExeTask = befExeTask;
		_aftExeTask = aftExeTask;
	}
	
	/**
	 * Constructor for non-editable command type.
	 * For example, SEARCH, SHOW, HELP AND HOME.
	 * @param cmd - non-editable command type
	 * @param content - content of the parsing result
	 */
	public ExecutionPair(CommandType cmd, String content) {
		_cmd = cmd;
		_content = content;
	}
	
	/**
	 * Get undo execution result.
	 * @return undo result
	 */
	public Result getUndo() {
		CommandType newCmd = getReverseCmd();
		if (isEditableType(newCmd)) {
			return new Result(newCmd, _befExeTask);
		} else {
			Result result = new Result();
			result.setCommand(newCmd);
			result.setContent(_content);
			return result;
		}
	}
	
	/**
	 * Get redo execution result.
	 * @return redo result
	 */
	public Result getRedo() {
		if (isEditableType(_cmd)) {
			return new Result(_cmd, _aftExeTask);
		} else {
			Result result = new Result();
			result.setCommand(_cmd);
			result.setContent(_content);
			return result;
		}
	}
	
	/**
	 * Determine if the command type is editable.
	 * @param cmd - command type
	 * @return true if command is editable type; false otherwise
	 */
	private boolean isEditableType(CommandType cmd) {
		return cmd == CommandType.ADD ||
				cmd == CommandType.DELETE ||
				cmd == CommandType.EDIT ||
				cmd == CommandType.DONE ||
				cmd == CommandType.UNDONE;
	}
	
	/**
	 * Get the reverse of the command type.
	 * For example, ADD <-> DELETE, DONE <-> UNDONE
	 * @return reverse command type
	 */
	private CommandType getReverseCmd() {
		switch (_cmd) {
			case ADD:
				return CommandType.DELETE;
			case DELETE:
				return CommandType.ADD;
			case DONE:
				return CommandType.UNDONE;
			case UNDONE:
				return CommandType.DONE;
			default:
				return _cmd;
		}
	}
}

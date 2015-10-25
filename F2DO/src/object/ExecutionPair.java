package object;

import type.CommandType;

public class ExecutionPair {
	private CommandType _cmd = CommandType.INVALID;
	private String _content = null;
	private Task _oldTask = null;
	private Task _newTask = null;
	
	public ExecutionPair(CommandType cmd, Task oldTask, Task newTask) {
		_cmd = cmd;
		_oldTask = oldTask;
		_newTask = newTask;
	}
	
	public ExecutionPair(CommandType cmd, String content) {
		_cmd = cmd;
		_content = content;
	}
	
	public Result getUndo() {
		CommandType newCmd = getReverseCmd();
		if (isEditableType(newCmd)) {
			return new Result(newCmd, _oldTask);
		} else {
			Result result = new Result();
			result.setCommand(newCmd);
			result.setContent(_content);
			return result;
		}
	}
	
	public Result getRedo() {
		if (isEditableType(_cmd)) {
			return new Result(_cmd, _newTask);
		} else {
			Result result = new Result();
			result.setCommand(_cmd);
			result.setContent(_content);
			return result;
		}
	}
	
	private boolean isEditableType(CommandType cmd) {
		return cmd == CommandType.ADD ||
				cmd == CommandType.DELETE ||
				cmd == CommandType.EDIT ||
				cmd == CommandType.DONE ||
				cmd == CommandType.UNDONE;
	}
	
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

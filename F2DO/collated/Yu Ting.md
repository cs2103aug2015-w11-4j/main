# Yu Ting
###### src\history\History.java
``` java
package history;

import java.util.EmptyStackException;
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
		try {
			ExecutionPair pair = _undoStack.pop();
			Result result = pair.getUndo();
			_redoStack.push(pair);
			
			// Get undo again if the command type is not editable
			if (!isEditableType(result.getCmd())) {
				pair = _undoStack.pop();
				result = pair.getUndo();
				_redoStack.push(pair);
			}
			
			return result;
		} catch (EmptyStackException e) {
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
```
###### src\object\ExecutionPair.java
``` java
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
```
###### src\object\Task.java
``` java
package object;

import java.util.ArrayList;
import java.util.Date;

import type.TaskType;

/**
 * 
 * @author 
 *
 */
public class Task {
	private int _taskID = -1;
	private String _taskName = null;
	private Date _startDate = null;
	private Date _endDate = null;
	private TaskType _taskType = null;
	private boolean _isCompleted = false;
	private int _priority = -1;
	private ArrayList<Category> _categories;
	
	/**
	 * Default constructor.
	 */
	public Task() {	}
	
	/**
	 * Constructor for floating task.
	 * @param taskID
	 * @param taskName
	 * @param priority
	 */
	public Task(int taskID, String taskName, TaskType type, int priority){
		_taskID = taskID;
		_taskName = taskName;
		_taskType = type;
		_priority = priority;
	}

	/**
	 * Constructor for deadline task.
	 * @param taskID
	 * @param taskName
	 * @param deadline
	 * @param priority
	 */
	public Task(int taskID, String taskName, TaskType type, 
			Date deadline, int priority) {
		this(taskID, taskName, type, priority);
		_endDate = deadline;
	}

	/**
	 * Constructor for event task.
	 * @param taskID
	 * @param taskName
	 * @param startDate
	 * @param endDate
	 * @param priority
	 */
	public Task(int taskID, String taskName, TaskType type, 
			Date startDate, Date endDate, int priority) {
		this(taskID, taskName, type, priority);
		_startDate = startDate;
		_endDate = endDate;
	}
	
	/**
	 * Constructor for result from parser.
	 * @param result
	 */
	public Task(Result result) {
		this (result.getStorageID(), result.getContent(), result.getType(),
				result.getStartDate(), result.getEndDate(), result.getPriority());
	}
	
	/**
	 * Get task ID.
	 * @return task ID
	 */
	public int getTaskID() {
		return _taskID;
	}
	
	/**
	 * Set task ID.
	 * @param taskID
	 */
	public void setTaskID(int taskID) {
		_taskID = taskID;
	}
	
	/**
	 * Get task name.
	 * @return
	 */
	public String getTaskName() {
		return _taskName;
	}
	
	/**
	 * Set task name.
	 * @param taskName
	 */
	public void setTaskName(String taskName) {
		_taskName = taskName;
	}
	
	/**
	 * Get start date and time.
	 * @return start date and time
	 */
	public Date getStartDate() {
		return _startDate;
	}
	
	/**
	 * Set start date and time.
	 * @param startDate - start date and time
	 */
	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}
	
	/**
	 * Get end date and time.
	 * @return end date and time
	 */
	public Date getEndDate() {
		return _endDate;
	}
	
	/**
	 * Set end date and time.
	 * @param endDate - end date and time
	 */
	public void setEndDate(Date endDate) {
		_endDate = endDate;
	}
	
	/**
	 * Get task type.
	 * @return task type
	 */
	public TaskType getTaskType() {
		return _taskType;
	}
	
	/**
	 * Set task type.
	 * @param taskType
	 */
	public void setTaskType(TaskType taskType) {
		_taskType = taskType;
	}

	/**
	 * Get if the task is completed.
	 * @return true if the task is completed; false otherwise
	 */
	public Boolean getCompleted() {
		return _isCompleted;
	}
	
	/**
	 * Set if the task is completed.
	 * @param completed - true if the task is completed; false otherwise
	 */
	public void setCompleted(Boolean completed) {
		_isCompleted = completed;
	}
	
	/**
	 * Get priority of the task.
	 * @return priority
	 */
	public int getPriority() {
		return _priority;
	}
	
	/**
	 * Set priority of the task.
	 * @param priority
	 */
	public void setPriority(int priority) {
		_priority = priority;
	}

	/**
	 * Get categories.
	 * @return categories
	 */
	public ArrayList<Category> getCategory() {
		return _categories;
	}
	
	/**
	 * Set categories
	 * @param categories
	 */
	public void setCategory(ArrayList<Category> categories) {
		_categories = categories;
	}
}
```
###### src\parser\DateTime.java
``` java
package parser;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.utils.Span;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTime {
	private static final int DAY = 0;
	private static final int MONTH = 1;
	private static final int YEAR = 2;
	private static final int DATE_SIZE = 3;
	private static final int DAY_MONTH_SIZE = 2;
	private static final int DAY_MONTH_YEAR_SIZE = 3;
	private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	/**
	 * Analyze input and return standard date format.
	 * @param input - possible date input
	 * @return standard date format
	 */
	public static Date parse(String input) {
		return parse(input, true);
	}
	
	/**
	 * Combine the date and time into a standard date format.
	 * @param date 
	 * @param time
	 * @return standard date format
	 */
	public static Date combineDateTime(Date date, Date time) {
		Calendar calendar = Calendar.getInstance();
		Calendar dateCalendar = Calendar.getInstance();
		Calendar timeCalendar = Calendar.getInstance();
		
		calendar.clear();
		dateCalendar.setTime(date);
		timeCalendar.setTime(time);
		
		calendar.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
		calendar.set(Calendar.DATE, dateCalendar.get(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
		
		return calendar.getTime();
	}
	
	/**
	 * Ensure end date is later than start date.
	 * @param startDate - start date
	 * @param endDate - end date
	 * @return later end date
	 */
	public static Date getLaterEndDate(Date startDate, Date endDate) {
		Date newEndDate = endDate;
		
		if (startDate != null && endDate != null) {
			Calendar startCalendar = Calendar.getInstance();
			Calendar endCalendar = Calendar.getInstance();

			startCalendar.setTime(startDate);
			endCalendar.setTime(endDate);

			if (!isEndDateLater(startDate, endDate)) {
				int startYear = startCalendar.get(Calendar.YEAR);
				int endYear = endCalendar.get(Calendar.YEAR);
				
				if (startYear > endYear) {
					endCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR));
				} else {
					endCalendar.add(Calendar.DAY_OF_MONTH, 7);
				}
				newEndDate = endCalendar.getTime();
			}
		}	
		return newEndDate;
	}
	
	/**
	 * Compare if the end date is later than the start date.
	 * @param startDate - start date
	 * @param endDate - end date
	 * @return true if end date is later than start date; false otherwise
	 */
	private static boolean isEndDateLater(Date startDate, Date endDate) {
		try {
			if (startDate.compareTo(endDate) < 0) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	/**
	 * Analyze input and return standard date format with the option of date in 
	 * British or American date format.
	 * @param input - possible date input
	 * @param isBritish - British or American format
	 * @return standard date format
	 */
	private static Date parse(String input, boolean isBritish) {	
		if (isBritish) {
			return parseBritish(input);
		} else {
			return parseAmerican(input);
		}
	}
	
	/**
	 * Analyze the input in American date format. 
	 * @param input - possible American date format
	 * @return standard date format
	 */
	private static Date parseAmerican(String input) {
		Date dateTime = null;
		Span parsedDateTime = Chronic.parse(input);
		
		//System.out.println("parsedAmerican: " + parsedDateTime);
		
		if(parsedDateTime != null) {
			dateTime = parsedDateTime.getEndCalendar().getTime();
		}
		
		return dateTime;
	}
	
	/**
	 * Analyze the input in British date format.
	 * @param input - possible British date format input
	 * @return standard date format
	 */
	private static Date parseBritish(String input) {
		String date = getAmericanDate(input);
		String time = getTime(input);
		Date resultDate = null;
		
		if (date == null && time == null) {
			return parseAmerican(input);
		} else {
			String dateTime = "";
			//System.out.println("DATE: " + date);
			
			if (date != null) {
				dateTime += date + " ";
			} else {
				Date tempDate = parseAmerican(input);
				
				if (tempDate != null) {
					Calendar dateCalendar = Calendar.getInstance();
					dateCalendar.setTime(tempDate);
					
					dateTime += dateCalendar.get(Calendar.YEAR) + "-" + 
							(dateCalendar.get(Calendar.MONTH) + 1) + "-" + 
							dateCalendar.get(Calendar.DATE) + " ";
				}
			}
			
			if (time != null) {
				dateTime += time;
			}
			
			System.out.println(dateTime);
			
			resultDate = parseAmerican(dateTime);
			
			if (date == null && resultDate != null) {
				Date currentTime = new Date();
				
				if (resultDate.compareTo(currentTime) < 0) {
					Calendar newCalendar = Calendar.getInstance();
					Calendar oldCalendar = Calendar.getInstance();
					
					newCalendar.clear();
					oldCalendar.setTime(resultDate);
					newCalendar.set(oldCalendar.get(Calendar.YEAR) + 1, 
							oldCalendar.get(Calendar.MONTH), 
							oldCalendar.get(Calendar.DATE), 
							oldCalendar.get(Calendar.HOUR_OF_DAY), 
							oldCalendar.get(Calendar.MINUTE), 
							oldCalendar.get(Calendar.MILLISECOND));
					
					resultDate = newCalendar.getTime();
				}
			}
			
			return resultDate;
		}
	}
	
	/**
	 * Convert the British date format into American date format, "MMM DD YYYY" or "MMM DD YY".
	 * @param input - possible British date format input
	 * @return standard date format
	 */
	private static String getAmericanDate(String input) {
		String shortMonth = "jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec";
		String regexNumbericDM = ".*?([0-9]{1,2})[/-]([0-9]{1,2}).*";
		String regexShortDM = ".*?([0-9]{1,2})[ /-](" + shortMonth + ").*";
		String regexNumericDMY = ".*?([0-9]{1,2})[/-]([0-9]{1,2})[/-]([0-9]{2,4}).*";
		String regexShortDMY = ".*?([0-9]{1,2})[ /-](" + shortMonth + ")[ /-]([0-9]{2,4}).*";
		
		String[] twoGroups = {regexNumbericDM, regexShortDM};
		String[] threeGroups = {regexNumericDMY, regexShortDMY};
		
		for (int i = 0; i < threeGroups.length; i++) {
			String result = getDateMatcher(input, threeGroups[i], DAY_MONTH_YEAR_SIZE);
			
			if (result != null) {
				return result;
			}
		}
	
		for (int i = 0 ; i< twoGroups.length; i++) {
			String result = getDateMatcher(input, twoGroups[i], DAY_MONTH_SIZE);
			
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param input
	 * @param regex
	 * @param groupNumber
	 * @return
	 */
	private static String getDateMatcher(String input, String regex, int groupNumber) {
		String dateTime = null;
		String[] dayMonthYear = new String[DATE_SIZE];
		Arrays.fill(dayMonthYear, "");
		
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			dateTime = "";
			for (int i = 0; i < groupNumber; i++) {
				dayMonthYear[i] = matcher.group(i + 1);
			}
			
			if (groupNumber >= DAY_MONTH_SIZE) {
				if (isConvertable(dayMonthYear[MONTH])) {
					int monthIndex = Integer.parseInt(dayMonthYear[MONTH]) - 1;

					if (monthIndex < MONTHS.length) {
						dateTime += MONTHS[monthIndex] + " ";
					}
				} else {
					dateTime += dayMonthYear[MONTH] + " ";
				}
				
				dateTime += dayMonthYear[DAY] + " ";
			}
			
			if (groupNumber == DAY_MONTH_YEAR_SIZE) {
				dateTime += dayMonthYear[YEAR];
			}
		}
		
		return dateTime;
	}
	
	private static String getTime(String input) {
		String regexAmPm = ".*?([0-9]{1,2})(am|pm).*";
		String regexColon = ".*?([0-9]{1,2})[:.]([0-9]{1,2}).*";
		
		Pattern pattern = Pattern.compile(regexAmPm, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			return matcher.group(1) + matcher.group(2);
		}
		
		pattern = Pattern.compile(regexColon);
		matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			return matcher.group(1) + ":" + matcher.group(2);
		}
		return null;
	}
	
	private static boolean isConvertable(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static void main(String[] args) {
		parseBritish("fjdiaf 2/4/1992 dfjdif");
		parseBritish("16-04-1992");
		parseBritish("16/12/15");
		parseBritish("17-Apr-1992");
		parseBritish("17-Apr-92");
		parseBritish("fjdifd 18/Apr/1992 dfjidf");
		parseBritish("19 Apr 1992");
		parseBritish("20 Apr");
		parseBritish("dfdjaif 21 Apr fdjifd");
		
		parseBritish("4pm");
		parseBritish("2:30");
		parseBritish("4.30");
		parseBritish("6 Nov 2pm");
		parseBritish("Nov 7 2pm");
		
		parseBritish("Fri Oct 23 12:00:00 SGT 2015");
	}
}

```
###### src\parser\IKeyword.java
``` java
package parser;

import object.Result;
import type.KeywordType;

import java.util.HashMap;

public interface IKeyword {	
	public static IKeyword parseKeyword(KeywordType keywordType, String input) {
		IKeyword keywordFunc = new KeywordDefault(input);
		HashMap<KeywordType, IKeyword> functions = KeywordHelper.getFunctions(input);

		if (functions.containsKey(keywordType)) {
			keywordFunc = functions.get(keywordType);
		}
		return keywordFunc;
	}
	
	public Result analyze();
}
```
###### src\parser\KeywordAt.java
``` java
package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Result;

public class KeywordAt implements IKeyword {
	private static String _input = null;
	
	public KeywordAt(String input) {
		_input = input;
	}
	
	/**
	 * Analyze the pattern that contains 'at'.
	 * Return the analyzing result.
	 */
	public Result analyze() {
		String regexAtOn = "(.*?) at (.*?) on (.*)";
		String regexAt = "(.*?) at (.*?)";
		
		final int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
		
		Pattern pattern = Pattern.compile(regexAtOn, flags);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeThreeInfo(true, 
					matcher.group(1),
					matcher.group(2),
					matcher.group(3));
			return result;
		}
		
		pattern = Pattern.compile(regexAt, flags);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeTwoInfo(true, 
					matcher.group(1), 
					matcher.group(2));
			return result;
		}
		
		return new Result(null, null, null);
	}
}
```
###### src\parser\KeywordByDue.java
``` java
package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Result;

public class KeywordByDue implements IKeyword {
	private static String _input = null;
	private final int _flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
	
	public KeywordByDue(String input) {
		_input = input;
	}
	
	/**
	 * Analyze the pattern that contains 'by' and 'due'.
	 * Return the analyzing result.
	 */
	public Result analyze() {
		String regexBy = "(.*?) by (.*?)";
		String regexDue = "(.*?) due (.*?)";
		String regexDueOn = "(.*?) due on (.*?)";
		String regexOnlyBy = "by (.*?)";
		String regexOnlyDue = "due (.*?)";
		String regexOnlyDueOn = "due on (.*?)";
		
		String[] regex1 = {regexBy, regexDue, regexDueOn};
		String[] regex2 = {regexOnlyBy, regexOnlyDue, regexOnlyDueOn};
		
		for (int i = 0; i < regex1.length; i++) {
			Result regexResult = getMatcher(regex1[i]);
			
			if (regexResult != null) {
				return regexResult;
			}
		}
		
		for (int i = 0; i < regex1.length; i++) {
			Result regexResult = getMatcher2(regex2[i]);
			
			if (regexResult != null) {
				return regexResult;
			}
		}
		
		return new Result(null, null, null);
	}
	
	private Result getMatcher(String regex) {
		Pattern pattern = Pattern.compile(regex, _flags);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeTwoInfo(false, 
					matcher.group(1), 
					matcher.group(2));
			return result;
		}
		
		return null;
	}
	
	private Result getMatcher2(String regex) {
		Pattern pattern = Pattern.compile(regex, _flags);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeOneInfo(false, 
					matcher.group(1));
			return result;
		}
		
		return null;
	}
}
```
###### src\parser\KeywordDay.java
``` java
package parser;

import object.Result;

public class KeywordDay implements IKeyword {
	private static String _input = null;
	
	public KeywordDay(String input) {
		_input = input;
	}
	
	public Result analyze() {
		
		return new Result(null, null, null);
	}

}
```
###### src\parser\KeywordDefault.java
``` java
package parser;

import object.Result;

public class KeywordDefault implements IKeyword {
	String _input = null;
	
	public KeywordDefault(String input) {
		// remove the redundant spacing
		this._input = input.replaceAll("\\s+$", "");
	}
	
	public Result analyze() {
		return new Result(_input, null, null);
	}
}
```
###### src\parser\KeywordFromTo.java
``` java
package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Result;

public class KeywordFromTo implements IKeyword{
	private static String _input = null;
	
	public KeywordFromTo(String input) {
		_input = input;
	}
	
	/**
	 * Analyze the pattern that contains 'from' and 'to'.
	 * Return the analyzing result.
	 */
	public Result analyze() {
		String regexFromToOn = "(.*?) from (.*?) to (.*) on (.*?)";
		String regexFromTo = "(.*?) from (.*?) to (.*)";
		
		final int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
		
		Pattern pattern = Pattern.compile(regexFromToOn, flags);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeFourInfo(matcher.group(1),
					matcher.group(2),
					matcher.group(3),
					matcher.group(4));
			return result;
		}
		
		pattern = Pattern.compile(regexFromTo, flags);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeThreeInfo(matcher.group(1), 
					matcher.group(2), 
					matcher.group(3));
			return result;
		}
		
		return new Result(null, null, null);
	}
}
```
###### src\parser\KeywordHelper.java
``` java
package parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import object.Result;
import type.KeywordType;


public class KeywordHelper {
	
	public static TreeMap<Integer, KeywordType> getKeywordIndex(ArrayList<String> splitWords) {
		TreeMap<Integer, KeywordType> keywordIndex = new TreeMap<Integer, KeywordType>();

		for (int i = 0; i < splitWords.size(); i++) {
			String word = splitWords.get(i);
			KeywordType toType = KeywordType.toType(word);
			
			if (toType != KeywordType.INVALID) {
				keywordIndex.put(i, toType);
			}
		}
		
		return keywordIndex;
	}
	
	public static HashMap<KeywordType, IKeyword> getFunctions(String input) {
		HashMap<KeywordType, IKeyword> functions = new HashMap<KeywordType, IKeyword>();
		
		functions.put(KeywordType.AT, new KeywordAt(input));
		functions.put(KeywordType.ON, new KeywordOn(input));
		functions.put(KeywordType.FROM, new KeywordFromTo(input));
		functions.put(KeywordType.IN, new KeywordIn(input));
		functions.put(KeywordType.BY, new KeywordByDue(input));
		functions.put(KeywordType.DUE, new KeywordByDue(input));
		functions.put(KeywordType.TOMORROW, new KeywordDay(input));
		functions.put(KeywordType.YESTERDAY, new KeywordDay(input));
		
		return functions;
	}
	
	// analyze Only On (day)
	public static Result analyzeOneInfo(boolean isStartDate, String dateTimeString) {
		Date dateTime = DateTime.parse(dateTimeString);
		Result res = new Result();
		
		if (isStartDate) {
			res.setStartDate(dateTime);
			return res;
		} else {
			res.setEndDate(dateTime);
			return res;
		}
	}
	
	public static Result analyzeTwoInfo(boolean isStartDate, String title, 
			String dateTimeString) {
		Date dateTime = DateTime.parse(dateTimeString);
		
		if (isStartDate) {
			return new Result(rmWhitespace(title), dateTime, null);
		} else {
			return new Result(rmWhitespace(title), null, dateTime);
		}
	}
	
	public static Result analyzeThreeInfo(String title, String startDateString, 
			String endDateString) {
		Date startDate = DateTime.parse(startDateString);
		Date endDate = DateTime.parse(endDateString);
		
		if (startDate != null && endDate != null) {
			endDate = DateTime.getLaterEndDate(startDate, endDate);
		}
		
		return new Result(rmWhitespace(title), startDate, endDate);
	}
	
	public static Result analyzeThreeInfo(boolean isStartDate, String title, 
			String timeString, String dateString) {
		Date time = DateTime.parse(timeString);
		Date date = DateTime.parse(dateString);
		
		if (time != null && date != null) {
			Date dateTime = DateTime.combineDateTime(date, time);
			if (isStartDate) {
				return new Result(rmWhitespace(title), dateTime, null);
			} else {
				return new Result(rmWhitespace(title), null, dateTime);
			}
		}
		
		return new Result(rmWhitespace(title), null, null);
	}
	
	public static Result analyzeFourInfo(String title, String startTimeString, 
			String endTimeString, String dateString) {
		Date startDate = null;
		Date endDate = null;
		Date startTime = DateTime.parse(startTimeString);
		Date endTime = DateTime.parse(endTimeString);
		Date date = DateTime.parse(dateString);
		
		if (startTime != null && date != null) {
			startDate = DateTime.combineDateTime(date, startTime);
		}
		
		if (endTime != null && date != null) {
			endDate = DateTime.combineDateTime(date, endTime);
		}
		
		if (startTime == null && endTime == null && date != null) {
			startDate = date;
		}
		
		if (startDate != null && endDate != null) {
			endDate = DateTime.getLaterEndDate(startDate, endDate);
		}
		
		return new Result(rmWhitespace(title), startDate, endDate);
	}
	
	private static String rmWhitespace(String string) {
		return string.replaceAll("\\s+$", "");
	}
}
```
###### src\parser\KeywordIn.java
``` java
package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Result;

public class KeywordIn implements IKeyword {
	private String _input = null;
	
	public KeywordIn(String input) {
		_input = input;
	}
	
	/**
	 * Analyze the pattern that contains 'in'.
	 * Return the analyzing result.
	 */
	public Result analyze() {
		String regexIn = "(.*?) in (.*?)";
		
		final int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
		
		Pattern pattern = Pattern.compile(regexIn, flags);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeTwoInfo(false, 
					matcher.group(1), 
					"in " + matcher.group(2));
			return result;
		}
		
		
		return new Result(null, null, null);
	}

}
```
###### src\parser\KeywordOn.java
``` java
package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Result;

public class KeywordOn implements IKeyword {
	
	private String _input = null;
	
	public KeywordOn(String input) {
		_input = input;
	}
	
	/**
	 * Analyze the pattern that contains 'on'.
	 * Return the analyzing result.
	 */
	public Result analyze() {
		String regexOnFromTo = "(.*?) on (.*?) from (.*) to (.*?)";
		String regexOn = "(.*?) on (.*?)";
		String regexOnlyOn = "on (.*?)";
		
		final int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
		
		Pattern pattern = Pattern.compile(regexOnFromTo, flags);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeFourInfo(matcher.group(1),
					matcher.group(3),
					matcher.group(4),
					matcher.group(2));
			return result;
		}
		
		pattern = Pattern.compile(regexOn, flags);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeTwoInfo(true, 
					matcher.group(1), 
					matcher.group(2));
			return result;
		}
		
		pattern = Pattern.compile(regexOnlyOn, flags);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeOneInfo(true, 
					matcher.group(1));
			return result;
		}
		
		return new Result(null, null, null);
	}
}
```
###### src\parser\Parser.java
``` java
package parser;

import java.util.Date;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.NumberFormatException;

import type.CommandType;
import type.KeywordType;
import type.TaskType;
import object.Result;
import object.Task;

public class Parser {
	private static String _removeCmdInput = "";
	
	/**
	 * Parse the command.
	 * @param input - command input
	 * @return the parsed result
	 */
	public static Result parse(String input, ArrayList<Task> taskList) {
		int storageID = -1;
		_removeCmdInput = "";
		
		CommandType cmd = analyzeCmd(input);
		
		if (cmd == CommandType.DELETE || 
				cmd == CommandType.EDIT ||
				cmd == CommandType.DONE ||
				cmd == CommandType.UNDONE) {
			storageID = analyzeID(_removeCmdInput, taskList);
		}
		
		Result tempResult = analyzeDateTitle(_removeCmdInput);
		
		String title = tempResult.getContent();
		Date startDate = tempResult.getStartDate();
		Date endDate = tempResult.getEndDate();
		TaskType type = analyzeTask(cmd, title, startDate, endDate);
		
		Result result = new Result(storageID, cmd, title, type, startDate, endDate);
		return result;
	}
	
	/**
	 * Determine type of the command.
	 * @param word - parsed word
	 * @return command type
	 */
	private static CommandType analyzeCmd(String input) {
		String[] splitWords = input.split(" ");
		CommandType cmd = CommandType.INVALID;
		
		for (int i = 0; i < splitWords.length; i++) {
			if (cmd == CommandType.INVALID) {
				cmd = CommandType.toCmd(splitWords[i]);
				
				if (cmd != CommandType.INVALID) {
					continue;
				}
			}
			_removeCmdInput += splitWords[i] + " ";
		}

		return cmd;
	}
	
	/**
	 * Determine the ID in storage corresponding to UI ID.
	 * @param input - input after removing command
	 * @param taskList - task list displayed in UI
	 * @return ID in storage if exists, otherwise -1
	 */
	private static int analyzeID(String input, ArrayList<Task> taskList) {
		String[] splitWords = input.split(" ");
		int storageID = -1;
		
		if (splitWords.length > 0) {
			try {
				int displayID = Integer.parseInt(splitWords[0]) - 1;

				if (displayID < taskList.size() && displayID >= 0) {
					storageID = taskList.get(displayID).getTaskID();
				}

				if (splitWords.length > 1) {
					_removeCmdInput = "";
					for (int i = 1; i < splitWords.length; i++) {
						_removeCmdInput += splitWords[i] + " ";
					}
				}
				
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		
		return storageID;
	}
	
	/**
	 * Determine type of the task.
	 * @param title - title of the task
	 * @param startDate - start date and time
	 * @param endDate - end date and time
	 * @return task type
	 */
	private static TaskType analyzeTask(CommandType type, String title, Date startDate, Date endDate) {
		if (title == null && startDate == null && endDate == null) {
			return TaskType.INVALID;
		}
		
		if (type.equals(CommandType.ADD) && (title == null || title.equals("") || title.isEmpty())) {
			return TaskType.INVALID;
		}
		
		if (startDate == null && endDate == null) {
			return TaskType.FLOATING;
		} else if (startDate == null && endDate != null) {
			return TaskType.DEADLINE;
		} else {
			return TaskType.EVENT;
		}
	}
	
	/**
	 * Determine the dates and title of the task.
	 * @param input - input after removing command
	 * @return title, start date and end date
	 */
	private static Result analyzeDateTitle(String input) {
		ArrayList<String> splitWords = new ArrayList<String>(Arrays.asList(input.split(" ")));
		TreeMap<Integer, KeywordType> keywordIndices = KeywordHelper.getKeywordIndex(splitWords);
		ArrayList<Integer> indexList = new ArrayList<Integer>(keywordIndices.keySet());
		int listSize = indexList.size();
		
		// If the input contains keyword
		for (int i = 0; i < listSize; i++) {
			int index = indexList.get(i);
			KeywordType keyword = keywordIndices.get(index);
			String parseInput = "";
			
			if (i < (listSize - 1)) {
				int nextIndex = indexList.get(i + 1);
				
				for (int j = 0; j < nextIndex; j++) {
					parseInput += splitWords.get(j) + " ";
				}
			} else {
				parseInput = input;
			}
			
			IKeyword function = IKeyword.parseKeyword(keyword, parseInput);
			Result result = function.analyze();
			
			if (isDateFound(result.getStartDate(), result.getEndDate())) {
				return result;
			}
		}
		
		// If the input does not contain keyword
		IKeyword function = IKeyword.parseKeyword(null, input);
		return function.analyze();
	}
	
	/**
	 * Check if any of start date or end date is found.
	 * @param startDate - start date
	 * @param endDate - end date
	 * @return true if any date is found; false otherwise
	 */
	private static boolean isDateFound(Date startDate, Date endDate) {
		if (startDate == null && endDate == null) {
			return false;
		}
		return true;
	}
}
```
###### src\type\CommandType.java
``` java
package type;

public enum CommandType {
	ADD,
	EDIT,
	DELETE,
	SEARCH,
	SHOW,
	DONE,
	UNDONE,
	HELP,
	HOME,
	INVALID, 
	CAT, 
	MOVE;
	
	public static CommandType toCmd(String word) {
		try {
			word = word.toUpperCase();
			if (word.equals("DEL")) {
				return DELETE;
			}
			
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID; 
		}
	}
}
```
###### src\type\KeywordType.java
``` java
package type;

public enum KeywordType {
	AT,
	ON,
	FROM,
	IN,
	BY,
	DUE,
	UNTIL,
	TOMORROW,
	YESTERDAY,
	INVALID;
	
	public static KeywordType toType(String word) {
		try {
			word = word.toUpperCase();
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID; 
		}
	}
}
```
###### src\type\ShowType.java
``` java
package type;

public enum ShowType {
	ALL,
	DONE,
	UNDONE,
	INVALID;
	
	public static ShowType toType(String word) {
		try {
			word = word.toUpperCase();
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID; 
		}
	}
}
```
###### src\type\TaskType.java
``` java
package type;

public enum TaskType {
	EVENT,
	DEADLINE,
	FLOATING,
	INVALID;
}
```
###### tests\parser\DateTimeTest.java
``` java
package parser;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import org.junit.Test;

public class DateTimeTest {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Test
	public void testSlashNumericDMY() {
		
		logger.info("Starting SlashNumericDMY test");

		assertEquals(getResult(2015, 12, 16, 12, 0), simpleFormat(DateTime.parse("16/12/2015")));
		assertEquals(getResult(2015, 12, 17, 12, 0), simpleFormat(DateTime.parse("17/12/15")));
		assertEquals(getResult(2015, 12, 6, 12, 0), simpleFormat(DateTime.parse("6/12/2015")));
		assertEquals(getResult(2015, 12, 7, 12, 0), simpleFormat(DateTime.parse("07/12/2015")));
		assertEquals(getResult(2015, 2, 16, 12, 0), simpleFormat(DateTime.parse("16/2/2015")));
		assertEquals(getResult(2015, 2, 17, 12, 0), simpleFormat(DateTime.parse("17/02/2015")));

		logger.info("Successful end of SlashNumericDMY test");
	}
	
	@Test 
	public void testHyphenNumericDMY() {
	    
	    logger.info("Starting HyphenNumericDMY test");

		assertEquals(getResult(2015, 12, 16, 12, 0), simpleFormat(DateTime.parse("16-12-2015")));
		assertEquals(getResult(2015, 12, 16, 12, 0), simpleFormat(DateTime.parse("16-12-15")));
		assertEquals(getResult(2015, 12, 6, 12, 0), simpleFormat(DateTime.parse("6-12-2015")));
		assertEquals(getResult(2015, 12, 6, 12, 0), simpleFormat(DateTime.parse("06-12-2015")));
		assertEquals(getResult(2015, 2, 16, 12, 0), simpleFormat(DateTime.parse("16-2-2015")));
		assertEquals(getResult(2015, 2, 16, 12, 0), simpleFormat(DateTime.parse("16-02-2015")));

	    logger.info("Successful end of HyphenNumericDMY test");
	}
	
	@Test 
	public void testShortDMY() {
	    
		logger.info("Starting ShortDMY test");

		assertEquals(getResult(2015, 12, 16, 12, 0), simpleFormat(DateTime.parse("16 Dec 2015")));
		assertEquals(getResult(2015, 12, 16, 12, 0), simpleFormat(DateTime.parse("16 Dec 15")));
		assertEquals(getResult(2015, 12, 6, 12, 0), simpleFormat(DateTime.parse("6 Dec 2015")));
		assertEquals(getResult(2015, 12, 6, 12, 0), simpleFormat(DateTime.parse("06 Dec 2015")));
		assertEquals(getResult(2015, 2, 8, 12, 0), simpleFormat(DateTime.parse("08 Feb 15")));

	    logger.info("Successful end of ShortDMY test");
	}
	
	@Test
	public void testSlashNumbericDM() {
	    
		logger.info("Starting SlashNumbericDM test");

	    assertEquals(getResult(0, 12, 16, 12, 0), simpleFormat(DateTime.parse("16/12")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("6/12")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("06/12")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("16/2")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("16/02")));
	
	    logger.info("Successful end of SlashNumbericDM test");
	}
	
	@Test
	public void testHyphenNumbericDM() {
	    
		logger.info("Starting HyphenNumbericDM test");

	    assertEquals(getResult(0, 12, 16, 12, 0), simpleFormat(DateTime.parse("16-12")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("6-12")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("06-12")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("16-2")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("16-02")));

		logger.info("Successful end of HyphenNumbericDM test");
	}
	
	@Test 
	public void testShortDM() {
	    
		logger.info("Starting ShortDM test");

	    assertEquals(getResult(0, 12, 16, 12, 0), simpleFormat(DateTime.parse("16 Dec")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("6 Dec")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("06 Dec")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("16 Feb")));
		
		logger.info("Successful end of ShortDM test");
	}
	
	@Test 
	public void testShortMD() {
	    
		logger.info("Starting ShortMD test");

	    assertEquals(getResult(0, 12, 16, 12, 0), simpleFormat(DateTime.parse("Dec 16")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("Dec 6")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("Dec 06")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("Feb 16")));

		logger.info("Successful end of ShortMD test");
	}
	
	@Test 
	public void testTime() {
	    
		logger.info("Starting Time test");

	    assertEquals(getResult(0, 0, 0, 15, 0), simpleFormat(DateTime.parse("3pm")));
		assertEquals(getResult(0, 0, 0, 8, 0), simpleFormat(DateTime.parse("8am")));
		assertEquals(getResult(0, 0, 0, 17, 30), simpleFormat(DateTime.parse("17:30")));
		assertEquals(getResult(0, 0, 0, 17, 30), simpleFormat(DateTime.parse("17.30")));
	
		logger.info("Successful end of Time test");
	}
	
	@Test
	public void testMixture() {
	    
		logger.info("Starting Mixture test");

	    assertEquals(getResult(0, 11, 4, 16, 0), simpleFormat(DateTime.parse("Nov 4 4pm")));
		assertEquals(getResult(0, 2, 16, 7, 0), simpleFormat(DateTime.parse("16 Feb 7am")));
		
		// THESE THREE FAILED
		assertEquals(getResult(0, 11, 4, 8, 30), simpleFormat(DateTime.parse("Nov 4 8.30")));
		assertEquals(getResult(0, 2, 16, 22, 10), simpleFormat(DateTime.parse("16 Feb 22:10")));
		assertEquals(getResult(2015, 2, 17, 22, 10), simpleFormat(DateTime.parse("17 Feb 2015 22:10")));
	
	    logger.info("Successful end of Mixture test");
	}
	
	private String getResult(int year, int month, int date, int hour, int min) {
		Date currentTime = new Date();
		Calendar currentCalendar = Calendar.getInstance();
		Calendar givenCalendar = Calendar.getInstance();
		
		givenCalendar.clear();
		currentCalendar.setTime(currentTime);
		
		if (year == 0) {
			givenCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
		} else {
			givenCalendar.set(Calendar.YEAR, year);
		}
		
		if (month == 0) {
			givenCalendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH));
		} else {
			givenCalendar.set(Calendar.MONTH, month - 1);
		}
		
		if (date == 0) {
			givenCalendar.set(Calendar.DATE, currentCalendar.get(Calendar.DATE));
		} else {
			givenCalendar.set(Calendar.DATE, date);
		}
		
		givenCalendar.set(Calendar.HOUR_OF_DAY, hour);
		givenCalendar.set(Calendar.MINUTE, min);
		
		int compare = givenCalendar.compareTo(currentCalendar);
		
		if (compare < 0 && year == 0) {
			givenCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR) + 1);
		}
		
		System.out.println(givenCalendar.getTime().toString());
		
		return simpleFormat(givenCalendar.getTime());
	}
	
	private String simpleFormat(Date date) {
		DateFormat simpleDate = new SimpleDateFormat("dd MMM yyyy HH:mm");
		return simpleDate.format(date);
	}

}
```
###### tests\parser\ParserTest.java
``` java
/**
 * 
 */
package parser;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.*;
import logic.LogicControllerTest;
import object.Result;
import object.Task;
import type.CommandType;
import type.TaskType;

/**
 * 
 *
 */
public class ParserTest extends LogicControllerTest{

	Result result;
	
	/**
	 * Testing "ADD" parsing inputs
	 * Input 1: "add Read a book"
	 */
	@Test
	public void testParserAddInput1() {
		result = Parser.parse("add Read a book", new ArrayList<Task>());
		assertEquals(result.getContent(),"Read a book");
		assertEquals(result.getType(), TaskType.FLOATING);
		assertEquals(result.getCmd(), CommandType.ADD);
		assertEquals(result.getEndDate(),null);
		assertEquals(result.getStartDate(),null);
		assertEquals(result.getPriority(),-1);
	}
	
	/**
	 * Input 2: "add Read maths book by wed"
	 */
	@Test
	public void testParserAddInput2() {
		result = Parser.parse("add Read maths book by wed", new ArrayList<Task>());
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		Date date = cal.getTime();
		
	    assertEquals(result.getContent(),"Read maths book");
		assertEquals(result.getType(), TaskType.DEADLINE);
		assertEquals(result.getCmd(), CommandType.ADD);
	    assertEquals(result.getEndDate().toString(),date.toString());
		assertEquals(result.getStartDate(),null);
		assertEquals(result.getPriority(),-1);
	}
	/**
	 * Input 3: "add Maths exam on thu"
	 */
	@Test
	public void testParserAddInput3() {
		result = Parser.parse("add Maths exam on thu", new ArrayList<Task>());
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		Date date = cal.getTime();
		
		assertEquals("Maths exam", result.getContent());
		assertEquals(TaskType.EVENT, result.getType());
		assertEquals(CommandType.ADD, result.getCmd());
		assertEquals(null, result.getEndDate());
		assertEquals(date.toString(), result.getStartDate().toString());
		assertEquals(-1, result.getPriority());
	}
	/**
	 * Input 4: "add Camping trip from wed to mon"
	 */
	@Test
	public void testParserAddInput4() {
		result = Parser.parse("add Camping trip from wed to mon", new ArrayList<Task>());
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		Date date1 = cal.getTime();
		
		// Need to manually edit the date should this day is reached.
		cal.set(Calendar.MONTH,10);
		cal.set(Calendar.WEEK_OF_MONTH,1);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		Date date2 = cal.getTime();
		
		System.out.println("1DATE: "+ date1.toString());
		System.out.println("1RDATE: "+ result.getStartDate().toString());
		
		System.out.println("2DATE: "+ date2.toString());
		System.out.println("2RDATE: "+ result.getEndDate().toString());
		
		assertEquals("Camping trip", result.getContent());
		assertEquals(TaskType.EVENT, result.getType());
		assertEquals(CommandType.ADD, result.getCmd());
		assertEquals(date2.toString(), result.getEndDate().toString());
		assertEquals(date1.toString(), result.getStartDate().toString());
		assertEquals(-1, result.getPriority());
	}
	/**
	 * Input 5: "add Go to market"
	 */
	@Test
	public void testParserAddInput5() {
		result = Parser.parse("add Go to market", new ArrayList<Task>());
		assertEquals("Go to market", result.getContent());
		assertEquals(TaskType.FLOATING, result.getType());
		assertEquals(CommandType.ADD, result.getCmd());
		assertEquals(null, result.getEndDate());
		assertEquals(null, result.getStartDate());
		assertEquals(-1, result.getPriority());
	}
	/**
	 * Input 6: "add Run marathon from Clementi to Bugis"
	 */
	@Test
	public void testParserAddInput6() {
		result = Parser.parse("add Run marathon from Clementi to Bugis", new ArrayList<Task>());
		assertEquals("Run marathon from Clementi to Bugis", result.getContent());
		assertEquals(TaskType.FLOATING, result.getType());
		assertEquals(CommandType.ADD, result.getCmd());
		assertEquals(null, result.getEndDate());
		assertEquals(null, result.getStartDate());
		assertEquals(-1, result.getPriority());
	}
	/**
	 * Input 7: "add Read a book"
	 */
	@Test
	public void testParserAddInput7() {
		result = Parser.parse("add Watch a movie on thu 4pm", new ArrayList<Task>());
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR,4);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		Date date = cal.getTime();
		
		//System.out.println("DATE: "+ date.toString());
		//System.out.println("RDATE: "+ result.getStartDate().toString());
		
		assertEquals("Watch a movie", result.getContent());
		assertEquals(TaskType.EVENT, result.getType());
		assertEquals(CommandType.ADD, result.getCmd());
		assertEquals(null, result.getEndDate());
		assertEquals(date.toString(), result.getStartDate().toString());
		assertEquals(-1, result.getPriority());
	}
	
	/**
	 * Input 8: "add Walk home from market"
	 */
	@Test
	public void testParserAddInput8() {
		result = Parser.parse("add Walk home from market", new ArrayList<Task>());
		assertEquals("Walk home from market", result.getContent());
		assertEquals(TaskType.FLOATING, result.getType());
		assertEquals(CommandType.ADD, result.getCmd());
		assertEquals(null, result.getEndDate());
		assertEquals(null, result.getStartDate());
		assertEquals(-1, result.getPriority());
	}

	/**
	 * Input 9: "add test in two days"
	 */
	@Test
	public void testParserAddInput9() {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 2);
		Date date = cal.getTime();
		
		result = Parser.parse("add test in two days", new ArrayList<Task>());
		assertEquals("test", result.getContent());
		assertEquals(TaskType.DEADLINE, result.getType());
		assertEquals(CommandType.ADD, result.getCmd());
		assertEquals(date.toString(), result.getEndDate().toString());
		assertEquals(null, result.getStartDate());
		assertEquals(-1, result.getPriority());
	} 
	
	/**
	 * 
	 */
	@Test
	public void testParserDelInput1() {
		result = Parser.parse("del 1", new ArrayList<Task>());
		assertEquals("1", result.getContent());
		assertEquals(TaskType.FLOATING, result.getType());
		assertEquals(CommandType.DELETE, result.getCmd());
		assertEquals(null, result.getEndDate());
		assertEquals(null, result.getStartDate());
		assertEquals(-1, result.getPriority());
	}
	
}
```

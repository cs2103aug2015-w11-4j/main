package history;

import static org.junit.Assert.*;


import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import object.Result;
import object.Task;
import type.CommandType;

//@@author A0108511U
public class HistoryTest {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Before
	public void setUp() {
		Result result = new Result();
		
		while (result != null) {
			result = History.undo();
		}
	}

	@Test
	public void testEditable() {
		Task task1 = new Task();
		task1.setTaskID(1);
		task1.setStartDate(null);
		task1.setEndDate(null);
		task1.setTaskName("First task");
		
		CommandType add = CommandType.ADD;
		CommandType del = CommandType.DELETE;
		CommandType done = CommandType.DONE;
		CommandType undone = CommandType.UNDONE;
		
		logger.info("Starting tests");
		assertEquals(null, History.undo());		// No undo operation
		assertEquals(null, History.redo());		// No redo operation
		
		assertTrue(History.push(add, task1));	// Add Task 1
		assertEquals(CommandType.DELETE, History.undo().getCommand());	// Undo for add is delete
		assertEquals(CommandType.ADD, History.redo().getCommand());		// Redo for add is add
		
		assertTrue(History.push(done, task1));
		assertEquals(CommandType.UNDONE, History.undo().getCommand());	// Undo for done is undone
		assertEquals(CommandType.DONE, History.redo().getCommand());	// Redo for done is done
		
		assertTrue(History.push(undone, task1));
		assertEquals(CommandType.DONE, History.undo().getCommand());	// Undo for undone is done
		assertEquals(CommandType.UNDONE, History.redo().getCommand());	// Redo for undone is undone
		
		assertTrue(History.push(del, task1));
		assertEquals(CommandType.ADD, History.undo().getCommand());		// Undo for delete is add
		assertEquals(CommandType.DELETE, History.redo().getCommand());	// Redo for delete is delete
		
	    logger.info("Successful end of tests");
	}
	
	@Test
	public void testRecentNonEditable() {
		// First push - SHOW ALL
		assertTrue(History.push(CommandType.SHOW, "all"));
		assertEquals(null, History.undo());		// No recent non-editable will be processed
		
	}
}

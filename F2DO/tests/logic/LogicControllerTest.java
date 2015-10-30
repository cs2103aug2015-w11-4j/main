package logic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Before;
import org.junit.Test;

import object.Task;

public class LogicControllerTest {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Test
	public final void testProcess() {
	    
		ArrayList<Task> testDisplayList = new ArrayList<Task>();
		Task sampleTaskOne = new Task();
		sampleTaskOne.setTaskID(1);
		sampleTaskOne.setTaskName("Task One");
		sampleTaskOne.setStartDate(null);
		sampleTaskOne.setEndDate(null);
		testDisplayList.add(sampleTaskOne);
		
		
		//Starting the logger
		logger.info("Starting tests");
	    
		//Testing floating tasks
		assertEquals("Feedback: Task One has been successfully added!",
					(LogicController.process("add Task One", LogicController.getDisplayList())));
		
		//Checking if the adding works fine
		assertNotEquals(testDisplayList.get(0), LogicController.getDisplayList().get(0));
		assertEquals(LogicController.getDisplayList().size(), testDisplayList.size());
		
		//Testing "del" shortform
		assertEquals("Feedback: Task One has been deleted!",
					(LogicController.process("del 1", LogicController.getDisplayList())));
		
		//Testing if task can be added with "from-to" keywords
	    assertEquals("Feedback: testtask has been successfully added!", 
					(LogicController.process("add testtask from friday to saturday", LogicController.getDisplayList())));
	    logger.info("Adding task with from-to works.");
	    
	    //Testing delete function
		assertEquals("Feedback: testtask has been deleted!",
					(LogicController.process("delete 1", LogicController.getDisplayList())));
	    logger.info("Deleting task works");
	    
	    //Testing adding tasks with "by"
		assertEquals("Feedback: testtask2 has been successfully added!", 
					(LogicController.process("add testtask2 by saturday", LogicController.getDisplayList())));
	    logger.info("Adding task with by works");

	    //Testing completion marking
		assertEquals("Feedback: testtask2 has been marked as completed!",
					(LogicController.process("done 1", LogicController.getDisplayList())));
	    logger.info("Marking task as done works.");

	    //Testing complete task display
		assertEquals("Feedback: Show 1 complete task!",
					(LogicController.process("show done", LogicController.getDisplayList())));
	    logger.info("Showing completed tasks works");

	    //Clearing list for next test
		assertEquals("Feedback: testtask2 has been deleted!",
					(LogicController.process("delete 1", LogicController.getDisplayList())));
	    logger.info("Deleting task works.");

	    //Testing incomplete task listing
		assertEquals("Feedback: No incomplete task!",
					(LogicController.process("show undone", LogicController.getDisplayList())));
	    logger.info("Showing undone works (if there's no task)");
	    
	    //Testing out of bound deletion index
	    assertEquals("Feedback: The entered number does not exist!",
	    			(LogicController.process("delete 0", LogicController.getDisplayList())));
	    logger.info("Deleting task numbers not acceptable throws the correct error feedback");

	    //Finishing tests
	    logger.info("End of tests");

	}

}



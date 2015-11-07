//@@author Ming Yang
package logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.junit.Test;

import object.Task;
import parser.DateTime;

public class LogicControllerTest {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	@Test
	public final void testProcess() {
	    
		ArrayList<Task> testDisplayList = new ArrayList<Task>();
		Task sampleTaskOne = new Task();
		sampleTaskOne.setTaskID(1);
		sampleTaskOne.setTaskName("Meeting with Boss");
		sampleTaskOne.setStartDate(null);
		sampleTaskOne.setEndDate(null);
		testDisplayList.add(sampleTaskOne);
				
		//Starting the logger
		logger.info("Starting general logic tests");
	    
		//Testing floating tasks
		assertEquals("Feedback: Meeting with Boss has been successfully added!",
					(LogicController.process("add Meeting with Boss", LogicController.getDisplayList())));
		
		//Checking if the adding works fine
		assertEquals(testDisplayList.get(0).getTaskName(), LogicController.getDisplayList().get(0).getTaskName());
		assertEquals(testDisplayList.get(0).getStartDate(), LogicController.getDisplayList().get(0).getStartDate());
		assertEquals(testDisplayList.get(0).getEndDate(), LogicController.getDisplayList().get(0).getEndDate());
		assertEquals(testDisplayList.size(), LogicController.getDisplayList().size());
		
		//Testing if task can be added with "from-to" keywords
	    assertEquals("Feedback: Holiday with wife has been successfully added!", 
					(LogicController.process("add Holiday with wife from friday to saturday", LogicController.getDisplayList())));
	    logger.info("Adding task with from-to works.");

		//Check for floating list
		assertEquals(LogicController.getDisplayList().get(1).getTaskName(), LogicController.getFloatingList().get(0).getTaskName());
		assertEquals(LogicController.getDisplayList().get(1).getStartDate(), LogicController.getFloatingList().get(0).getStartDate());
		assertEquals(LogicController.getDisplayList().get(1).getEndDate(), LogicController.getFloatingList().get(0).getEndDate());
		
		//Check for non-floating list
		assertEquals(LogicController.getDisplayList().get(0).getTaskName(), LogicController.getNonFloatingList().get(0).getTaskName());
		assertEquals(LogicController.getDisplayList().get(0).getStartDate(), LogicController.getNonFloatingList().get(0).getStartDate());
		assertEquals(LogicController.getDisplayList().get(0).getEndDate(), LogicController.getNonFloatingList().get(0).getEndDate());
		
		//Testing "del" shortform
		assertEquals("Feedback: Holiday with wife has been deleted!",
					(LogicController.process("del 1", LogicController.getDisplayList())));
	    
	    //Testing delete function
		assertEquals("Feedback: Meeting with Boss has been deleted!",
					(LogicController.process("delete 1", LogicController.getDisplayList())));
	    logger.info("Deleting task works");
	    
	    //Testing adding tasks with "by"
		assertEquals("Feedback: Homework has been successfully added!", 
					(LogicController.process("add Homework by saturday", LogicController.getDisplayList())));
	    logger.info("Adding task with by works");

	    //Test Edit
	    assertEquals("Feedback: Homework has been modified!",
	    			(LogicController.process("edit 1 Homework by Sunday", LogicController.getDisplayList())));
	    logger.info("Editing task works.");

	    //Test undone display
	    assertEquals("Feedback: Show 1 incomplete task!",
				(LogicController.process("show undone", LogicController.getDisplayList())));
	    logger.info("Showing incomplete tasks works");

	    //Testing completion marking
		assertEquals("Feedback: Homework has been marked as completed!",
					(LogicController.process("done 1", LogicController.getDisplayList())));
	    logger.info("Marking task as done works.");

	    //Testing complete task display
		assertEquals("Feedback: Show 1 completed task!",
					(LogicController.process("show done", LogicController.getDisplayList())));
	    logger.info("Showing completed tasks works");
	    
	    //Testing marking done task done
	    assertEquals("Feedback: Homework has been marked as completed!",
					(LogicController.process("done 1", LogicController.getDisplayList())));
		assertEquals("Feedback: Show 1 completed task!",
					(LogicController.process("show done", LogicController.getDisplayList())));
		logger.info("Marking completed task completed works");
    
	    //Testing marking done task undone
		assertEquals("Feedback: Homework has been marked as incomplete!",
					(LogicController.process("undone 1", LogicController.getDisplayList())));
		logger.info("Marking done task as undone works.");
		
		//Testing marking undone task undone
		assertEquals("Feedback: Homework has been marked as incomplete!",
					(LogicController.process("undone 1", LogicController.getDisplayList())));
		logger.info("Marking undone task as undone works.");
	    
		//Testing show all display
		assertEquals("Feedback: Show all task!",
					(LogicController.process("show all", LogicController.getDisplayList())));
		logger.info("Showing all tasks works for 1 task");
		
		//Testing show all/done/undone for tasklists with more than one task
		LogicController.process("add Meeting", LogicController.getDisplayList());
		assertEquals("Feedback: Show all tasks!",
					(LogicController.process("show all", LogicController.getDisplayList())));
		assertEquals("Feedback: Show 2 incomplete tasks!",
					(LogicController.process("show undone", LogicController.getDisplayList())));
		LogicController.process("done 2", LogicController.getDisplayList());
		LogicController.process("done 1", LogicController.getDisplayList());
		assertEquals("Feedback: Show 2 completed tasks!",
					(LogicController.process("show done", LogicController.getDisplayList())));
		LogicController.process("delete 2", LogicController.getDisplayList());
		logger.info("Showing all/done/undone tasks works for 2 or more tasks");

		//Testing edit task with no modifications
		LogicController.process("show done", LogicController.getDisplayList());
	    assertEquals("Feedback: Nothing to be modified!",
					(LogicController.process("edit 1", LogicController.getDisplayList())));
	    logger.info("Editing task with no modification works.");
		
	    //Testing edit task with no modifications
		LogicController.process("show done", LogicController.getDisplayList());
	    assertEquals("Feedback: Nothing to be modified!",
					(LogicController.process("edit 1", LogicController.getDisplayList())));
	    logger.info("Editing task with no modification works.");
	    
	    //Clearing list for next test
		LogicController.process("show done", LogicController.getDisplayList());
		assertEquals("Feedback: Homework has been deleted!",
					(LogicController.process("delete 1", LogicController.getDisplayList())));
	    logger.info("Deleting task works.");
	    
	    //Testing incomplete/complete/all task listing for blank tasklist
		assertEquals("Feedback: No incomplete task!",
					(LogicController.process("show undone", LogicController.getDisplayList())));
		assertEquals("Feedback: No completed task!",
					(LogicController.process("show done", LogicController.getDisplayList())));
		assertEquals("Feedback: No task!",
					(LogicController.process("show all", LogicController.getDisplayList())));
	    logger.info("Showing functions work (if there's no task)");
	    
	    //Testing out of bound deletion index
	    assertEquals("Feedback: The entered number does not exist!",
					(LogicController.process("delete 0", LogicController.getDisplayList())));
	    assertEquals("Feedback: The entered number does not exist!",
					(LogicController.process("delete 1", LogicController.getDisplayList())));
	    assertEquals("Feedback: The entered number does not exist!",
					(LogicController.process("delete 2", LogicController.getDisplayList())));
	    logger.info("Deleting task numbers not acceptable throws the correct error feedback");
	    
	    //Out of bound testing for done/undone
	    assertEquals("Feedback: The entered number does not exist!",
					(LogicController.process("done 0", LogicController.getDisplayList())));
	    assertEquals("Feedback: The entered number does not exist!",
					(LogicController.process("undone 0", LogicController.getDisplayList())));
	    
	    //Testing out of bound for edit
	    assertEquals("Feedback: The entered number does not exist!",
					(LogicController.process("edit 0", LogicController.getDisplayList())));
	    	    
	    //Testing invalid adds
		assertEquals("Feedback:  cannot be added!",
					(LogicController.process("add        ", LogicController.getDisplayList())));
	    logger.info("Adding blank task not acceptable throws the correct error feedback");
	    
	    Task sampleTaskTwo = new Task();
		sampleTaskTwo.setTaskID(1);
		sampleTaskTwo.setTaskName("Meeting with Boss");
		sampleTaskTwo.setStartDate(DateTime.parse("01/12/2015"));
		sampleTaskTwo.setEndDate(DateTime.parse("31/12/2015"));
		testDisplayList.add(sampleTaskOne);
		LogicController.process("add Project from 00/12 to 32/12", LogicController.getDisplayList());
		assertNotEquals(testDisplayList.get(0).getStartDate(), LogicController.getDisplayList().get(0).getStartDate());
		assertNotEquals(testDisplayList.get(0).getEndDate(), LogicController.getDisplayList().get(0).getEndDate());
			    
	    //Finishing tests. Clears the test list.
		int length = LogicController.getDisplayList().size();
		if (length >= 0) {
			for (int i = 0; i < length; i++) {
				LogicController.process("delete 1", LogicController.getDisplayList());
			}
		}
		logger.info("End of general logic tests");

	}

	//@@author Sufyan
	@Test
	public void testComparator() {
		
		logger.info("Start of comparator tests");

		//t1 has end no start; t2 has end no start.
		LogicController.process("add 2103 Tutorial homework due today", LogicController.getDisplayList());
		LogicController.process("add 2334 revision by next Sunday 4pm", LogicController.getDisplayList());
		
		//t1 has end no start; t2 has start no end.
		LogicController.process("add 3230 Homework due Friday", LogicController.getDisplayList());
		LogicController.process("add 2101 Presentation at Friday 4pm", LogicController.getDisplayList());
		
		//t1 has end no start, t2 has neither.
		LogicController.process("add Finish revision by Sunday 4pm", LogicController.getDisplayList());
		LogicController.process("add Learn make-up", LogicController.getDisplayList());
		
		//t1 has end no start, t2 has start and end.
		LogicController.process("add Stock up on drinks due 08/11", LogicController.getDisplayList());
		LogicController.process("add Conduct online course from Friday to Sunday", LogicController.getDisplayList());

		//t1 has start no end, t2 has end no start.
		LogicController.process("add Dinner with parents at 08/11 4pm", LogicController.getDisplayList());
		LogicController.process("add 2103 Website due next Sunday", LogicController.getDisplayList());
		
		//t1 has start no end, t2 has start no end.
		LogicController.process("add Watch documentary about anime at 07/11 4pm", LogicController.getDisplayList());
		LogicController.process("add Facial appointment at Sunday 4pm", LogicController.getDisplayList());

		//t1 has start no end, t2 has end and start.
		LogicController.process("add Cousin's Wedding at next Sunday 4pm", LogicController.getDisplayList());
		LogicController.process("add Finals from 24/11 to 03/12", LogicController.getDisplayList());

		//t1 has start no end, t2 has neither.
		LogicController.process("add Shop for clothes at 20/11 4pm", LogicController.getDisplayList());
		LogicController.process("add Learning Japanese", LogicController.getDisplayList());

		//t1 has both, t2 has start no end.
		LogicController.process("add 2103 Project Finalisation from Wednesday to Sunday", LogicController.getDisplayList());
		LogicController.process("add 3235 Steps preparation at Thursday 4pm", LogicController.getDisplayList());
		
		//t1 has both, t2 has end no start.
		LogicController.process("add 2334 tutorials from Thursday to Saturday", LogicController.getDisplayList());
		LogicController.process("add Do supermarket shopping by Sunday 4pm", LogicController.getDisplayList());
		
		//t1 has both, t2 has neither.
		LogicController.process("add 2103 Handout and Slides in 4 days", LogicController.getDisplayList());
		LogicController.process("add Prepare for cosplay", LogicController.getDisplayList());
		
		//t1, t2 have both.
		LogicController.process("add AFA from 28/11 to 29/11", LogicController.getDisplayList());
		LogicController.process("add 7Steps from 18/11 6pm to 18/11 10pm", LogicController.getDisplayList());
		
		//t1 has neither, t2 has end no start.
		LogicController.process("add Exercise", LogicController.getDisplayList());
		LogicController.process("add Complete 2334 webcast by Sunday 4pm", LogicController.getDisplayList());
				
		//t1 has neither, t2 has start no end.
		LogicController.process("add Reorganise nendoroids", LogicController.getDisplayList());
		LogicController.process("add 2101 Presentation at Saturday 9am", LogicController.getDisplayList());
		
		//t1 has neither, t2 has both.
		LogicController.process("add Enjoy Life", LogicController.getDisplayList());
		LogicController.process("add Holiday from 25/12 to 30/12", LogicController.getDisplayList());
						
		//t1, t2 have neither.
		LogicController.process("add Clean up room", LogicController.getDisplayList());
		LogicController.process("add Pack wardrobe", LogicController.getDisplayList());
		
		int length = LogicController.getDisplayList().size();
		for (int i = 0; i < length; i++) {
			LogicController.process("delete 1", LogicController.getDisplayList());
		}

		logger.info("End of comparator tests");
	}
	
}
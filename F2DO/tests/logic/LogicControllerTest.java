package logic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Test;

public class LogicControllerTest {
	
	private final static Logger LOGGER = Logger.getLogger(LogicControllerTest.class.getName());
	
	@Test
	public final void testProcess() {
	    Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		Logger rootLogger = Logger.getLogger("");
	    Handler[] handlers = rootLogger.getHandlers();
	    if (handlers[0] instanceof ConsoleHandler) {
	      rootLogger.removeHandler(handlers[0]);
	    }
	    logger.setLevel(Level.INFO);
	    FileHandler fileTxt = null;
		try {
			fileTxt = new FileHandler("Logging.txt");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

	    SimpleFormatter formatterTxt = new SimpleFormatter();
	    fileTxt.setFormatter(formatterTxt);
	    logger.addHandler(fileTxt);

	    assertEquals("Feedback: testtask has been successfully added!", 
					(LogicController.process("add testtask from friday to saturday", LogicController.getDisplayList())));
		assertEquals("Feedback: 1 has been deleted!",
					(LogicController.process("delete 1", LogicController.getDisplayList())));
		assertEquals("Feedback: testtask2 has been successfully added!", 
					(LogicController.process("add testtask2 by saturday", LogicController.getDisplayList())));
		assertEquals("Feedback: testtask2 has been marked as completed!",
					(LogicController.process("done 1", LogicController.getDisplayList())));
		assertEquals("Feedback: Show 1 complete task!",
					(LogicController.process("show done", LogicController.getDisplayList())));
		assertEquals("Feedback: 1 has been deleted!",
					(LogicController.process("delete 1", LogicController.getDisplayList())));
		assertEquals("Feedback: No incomplete task!",
					(LogicController.process("show undone", LogicController.getDisplayList())));
		//assertEquals("")
		
	}

}



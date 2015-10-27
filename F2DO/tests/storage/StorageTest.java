package storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Test;

public class StorageTest {

	@Test
	public void test() {
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		Logger rootLogger = Logger.getLogger("");
	    Handler[] handlers = rootLogger.getHandlers();
	    
	    if (handlers[0] instanceof ConsoleHandler) {
	      rootLogger.removeHandler(handlers[0]);
	    }
	    
	    FileHandler fileTxt = null;
		
	    try {
			fileTxt = new FileHandler("StorageLogging.txt");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

	    logger.addHandler(fileTxt);
	    SimpleFormatter formatterTxt = new SimpleFormatter();
	    fileTxt.setFormatter(formatterTxt);
	}

}

package storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Before;
import org.junit.Test;

public class StorageTest {

	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Before
	public void loggerSetup() {
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
	
	@Test
	public void test() {
		logger.info("Starting tests");
	    logger.info("End of tests");

	}

}

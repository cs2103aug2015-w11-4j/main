package parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.junit.Before;
import org.junit.Test;

public class DateTimeTest {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Before
	public void loggerSetup() {
		Logger rootLogger = Logger.getLogger("");
	    Handler[] handlers = rootLogger.getHandlers();
	    
	    if (handlers[0] instanceof ConsoleHandler) {
	      rootLogger.removeHandler(handlers[0]);
	    }
	    
	    FileHandler fileTxt = null;
		
	    try {
			fileTxt = new FileHandler("ParserLogging.txt");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}

	    logger.addHandler(fileTxt);
	    SimpleFormatter formatterTxt = new SimpleFormatter();
	    fileTxt.setFormatter(formatterTxt);
	}
	
	@Test
	public void testSlashNumericDMY() {
		assertEquals(getResult(2015, 12, 16, 12, 0), simpleFormat(DateTime.parse("16/12/2015")));
		assertEquals(getResult(2015, 12, 17, 12, 0), simpleFormat(DateTime.parse("17/12/15")));
		assertEquals(getResult(2015, 12, 6, 12, 0), simpleFormat(DateTime.parse("6/12/2015")));
		assertEquals(getResult(2015, 12, 7, 12, 0), simpleFormat(DateTime.parse("07/12/2015")));
		assertEquals(getResult(2015, 2, 16, 12, 0), simpleFormat(DateTime.parse("16/2/2015")));
		assertEquals(getResult(2015, 2, 17, 12, 0), simpleFormat(DateTime.parse("17/02/2015")));
	}
	
	@Test 
	public void testHyphenNumericDMY() {
		assertEquals(getResult(2015, 12, 16, 12, 0), simpleFormat(DateTime.parse("16-12-2015")));
		assertEquals(getResult(2015, 12, 16, 12, 0), simpleFormat(DateTime.parse("16-12-15")));
		assertEquals(getResult(2015, 12, 6, 12, 0), simpleFormat(DateTime.parse("6-12-2015")));
		assertEquals(getResult(2015, 12, 6, 12, 0), simpleFormat(DateTime.parse("06-12-2015")));
		assertEquals(getResult(2015, 2, 16, 12, 0), simpleFormat(DateTime.parse("16-2-2015")));
		assertEquals(getResult(2015, 2, 16, 12, 0), simpleFormat(DateTime.parse("16-02-2015")));
	}
	
	@Test 
	public void testShortDMY() {
		assertEquals(getResult(2015, 12, 16, 12, 0), simpleFormat(DateTime.parse("16 Dec 2015")));
		assertEquals(getResult(2015, 12, 16, 12, 0), simpleFormat(DateTime.parse("16 Dec 15")));
		assertEquals(getResult(2015, 12, 6, 12, 0), simpleFormat(DateTime.parse("6 Dec 2015")));
		assertEquals(getResult(2015, 12, 6, 12, 0), simpleFormat(DateTime.parse("06 Dec 2015")));
		assertEquals(getResult(2015, 2, 8, 12, 0), simpleFormat(DateTime.parse("08 Feb 15")));
	}
	
	@Test
	public void testSlashNumbericDM() {
		assertEquals(getResult(0, 12, 16, 12, 0), simpleFormat(DateTime.parse("16/12")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("6/12")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("06/12")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("16/2")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("16/02")));
	}
	
	@Test
	public void testHyphenNumbericDM() {
		assertEquals(getResult(0, 12, 16, 12, 0), simpleFormat(DateTime.parse("16-12")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("6-12")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("06-12")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("16-2")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("16-02")));
	}
	
	@Test 
	public void testShortDM() {
		assertEquals(getResult(0, 12, 16, 12, 0), simpleFormat(DateTime.parse("16 Dec")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("6 Dec")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("06 Dec")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("16 Feb")));
	}
	
	@Test 
	public void testShortMD() {
		assertEquals(getResult(0, 12, 16, 12, 0), simpleFormat(DateTime.parse("Dec 16")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("Dec 6")));
		assertEquals(getResult(0, 12, 6, 12, 0), simpleFormat(DateTime.parse("Dec 06")));
		assertEquals(getResult(0, 2, 16, 12, 0), simpleFormat(DateTime.parse("Feb 16")));
	}
	
	@Test 
	public void testTime() {
		assertEquals(getResult(0, 0, 0, 15, 0), simpleFormat(DateTime.parse("3pm")));
		assertEquals(getResult(0, 0, 0, 8, 0), simpleFormat(DateTime.parse("8am")));
		assertEquals(getResult(0, 0, 0, 17, 30), simpleFormat(DateTime.parse("17:30")));
		assertEquals(getResult(0, 0, 0, 17, 30), simpleFormat(DateTime.parse("17.30")));
	}
	
	@Test
	public void testMixure() {
		assertEquals(getResult(0, 11, 4, 16, 0), simpleFormat(DateTime.parse("Nov 4 4pm")));
		assertEquals(getResult(0, 2, 16, 7, 0), simpleFormat(DateTime.parse("16 Feb 7am")));
		
		// THESE THREE FAILED
		assertEquals(getResult(0, 11, 4, 8, 30), simpleFormat(DateTime.parse("Nov 4 8.30")));
		assertEquals(getResult(0, 2, 16, 22, 10), simpleFormat(DateTime.parse("16 Feb 22:10")));
		assertEquals(getResult(2015, 2, 17, 22, 10), simpleFormat(DateTime.parse("17 Feb 2015 22:10")));
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
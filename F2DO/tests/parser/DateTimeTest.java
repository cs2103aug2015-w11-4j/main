//@@author Yu Ting
package parser;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import org.junit.Test;

import date.DateTime;

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
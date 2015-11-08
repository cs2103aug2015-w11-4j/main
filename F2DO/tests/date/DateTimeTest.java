package date;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import org.junit.Test;

import date.DateTime;

//@@author A0118005W
public class DateTimeTest {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Test
	public void testSlashNumericDMY() {
		
		logger.info("Starting SlashNumericDMY test");

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Calendar cal = Calendar.getInstance();
		
		cal.set(2015, 11, 16);
		String testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16/12/2015").getDate()));
		
		cal.set(2015, 11, 17);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("17/12/15").getDate()));
		
		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("6/12/2015").getDate()));
		
		cal.set(2015, 11, 7);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("07/12/2015").getDate()));
		
		cal.set(2015, 1, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16/2/2015").getDate()));
		
		cal.set(2015, 1, 17);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("17/02/2015").getDate()));

		logger.info("Successful end of SlashNumericDMY test");
	}
	
	@Test 
	public void testHyphenNumericDMY() {
	    
	    logger.info("Starting HyphenNumericDMY test");

	    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Calendar cal = Calendar.getInstance();
		
		cal.set(2015, 11, 16);
		String testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16-12-2015").getDate()));

		cal.set(2015, 11, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16-12-15").getDate()));

		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("6-12-2015").getDate()));

		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("06-12-2015").getDate()));

		cal.set(2015, 1, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16-2-2015").getDate()));

		cal.set(2015, 1, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16-02-2015").getDate()));

	    logger.info("Successful end of HyphenNumericDMY test");
	}
	
	@Test 
	public void testShortDMY() {
	    
		logger.info("Starting ShortDMY test");

	    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Calendar cal = Calendar.getInstance();
		
		cal.set(2015, 11, 16);
		String testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16 Dec 2015").getDate()));

		cal.set(2015, 11, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16 Dec 15").getDate()));

		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("6 Dec 2015").getDate()));

		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("06 Dec 2015").getDate()));

		cal.set(2015, 1, 8);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("08 Feb 15").getDate()));

	    logger.info("Successful end of ShortDMY test");
	}
	
	@Test
	public void testSlashNumbericDM() {
	    
		logger.info("Starting SlashNumbericDM test");

	    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Calendar cal = Calendar.getInstance();
		
		cal.set(2015, 11, 16);
		String testDate = sdf.format(cal.getTime());
	    assertEquals(testDate, simpleFormat(DateTime.parse("16/12").getDate()));
	    
	    cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("6/12").getDate()));
		
		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("06/12").getDate()));
		
		cal.set(2015, 1, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16/2").getDate()));
		
		cal.set(2015, 1, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16/02").getDate()));
	
	    logger.info("Successful end of SlashNumbericDM test");
	}
	
	@Test
	public void testHyphenNumbericDM() {
	    
		logger.info("Starting HyphenNumbericDM test");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Calendar cal = Calendar.getInstance();
		
		cal.set(2015, 11, 16);
		String testDate = sdf.format(cal.getTime());
	    assertEquals(testDate, simpleFormat(DateTime.parse("16-12").getDate()));

		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("6-12").getDate()));

		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("06-12").getDate()));

		cal.set(2015, 1, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16-2").getDate()));

		cal.set(2015, 1, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16-02").getDate()));

		logger.info("Successful end of HyphenNumbericDM test");
	}
	
	@Test 
	public void testShortDM() {
	    
		logger.info("Starting ShortDM test");

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Calendar cal = Calendar.getInstance();
		
		cal.set(2015, 11, 16);
		String testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16 Dec").getDate()));

		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("6 Dec").getDate()));

		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("06 Dec").getDate()));

		cal.set(2015, 1, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16 Feb").getDate()));
		
		logger.info("Successful end of ShortDM test");
	}
	
	@Test 
	public void testShortMD() {
	    
		logger.info("Starting ShortMD test");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Calendar cal = Calendar.getInstance();
		
		cal.set(2015, 11, 16);
		String testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("Dec 16").getDate()));
		
		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("Dec 6").getDate()));
		
		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("Dec 06").getDate()));
		
		cal.set(2015, 1, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("Feb 16").getDate()));

		logger.info("Successful end of ShortMD test");
	}
	
	@Test
	public void testLongMD() {
		
		logger.info("Starting LongMD test");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Calendar cal = Calendar.getInstance();
		
		cal.set(2015, 11, 16);
		String testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("December 16").getDate()));
		
		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("December 6").getDate()));		
		
		cal.set(2015, 11, 6);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("December 06").getDate()));
		
		cal.set(2015, 1, 16);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("February 16").getDate()));
		
		logger.info("Successful end of LongMD test");

	}
	
	@Test 
	public void testTime() {
	    
		logger.info("Starting Time test");

	    assertEquals(getResult(0, 0, 0, 15, 0), simpleFormat(DateTime.parse("3pm").getDate()));
		assertEquals(getResult(0, 0, 0, 8, 0), simpleFormat(DateTime.parse("8am").getDate()));
		assertEquals(getResult(0, 0, 0, 17, 30), simpleFormat(DateTime.parse("17:30").getDate()));
		assertEquals(getResult(0, 0, 0, 17, 30), simpleFormat(DateTime.parse("17.30").getDate()));
	
		logger.info("Successful end of Time test");
	}
	
	@Test
	public void testMixture() {
	    
		logger.info("Starting Mixture test");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Calendar cal = Calendar.getInstance();
		
		cal.set(2015, 10, 4, 16, 0);
		String testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("Nov 4 4pm").getDate()));
		
		cal.set(2015, 1, 16, 7, 0);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16 Feb 7am").getDate()));
		
		cal.set(2015, 10, 4, 8, 30);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("Nov 4 8.30").getDate()));
		
		cal.set(2015, 1, 16, 22, 10);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("16 Feb 22:10").getDate()));
		
		cal.set(2015, 1, 17, 22, 10);
		testDate = sdf.format(cal.getTime());
		assertEquals(testDate, simpleFormat(DateTime.parse("17 Feb 2015 22:10").getDate()));
	
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
		
		System.out.println(givenCalendar.getTime().toString());
		
		return simpleFormat(givenCalendar.getTime());
	}
	
	private String simpleFormat(Date date) {
		DateFormat simpleDate = new SimpleDateFormat("dd MMM yyyy HH:mm");
		return simpleDate.format(date);
	}

}
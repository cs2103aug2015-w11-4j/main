//@@author Yu Ting
/**
 * 
 */
package parser;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
		assertEquals("Read a book", result.getContent());
		assertEquals(TaskType.FLOATING, result.getType());
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(null, result.getEndDate());
		assertEquals(null, result.getStartDate());
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
		
		assertEquals("Read maths book", result.getContent());
		assertEquals(TaskType.DEADLINE,result.getType());
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(date.toString(), result.getEndDate().toString());
		assertEquals(null, result.getStartDate());
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
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(null, result.getEndDate());
		assertEquals(date.toString(), result.getStartDate().toString());
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
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(date2.toString(), result.getEndDate().toString());
		assertEquals(date1.toString(), result.getStartDate().toString());
	}
	/**
	 * Input 5: "add Go to market"
	 */
	@Test
	public void testParserAddInput5() {
		result = Parser.parse("add Go to market", new ArrayList<Task>());
		assertEquals("Go to market", result.getContent());
		assertEquals(TaskType.FLOATING, result.getType());
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(null, result.getEndDate());
		assertEquals(null, result.getStartDate());
	}
	/**
	 * Input 6: "add Run marathon from Clementi to Bugis"
	 */
	@Test
	public void testParserAddInput6() {
		result = Parser.parse("add Run marathon from Clementi to Bugis", new ArrayList<Task>());
		assertEquals("Run marathon from Clementi to Bugis", result.getContent());
		assertEquals(TaskType.FLOATING, result.getType());
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(null, result.getEndDate());
		assertEquals(null, result.getStartDate());
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
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(null, result.getEndDate());
		assertEquals(date.toString(), result.getStartDate().toString());
	}
	
	/**
	 * Input 8: "add Walk home from market"
	 */
	@Test
	public void testParserAddInput8() {
		result = Parser.parse("add Walk home from market", new ArrayList<Task>());
		assertEquals("Walk home from market", result.getContent());
		assertEquals(TaskType.FLOATING, result.getType());
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(null, result.getEndDate());
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
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(date.toString(), result.getEndDate().toString());
		assertEquals(null, result.getStartDate());
	} 
	
	/**
	 * 
	 */
	@Test
	public void testParserDelInput1() {
		result = Parser.parse("del 1", new ArrayList<Task>());
		assertEquals("1", result.getContent());
		assertEquals(TaskType.FLOATING, result.getType());
		assertEquals(CommandType.DELETE, result.getCommand());
		assertEquals(null, result.getEndDate());
		assertEquals(null, result.getStartDate());
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
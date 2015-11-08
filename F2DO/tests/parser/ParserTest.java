package parser;

import static org.junit.Assert.*;

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

//@@author A0108511
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
		
		Date today = Calendar.getInstance().getTime();
		int weekOfYear = Integer.parseInt(new SimpleDateFormat("w").format(new java.util.Date()));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND,0);
		Date testEndDate = cal.getTime();
		
		while (testEndDate.before(today)) {
			weekOfYear++;
			cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
			testEndDate = cal.getTime();
		} 
		
		assertEquals("Read maths book", result.getContent());
		assertEquals(TaskType.DEADLINE,result.getType());
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(testEndDate.toString(), result.getEndDate().toString());
		assertEquals(null, result.getStartDate());
	}
	/**
	 * Input 3: "add Maths exam on thu"
	 */
	@Test
	public void testParserAddInput3() {
		result = Parser.parse("add Maths exam on thu", new ArrayList<Task>());
		
		Date today = Calendar.getInstance().getTime();
		int weekOfYear = Integer.parseInt(new SimpleDateFormat("w").format(new java.util.Date()));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		Date testStartDate = cal.getTime();
		
		while (testStartDate.before(today)) {
			weekOfYear++;
			cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
			testStartDate = cal.getTime();
		} 
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		Date testEndDate = cal.getTime();
		
		assertEquals("Maths exam", result.getContent());
		assertEquals(TaskType.EVENT, result.getType());
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(testStartDate.toString(), result.getStartDate().toString());
		assertEquals(testEndDate.toString(), result.getEndDate().toString());
	}
	/**
	 * Input 4: "add Camping trip from wed to mon"
	 */
	@Test
	public void testParserAddInput4() {
		result = Parser.parse("add Camping trip from wed to mon", new ArrayList<Task>());
		Date today = Calendar.getInstance().getTime();
		int weekOfYear = Integer.parseInt(new SimpleDateFormat("w").format(new java.util.Date()));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.WEDNESDAY);
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		Date testStartDate = cal.getTime();
		
		while (testStartDate.before(today)) {
			weekOfYear++;
			cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
			testStartDate = cal.getTime();
		} 
		
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		Date testEndDate = cal.getTime();
		
		while (testEndDate.before(testStartDate)) {
			weekOfYear++;
			cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
			testEndDate = cal.getTime();
		}
		
		assertEquals("Camping trip", result.getContent());
		assertEquals(TaskType.EVENT, result.getType());
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(testStartDate.toString(), result.getStartDate().toString());
		assertEquals(testEndDate.toString(), result.getEndDate().toString());
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
		
		Date today = Calendar.getInstance().getTime();
		int weekOfYear = Integer.parseInt(new SimpleDateFormat("w").format(new java.util.Date()));
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.THURSDAY);
		cal.set(Calendar.HOUR_OF_DAY, 16);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		Date testStartDate = cal.getTime();
		
		while (testStartDate.before(today)) {
			weekOfYear++;
			cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
			testStartDate = cal.getTime();
		} 
		
		assertEquals("Watch a movie", result.getContent());
		assertEquals(TaskType.EVENT, result.getType());
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(testStartDate.toString(), result.getStartDate().toString());
		assertEquals(null, result.getEndDate());
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
		cal.set(Calendar.HOUR_OF_DAY, 8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		Date testStartDate = cal.getTime();
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND,0);
		Date testEndDate = cal.getTime();
		
		result = Parser.parse("add test in two days", new ArrayList<Task>());
		assertEquals("test", result.getContent());
		assertEquals(TaskType.EVENT, result.getType());
		assertEquals(CommandType.ADD, result.getCommand());
		assertEquals(testStartDate.toString(), result.getStartDate().toString());
		assertEquals(testEndDate.toString(), result.getEndDate().toString());
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
	
}
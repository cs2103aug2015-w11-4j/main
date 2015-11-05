//@@author Yu Ting
package parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import object.Result;
import type.KeywordType;


public class KeywordHelper {
	
	public static TreeMap<Integer, KeywordType> getKeywordIndex(ArrayList<String> splitWords) {
		TreeMap<Integer, KeywordType> keywordIndex = new TreeMap<Integer, KeywordType>();

		for (int i = 0; i < splitWords.size(); i++) {
			String word = splitWords.get(i);
			KeywordType toType = KeywordType.toType(word);
			
			if (toType != KeywordType.INVALID) {
				keywordIndex.put(i, toType);
			}
		}
		
		return keywordIndex;
	}
	
	public static HashMap<KeywordType, IKeyword> getFunctions(String input) {
		HashMap<KeywordType, IKeyword> functions = new HashMap<KeywordType, IKeyword>();
		
		functions.put(KeywordType.AT, new KeywordAt(input));
		functions.put(KeywordType.ON, new KeywordOn(input));
		functions.put(KeywordType.FROM, new KeywordFromTo(input));
		functions.put(KeywordType.IN, new KeywordIn(input));
		functions.put(KeywordType.BY, new KeywordByDue(input));
		functions.put(KeywordType.DUE, new KeywordByDue(input));
		functions.put(KeywordType.TOMORROW, new KeywordDay(input));
		functions.put(KeywordType.YESTERDAY, new KeywordDay(input));
		
		return functions;
	}
	
	// analyze Only On (day)
	public static Result analyzeOneInfo(boolean isStartDate, String dateTimeString) {
		Date dateTime = DateTime.parse(dateTimeString);
		Result res = new Result();
		
		if (isStartDate) {
			res.setStartDate(dateTime);
			return res;
		} else {
			res.setEndDate(dateTime);
			return res;
		}
	}
	
	public static Result analyzeTwoInfo(boolean isStartDate, String title, 
			String dateTimeString) {
		Date dateTime = DateTime.parse(dateTimeString);
		
		if (isStartDate) {
			return new Result(rmWhitespace(title), dateTime, null);
		} else {
			return new Result(rmWhitespace(title), null, dateTime);
		}
	}
	
	public static Result analyzeThreeInfo(String title, String startDateString, 
			String endDateString) {
		Date startDate = DateTime.parse(startDateString);
		Date endDate = DateTime.parse(endDateString);
		
		if (startDate != null && endDate != null) {
			endDate = DateTime.getLaterEndDate(startDate, endDate);
		}
		
		return new Result(rmWhitespace(title), startDate, endDate);
	}
	
	public static Result analyzeThreeInfo(boolean isStartDate, String title, 
			String timeString, String dateString) {
		Date time = DateTime.parse(timeString);
		Date date = DateTime.parse(dateString);
		
		if (time != null && date != null) {
			Date dateTime = DateTime.combineDateTime(date, time);
			if (isStartDate) {
				return new Result(rmWhitespace(title), dateTime, null);
			} else {
				return new Result(rmWhitespace(title), null, dateTime);
			}
		}
		
		return new Result(rmWhitespace(title), null, null);
	}
	
	public static Result analyzeFourInfo(String title, String startTimeString, 
			String endTimeString, String dateString) {
		Date startDate = null;
		Date endDate = null;
		Date startTime = DateTime.parse(startTimeString);
		Date endTime = DateTime.parse(endTimeString);
		Date date = DateTime.parse(dateString);
		
		if (startTime != null && date != null) {
			startDate = DateTime.combineDateTime(date, startTime);
		}
		
		if (endTime != null && date != null) {
			endDate = DateTime.combineDateTime(date, endTime);
		}
		
		if (startTime == null && endTime == null && date != null) {
			startDate = date;
		}
		
		if (startDate != null && endDate != null) {
			endDate = DateTime.getLaterEndDate(startDate, endDate);
		}
		
		return new Result(rmWhitespace(title), startDate, endDate);
	}
	
	private static String rmWhitespace(String string) {
		return string.replaceAll("\\s+$", "");
	}
}

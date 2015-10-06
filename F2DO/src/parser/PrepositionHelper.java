package parser;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import type.PrepositionType;


public class PrepositionHelper {
	
	public static TreeMap<Integer, PrepositionType> getKeywordIndex(String input) {
		TreeMap<Integer, PrepositionType> keywordIndex = new TreeMap<Integer, PrepositionType>();
		List<String> splitWords = Arrays.asList(input.split(" "));
		
		for (PrepositionType type: PrepositionType.values()) {
			String keyword = type.toString().toLowerCase();
			int index = splitWords.indexOf(keyword);

			if (index >= 0) {
				keywordIndex.put(index, type);
			}
		}
		
		/*for(int key: keywordIndex.keySet()) {
			System.out.println("key: " + key + " value: " + keywordIndex.get(key));
		}*/
		
		return keywordIndex;
	}
	
	public static HashMap<PrepositionType, IPreposition> getFunctions(String input) {
		HashMap<PrepositionType, IPreposition> functions = new HashMap<PrepositionType, IPreposition>();
		
		functions.put(PrepositionType.AT, new PrepositionAt(input));
		functions.put(PrepositionType.ON, new PrepositionOn(input));
		functions.put(PrepositionType.FROM, new PrepositionFromTo(input));
		functions.put(PrepositionType.IN, new PrepositionIn(input));
		functions.put(PrepositionType.BY, new PrepositionBy(input));
		
		
		/*ON,
		FROM,
		IN,
		BY,
		UNTIL,
		TOMORROW,
		YESTERDAY*/
		return functions;
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
		
		return new Result(rmWhitespace(title), startDate, endDate);
	}
	
	private static String rmWhitespace(String string) {
		return string.replaceAll("\\s+$", "");
	}
}

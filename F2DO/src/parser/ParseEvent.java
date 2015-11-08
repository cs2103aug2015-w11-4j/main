package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import date.DatePair;
import date.DateTime;
import type.DayType;
import type.KeywordType;

public class ParseEvent implements IParseDateTime {
	private static final String JOIN_DELIMITER = " ";
	private static final String SPLIT_DELIMITER = "\\s+";
	private static final String REPLACE_DELIMITER = "";
	
	private String _input = null;
	private final int _flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;

	public ParseEvent(String input) {
		_input = input;
	}

	@Override
	public DatePair analyze() {
		String timeStr = DateTime.getTime(_input);
		Date startDate = DateTime.parse(_input);
		Date endDate = null;
		
		if (isFrom()) {
			DatePair result = analyzeKeywordFrom();
			startDate = result.getStartDate();
			endDate = result.getEndDate();
		} 
		
		// If no time is given, set default event time from 8am to 11pm
		if (timeStr == null) {
			Date startTime = DateTime.parse("8:00");
			Date endTime = DateTime.parse("23:00");
			
			startDate = DateTime.combineDateTime(startDate, startTime);
			if (endDate != null) {
				endDate = DateTime.combineDateTime(endDate, endTime);
			} else {
				endDate = DateTime.combineDateTime(startDate, endTime);
			}
		}
		
		// If the entered date RANGE has passed, get the date(s) next year
		Date now = new Date();
		if (startDate != null && startDate.compareTo(now) < 0) {
			if (endDate != null && endDate.compareTo(now) < 0) {
				startDate = DateTime.getOneYearLater(startDate);
				endDate = DateTime.getOneYearLater(endDate);
			} else if (endDate == null) {
				startDate = DateTime.getOneYearLater(startDate);
			}
		}
		
		// Get the string contains date time format
		DatePair datePair = new DatePair(startDate, endDate);
		datePair.setDateString(getDateStr());
		
		return datePair;
	}
	
	private String getDateStr() {
		ArrayList<String> words = new ArrayList<>(Arrays.asList(_input.split(SPLIT_DELIMITER)));
		TreeMap<Integer, KeywordType> keywordIndices = ParserHelper.getKeywordIndex(words);
		ArrayList<Integer> indexList = new ArrayList<Integer>(keywordIndices.keySet());
		int listSize = indexList.size();
		String dateTimeStr = _input;
		
		for (int i = 0; i < listSize; i++) {
			int index = indexList.get(i);
			String impossibleStr = "";
			
			if (i < (listSize - 1)) {
				int nextIndex = indexList.get(i + 1);
				impossibleStr = String.join(JOIN_DELIMITER, words.subList(index, nextIndex));
			} else {
				impossibleStr = String.join(JOIN_DELIMITER, words.subList(index, words.size()));
			}
			
			if (DateTime.parse(impossibleStr) == null) {
				dateTimeStr = dateTimeStr.replace(impossibleStr, REPLACE_DELIMITER);
			}
		}
		
		// Reformat the string
		words = new ArrayList<>(Arrays.asList(dateTimeStr.split(SPLIT_DELIMITER)));
		dateTimeStr = String.join(JOIN_DELIMITER, dateTimeStr);
		
		return dateTimeStr;
	}
	
	private DatePair analyzeKeywordFrom() {
		Date startDate = null;
		Date endDate = null;
		int functionNo = 0;
		
		loop: while(true) {
			DatePair pair = new DatePair(null, null);
			
			switch(functionNo) {
				case 0:
					pair = analyzeFromToOn();
					break;
				case 1:
					pair = analyzeFromOn();
					break;
				case 2:
					pair = analyzeFrom();
					break;
				default:
					break loop;
			}
			
			if (pair.getStartDate() != null || pair.getEndDate() != null) {
				startDate = pair.getStartDate();
				endDate = pair.getEndDate();
				break loop;
			}
			
			functionNo += 1;
		}
		
		return new DatePair(startDate, endDate);
	}
	
	private DatePair analyzeFromToOn() {
		String regexFromToOn = "from (.*?) to (.*) on (.*?)";
		Pattern pattern = Pattern.compile(regexFromToOn, _flags);
		Matcher matcher = pattern.matcher(_input);
		Date startDate = null;
		Date endDate = null;
		
		if (matcher.matches()) {
			Date startTime = DateTime.parse(matcher.group(1));
			Date endTime = DateTime.parse(matcher.group(2));
			Date date = DateTime.parse(matcher.group(3));
			
			if (startTime != null && date != null) {
				startDate = DateTime.combineDateTime(date, startTime);
			}
			
			if (endTime != null && date != null) {
				endDate = DateTime.combineDateTime(date, endTime);
			}
			
			if (startTime == null && endTime == null && date != null) {
				startDate = date;
			}
		}
		
		return new DatePair(startDate, endDate);
	}
	
	private DatePair analyzeFromOn() {
		String regexFromTo = "from (.*?) to (.*)";
		Pattern pattern = Pattern.compile(regexFromTo, _flags);
		Matcher matcher = pattern.matcher(_input);
		Date startDate = null;
		Date endDate = null;
		
		if (matcher.matches()) {
			startDate = DateTime.parse(matcher.group(1));
			endDate = DateTime.parse(matcher.group(2));
			
			if (endDate != null && startDate != null && 
					endDate.compareTo(startDate) < 0) {
				String[] startWords = matcher.group(1).split("\\s");
				String[] endWords = matcher.group(2).split("\\s");
				boolean isDayForStart = false;
				boolean isDayForEnd = false;
				
				for (int i = 0; i < startWords.length && !isDayForStart; i++) {
					if (DayType.toDay(startWords[i]) != DayType.INVALID) {
						isDayForStart = true;
					}
				}
				
				for (int i = 0; i < endWords.length && !isDayForEnd; i++) {
					if (DayType.toDay(endWords[i]) != DayType.INVALID) {
						isDayForEnd = true;
					}
				}
				
				if (isDayForStart && isDayForEnd) {
					endDate = DateTime.getOneWeekLater(endDate);
				}
			}
		}
		return new DatePair(startDate, endDate);
	}
	
	private DatePair analyzeFrom() {
		String regexFrom = "from (.*?)";
		Pattern pattern = Pattern.compile(regexFrom, _flags);
		Matcher matcher = pattern.matcher(_input);
		Date startDate = null;
		
		if (matcher.matches()) {
			startDate = DateTime.parse(_input);
		}
		
		return new DatePair(startDate, null);
	}
	
	private boolean isFrom() {
		String[] tokens = _input.split(SPLIT_DELIMITER);
		
		if (tokens.length > 0) {
			KeywordType keyword = KeywordType.toType(tokens[0]);
			
			if (keyword == KeywordType.FROM) {
				return true;
			}
		}
		return false;
	}
}

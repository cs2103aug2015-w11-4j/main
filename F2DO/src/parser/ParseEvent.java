package parser;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import date.DatePair;
import date.DateTime;
import type.DayType;
import type.KeywordType;

public class ParseEvent implements IParseDateTime {
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
		
		Date today = DateTime.getToday();
		
		if (startDate != null && startDate.compareTo(today) < 0) {
			startDate = DateTime.getOneYearLater(startDate);
			
			if (endDate != null) {
				endDate = DateTime.getOneYearLater(endDate);
			}
		}
		
		return new DatePair(startDate, endDate);
	}
	
	private DatePair analyzeKeywordFrom() {
		String regexFromToOn = "from (.*?) to (.*) on (.*?)";
		String regexFromTo = "from (.*?) to (.*)";
		String regexFrom = "from (.*?)";
		Date startDate = null;
		Date endDate = null;
		boolean isFound = false;
		
		Pattern pattern = Pattern.compile(regexFromToOn, _flags);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Date startTime = DateTime.parse(matcher.group(1));
			Date endTime = DateTime.parse(matcher.group(2));
			Date date = DateTime.parse(matcher.group(3));
			
			isFound = true;
			
			if (startTime != null && date != null) {
				startDate = DateTime.combineDateTime(date, startTime);
			}
			
			if (endTime != null && date != null) {
				endDate = DateTime.combineDateTime(date, endTime);
			}
			
			if (startTime == null && endTime == null && date != null) {
				startDate = date;
			}
			
			if (startTime == null && endTime == null && startDate == null && endTime == null) {
				isFound = false;
			}
		}
		
		pattern = Pattern.compile(regexFromTo, _flags);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches() && !isFound) {
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
			
			if (startDate != null || endDate != null) {
				isFound = true;
			}
		}
		
		pattern = Pattern.compile(regexFrom, _flags);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches() && !isFound) {
			startDate = DateTime.parse(_input);
		}
		
		Date now = new Date();
		
		if (startDate != null) {
			if (startDate.compareTo(now) < 0) {
				startDate = DateTime.getOneYearLater(startDate);
				
				if (endDate != null) {
					endDate = DateTime.getOneYearLater(endDate);
				}
			}
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
		String[] tokens = _input.split(" ");
		
		if (tokens.length > 0) {
			KeywordType keyword = KeywordType.toType(tokens[0]);
			
			if (keyword == KeywordType.FROM) {
				return true;
			}
		}
		return false;
	}
}

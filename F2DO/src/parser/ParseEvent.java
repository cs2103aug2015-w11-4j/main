package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import date.DatePair;
import date.DateTime;
import date.ParsedDate;
import type.DayType;
import type.KeywordType;

//@@author A0118005W
public class ParseEvent implements IParseDateTime {
	private static final String JOIN_DELIMITER = " ";
	private static final String SPLIT_DELIMITER = "\\s+";
	private static final String REPLACE_DELIMITER = "";
	
	private String _input = null;
	private final int _flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
	private boolean _isAbsoluteDate = false;
	private boolean _isValidDate = false;

	public ParseEvent(String input) {
		_input = input;
	}

	@Override
	public DatePair analyze() {
		String timeStr = DateTime.getTime(_input);
		ParsedDate result = DateTime.parse(_input);
		Date startDate = result.getDate();
		Date endDate = null;
		
		_isAbsoluteDate = result.isAbsolute();
		_isValidDate = result.isValid();
		
		if (isFrom()) {
			DatePair pair = analyzeKeywordFrom();
			startDate = pair.getStartDate();
			endDate = pair.getEndDate();
		} 
		
		// If no time is given, set default event time from 8am to 11pm
		if (timeStr == null) {
			Date startTime = DateTime.parse("08:00").getDate();
			Date endTime = DateTime.parse("23:00").getDate();
			
			startDate = DateTime.combineDateTime(startDate, startTime);
			if (endDate != null) {
				endDate = DateTime.combineDateTime(endDate, endTime);
			} else {
				endDate = DateTime.combineDateTime(startDate, endTime);
			}
		}
		
		// If the entered date RANGE has passed, get the date(s) next year
		Date now = new Date();
		if (startDate != null && !_isAbsoluteDate && startDate.compareTo(now) < 0) {
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
		
		if (!_isValidDate) {
			datePair.setErrorMsg(ParserHelper.ERROR_INVALID_DATE);
			System.out.println(datePair.isError());
		}
		
		return datePair;
	}
	
	/**
	 * Analyze the string that contains date or time format.
	 * @return date or time string
	 */
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
			
			if (DateTime.parse(impossibleStr).getDate() == null) {
				dateTimeStr = dateTimeStr.replace(impossibleStr, REPLACE_DELIMITER);
			}
		}
		
		// Reformat the string
		words = new ArrayList<>(Arrays.asList(dateTimeStr.split(SPLIT_DELIMITER)));
		dateTimeStr = String.join(JOIN_DELIMITER, dateTimeStr);
		
		return dateTimeStr;
	}
	
	/**
	 * Analyze string that begins with 'from'.
	 * @return start date and end date of parsing result
	 */
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
	
	/**
	 * Analyze the string that begins with 'from' and contains 'to' and 'on'.
	 * @return start date and end date of parsing result
	 */
	private DatePair analyzeFromToOn() {
		String regexFromToOn = "from (.*?) to (.*) on (.*?)";
		Pattern pattern = Pattern.compile(regexFromToOn, _flags);
		Matcher matcher = pattern.matcher(_input);
		Date startDate = null;
		Date endDate = null;
		
		if (matcher.matches()) {
			Date startTime = DateTime.parse(matcher.group(1)).getDate();
			Date endTime = DateTime.parse(matcher.group(2)).getDate();
			
			ParsedDate dateResult = DateTime.parse(matcher.group(3));
			Date date = dateResult.getDate();

			//_isRelativeDate = DateTime.isRelativeDate(matcher.group(3));
			_isAbsoluteDate = dateResult.isAbsolute();
			_isValidDate = dateResult.isValid();
			
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
	
	/**
	 * Analyze the string that begins with 'from' and contains 'on'.
	 * @return start date and end date of parsing result
	 */
	private DatePair analyzeFromOn() {
		String regexFromTo = "from (.*?) to (.*)";
		Pattern pattern = Pattern.compile(regexFromTo, _flags);
		Matcher matcher = pattern.matcher(_input);
		Date startDate = null;
		Date endDate = null;
		
		if (matcher.matches()) {
			ParsedDate startResult = DateTime.parse(matcher.group(1));
			ParsedDate endResult = DateTime.parse(matcher.group(2));
			startDate = startResult.getDate();
			endDate = endResult.getDate();
			
			_isAbsoluteDate = startResult.isAbsolute() || endResult.isAbsolute();
			_isValidDate = startResult.isValid() && endResult.isValid();
			
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
	
	/**
	 * Analyze the string that begins with 'from' only.
	 * @return start date and end date of parsing result
	 */
	private DatePair analyzeFrom() {
		String regexFrom = "from (.*?)";
		Pattern pattern = Pattern.compile(regexFrom, _flags);
		Matcher matcher = pattern.matcher(_input);
		Date startDate = null;
		
		if (matcher.matches()) {
			ParsedDate result = DateTime.parse(_input);
			startDate = result.getDate();
			_isAbsoluteDate = result.isAbsolute();
			_isValidDate = result.isValid();
		}
		
		return new DatePair(startDate, null);
	}
	
	/**
	 * Check if the first word is the keyword, 'from'.
	 * @return true if first word is 'from'; false otherwise
	 */
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

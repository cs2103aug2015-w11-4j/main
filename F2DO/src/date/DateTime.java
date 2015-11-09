package date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.joestelmach.natty.Parser;

import type.MonthType;

//@@author A0118005W
public class DateTime extends DateTimeHelper {
	private static final int DAY = 0;
	private static final int MONTH = 1;
	private static final int YEAR = 2;
	private static final int DATE_SIZE = 3;
	private static final int DAY_MONTH_SIZE = 2;
	private static final int DAY_MONTH_YEAR_SIZE = 3;
	private static final String JOIN_DELIMITER = "-";
	
	private static final String SHORT_MONTH = "jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec";
	private static final String LONG_MONTH = "january|february|march|april|may|june|"
			+ "july|august|september|october|november|december";
	private static final String MONTH_REGEX = SHORT_MONTH + "|" + LONG_MONTH;
	
	private static final Parser dateParser = new Parser();
	private static final int _flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
	private static final HashMap<MonthType, Integer> months = new HashMap<MonthType, Integer>();
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	static {
		months.clear();
		
		months.put(MonthType.JAN, 1);
		months.put(MonthType.JANUARY, 1);
		
		months.put(MonthType.FEB, 2);
		months.put(MonthType.FEBRUARY, 2);
		
		months.put(MonthType.MAR, 3);
		months.put(MonthType.MARCH, 3);
		
		months.put(MonthType.APR, 4);
		months.put(MonthType.APRIL, 4);
		
		months.put(MonthType.MAY, 5);
		
		months.put(MonthType.JUN, 6);
		months.put(MonthType.JUNE, 6);
		
		months.put(MonthType.JUL, 7);
		months.put(MonthType.JULY, 7);
		
		months.put(MonthType.AUG, 8);
		months.put(MonthType.AUGUST, 8);
		
		months.put(MonthType.SEP, 9);
		months.put(MonthType.SEPTEMBER, 9);
		
		months.put(MonthType.OCT, 10);
		months.put(MonthType.OCTOBER, 10);
		
		months.put(MonthType.NOV, 11);
		months.put(MonthType.NOVEMBER, 11);
		
		months.put(MonthType.DEC, 12);
		months.put(MonthType.DECEMBER, 12);
	}

	/**
	 * Parse the date with the given input.
	 * @param input - text
	 * @return date, exact date string and error message
	 */
	public static ParsedDate parse(String input) {
		ParsedDate result = new ParsedDate();
		try {
			String dateStr = getDate(input);
			String timeStr = getTime(input);
			String parsedStr = input;
			Date date = null;
			
			if (dateStr != null) {
				if (timeStr != null) {
					dateStr += " " + timeStr;
				}
				parsedStr = dateStr;
			}
			date = dateParser.parse(parsedStr).get(0).getDates().get(0);
			dateStr = dateParser.parse(input).get(0).getText();
			
			if (date != null) {
				boolean isAbsolute = isAbsoluteDate(parsedStr);
				boolean isValid = isValidDate(parsedStr);
				result = new ParsedDate(date, isValid, isAbsolute);
				result.setDateString(dateStr);
			}

		} catch (Exception e) {
			return new ParsedDate();
		}
		return result;
	}
	
	/**
	 * Get the time from the input string.
	 * @param input - possible time string
	 * @return time string if the pattern is matched
	 */
	public static String getTime(String input) {
		String regexAmPm = ".*?([0-9]{1,2})\\s?(am|pm).*";
		String regexAmPm2 = ".*?([0-9]{1,2})[:.]([0-9]{1,2})\\s?(am|pm).*";
		String regexColon = ".*?([0-9]{1,2})[:.]([0-9]{1,2}).*";
		
		Pattern pattern = Pattern.compile(regexAmPm, _flags);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			if(isValidTime(matcher.group(1), null)) {
				return matcher.group(1) + matcher.group(2);
			}
		}
		
		pattern = Pattern.compile(regexAmPm2);
		matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			if(isValidTime(matcher.group(1), matcher.group(2))) {
				return matcher.group(1) + ":" + matcher.group(2) + matcher.group(3);
			}
		}
		
		pattern = Pattern.compile(regexColon);
		matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			if(isValidTime(matcher.group(1), matcher.group(2))) {
				return matcher.group(1) + ":" + matcher.group(2);
			}
		}
		return null;
	}
	
	/**
	 * Get the date from the input string.
	 * @param input - possible date string
	 * @return date string if the pattern is matched
	 */
	private static String getDate(String input) {
		String regexTextMD1 = ".*?(" + MONTH_REGEX + ")[ /-]([0-9]{1,2})\\s.*";
		String regexTextMD2 = ".*?(" + MONTH_REGEX + ")[ /-]([0-9]{1,2})";
		String regexTextMDY1 = ".*?(" + MONTH_REGEX + ")[ /-]([0-9]{1,2})[ ,/-]\\s?([0-9]{2,4})\\s.*";
		String regexTextMDY2 = ".*?(" + MONTH_REGEX + ")[ /-]([0-9]{1,2})[ ,/-]\\s?([0-9]{2,4})";
		String[] allRegex = {regexTextMD1, regexTextMD2, regexTextMDY1, regexTextMDY2};
		
		String date = getAmericanDate(input);
		
		if (date == null) {
			for (int i = 0; i < allRegex.length; i++) {
				String regex = allRegex[i];
				Pattern pattern = Pattern.compile(regex, _flags);
				Matcher matcher = pattern.matcher(input);
				
				if (matcher.matches()) {
					int groupNum = matcher.groupCount();
					ArrayList<String> words = new ArrayList<String>();
					
					for (int j = 1; j <= groupNum; j++) {
						String word = matcher.group(j);
						
						if (j == 1 && !isConvertable(word)) {
							word = months.get(MonthType.toMonth(word)).toString();
						}
						words.add(word);
					}
					date = String.join(JOIN_DELIMITER, words);
				}
			}
		}
		
		return date;
	}
	
	/**
	 * Check if the given string contains absolute date 
	 * (which includes year).
	 * @param input - date string
	 * @return true if it contains year; false otherwise
	 */
	private static boolean isAbsoluteDate(String input) {
		if (input == null) {
			return false;
		}
		
		Pattern pattern = Pattern.compile("([0-9]{1,2})-([0-9]{1,2})-([0-9]{2,4})", _flags);
		Matcher matcher = pattern.matcher(input);
		
		return matcher.matches();
	}
	
	/**
	 * Check if the date is valid.
	 * @param input - date string
	 * @return true if the date is valid; false otherwise
	 */
	private static boolean isValidDate(String input) {
		if (input == null) {
			return false;
		}
		
		String[] formats = {"MM-dd-yyyy", "MM-dd"};
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		boolean isDate = false;
		
		Pattern pattern1 = Pattern.compile("([0-9]{1,2})-([0-9]{1,2})-([0-9]{2,4})", _flags);
		Matcher matcher1 = pattern1.matcher(input);
		
		Pattern pattern2 = Pattern.compile("([0-9]{1,2})-([0-9]{1,2})", _flags);
		Matcher matcher2 = pattern2.matcher(input);
		
		isDate = matcher1.matches() || matcher2.matches();
		
		if (!isDate) {
			return true;
		}
		
		for (int i = 0; i < formats.length; i++) {
			try {
				dateFormat = new SimpleDateFormat(formats[i]);
				dateFormat.setLenient(false);
				dateFormat.parse(input);
				return true;
			} catch (ParseException e) {}
		}
		return false;
	}
	
	/**
	 * Check if the time is valid.
	 * @param hourStr - hour string
	 * @param minStr - minute string
	 * @return true if the time is valid
	 */
	private static boolean isValidTime(String hourStr, String minStr) {
		try {
			int hour = Integer.parseInt(hourStr);
			
			if (hour > 23) {
				return false;
			}
			
			if (minStr != null) {
				int min = Integer.parseInt(minStr);
				if (min > 59) {
					return false;
				}
			}
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Convert the British date format into American date format, "MMM DD YYYY" or "MMM DD YY".
	 * @param input - possible British date format input
	 * @return standard date format
	 */
	private static String getAmericanDate(String input) {
		String regexNumbericDM = ".*?([0-9]{1,2})[/-]([0-9]{1,2}).*";
		String regexTextDM1 = ".*?([0-9]{1,2})[ /-](" + MONTH_REGEX + ")\\s.*";
		String regexTextDM2 = ".*?([0-9]{1,2})[ /-](" + MONTH_REGEX + ")";
		String regexNumericDMY = ".*?([0-9]{1,2})[/-]([0-9]{1,2})[/-]([0-9]{2,4}).*";
		String regexTextDMY1 = ".*?([0-9]{1,2})[ /-](" + MONTH_REGEX + ")[ ,/-]\\s?([0-9]{2,4})\\s.*";
		String regexTextDMY2 = ".*?([0-9]{1,2})[ /-](" + MONTH_REGEX + ")[ ,/-]\\s?([0-9]{2,4})";
				
		String[] twoGroups = {regexNumbericDM, regexTextDM1, regexTextDM2 };
		String[] threeGroups = {regexNumericDMY, regexTextDMY1, regexTextDMY2};
		
		for (int i = 0; i < threeGroups.length; i++) {
			String result = getDateMatcher(input, threeGroups[i], DAY_MONTH_YEAR_SIZE);
			
			if (result != null) {
				return result;
			}
		}
	
		for (int i = 0 ; i< twoGroups.length; i++) {
			String result = getDateMatcher(input, twoGroups[i], DAY_MONTH_SIZE);
			
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Get the string that contains the date format.
	 * @param input - possible date string
	 * @param regex - regular expression to determine the date
	 * @param groupNumber - number of groups in the regular expression
	 * @return string contains date
	 */
	private static String getDateMatcher(String input, String regex, int groupNumber) {
		String dateTime = null;
		String[] dayMonthYear = new String[DATE_SIZE];
		Arrays.fill(dayMonthYear, "");

		Pattern pattern = Pattern.compile(regex, _flags);
		Matcher matcher = pattern.matcher(input);

		if (matcher.matches()) {
			dateTime = "";
			for (int i = 0; i < groupNumber; i++) {
				dayMonthYear[i] = matcher.group(i + 1);
			}

			if (groupNumber >= DAY_MONTH_SIZE) {
				String monthStr = dayMonthYear[MONTH].toLowerCase();
				if (!isConvertable(monthStr)) {
					MonthType month = MonthType.toMonth(monthStr);
					if(months.containsKey(month)) {
						dayMonthYear[MONTH] = months.get(month).toString();
					}
				}
				
				dateTime += dayMonthYear[MONTH] + JOIN_DELIMITER;
				dateTime += dayMonthYear[DAY];
			}

			if (groupNumber == DAY_MONTH_YEAR_SIZE) {
				dateTime += JOIN_DELIMITER + dayMonthYear[YEAR];
			}
		}

		return dateTime;
	}
	
	/**
	 * Check if the input is able to convert to integer.
	 * @param number - number string
	 * @return true if it is convertible; false otherwise
	 */
	private static boolean isConvertable(String number) {
		try {
			Integer.parseInt(number);
		} catch (NumberFormatException e) {
			logger.info("Month format was MMM or month.");
			return false;
		}
		return true;
	}
}
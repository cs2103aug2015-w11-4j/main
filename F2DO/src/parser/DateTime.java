package parser;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.utils.Span;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTime {
	private static final int DAY = 0;
	private static final int MONTH = 1;
	private static final int YEAR = 2;
	private static final int DATE_SIZE = 3;
	private static final int DAY_MONTH_SIZE = 2;
	private static final int DAY_MONTH_YEAR_SIZE = 3;
	private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	/**
	 * Analyze input and return standard date format.
	 * @param input - possible date input
	 * @return standard date format
	 */
	public static Date parse(String input) {
		return parse(input, true);
	}
	
	/**
	 * Combine the date and time into a standard date format.
	 * @param date 
	 * @param time
	 * @return standard date format
	 */
	public static Date combineDateTime(Date date, Date time) {
		Calendar calendar = Calendar.getInstance();
		Calendar dateCalendar = Calendar.getInstance();
		Calendar timeCalendar = Calendar.getInstance();
		
		calendar.clear();
		dateCalendar.setTime(date);
		timeCalendar.setTime(time);
		
		calendar.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
		calendar.set(Calendar.DATE, dateCalendar.get(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
		
		return calendar.getTime();
	}
	
	/**
	 * Analyze input and return standard date format with the option of date in 
	 * British or American date format.
	 * @param input - possible date input
	 * @param isBritish - British or American format
	 * @return standard date format
	 */
	private static Date parse(String input, boolean isBritish) {		
		if (isBritish) {
			return parseBritish(input);
		} else {
			return parseAmerican(input);
		}
	}
	
	/**
	 * Analyze the input in American date format. 
	 * @param input - possible American date format
	 * @return standard date format
	 */
	private static Date parseAmerican(String input) {
		Date dateTime = null;
		Span parsedDateTime = Chronic.parse(input);
		
		//System.out.println("parsedAmerican: " + parsedDateTime);
		
		if(parsedDateTime != null) {
			dateTime = parsedDateTime.getEndCalendar().getTime();
		}
		
		return dateTime;
	}
	
	/**
	 * Analyze the input in British date format.
	 * @param input - possible British date format input
	 * @return standard date format
	 */
	private static Date parseBritish(String input) {
		String date = getAmericanDate(input);
		String time = getTime(input);
		
		if (date == null && time == null) {
			return parseAmerican(input);
		} else {
			String dateTime = "";
			
			if (date != null) {
				dateTime += date + " ";
			} else {
				Date tempDate = parseAmerican(input);
				
				if (tempDate != null) {
					Calendar dateCalendar = Calendar.getInstance();
					dateCalendar.setTime(tempDate);
					
					dateTime += dateCalendar.get(Calendar.YEAR) + "-" + 
							(dateCalendar.get(Calendar.MONTH) + 1) + "-" + 
							dateCalendar.get(Calendar.DATE) + " ";
				}
			}
			
			if (time != null) {
				dateTime += time;
			}
			
			System.out.println(dateTime);
			
			return parseAmerican(dateTime);
		}
	}
	
	/**
	 * Convert the British date format into American date format, "MMM DD YYYY" or "MMM DD YY".
	 * @param input - possible British date format input
	 * @return standard date format
	 */
	private static String getAmericanDate(String input) {
		String shortMonth = "jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec";
		String regexNumbericDM = ".*?([0-9]{1,2})[/-]([0-9]{1,2}).*";
		String regexShortDM = ".*?([0-9]{1,2})[ /-](" + shortMonth + ").*";
		String regexNumericDMY = ".*?([0-9]{1,2})[/-]([0-9]{1,2})[/-]([0-9]{2,4}).*";
		String regexShortDMY = ".*?([0-9]{1,2})[ /-](" + shortMonth + ")[ /-]([0-9]{2,4}).*";
		
		String[] twoGroups = {regexNumbericDM, regexShortDM};
		String[] threeGroups = {regexNumericDMY, regexShortDMY};
		
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
	 * 
	 * @param input
	 * @param regex
	 * @param groupNumber
	 * @return
	 */
	private static String getDateMatcher(String input, String regex, int groupNumber) {
		String dateTime = null;
		String[] dayMonthYear = new String[DATE_SIZE];
		Arrays.fill(dayMonthYear, "");
		
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			dateTime = "";
			for (int i = 0; i < groupNumber; i++) {
				dayMonthYear[i] = matcher.group(i + 1);
			}
			
			if (groupNumber >= DAY_MONTH_SIZE) {
				if (isConvertable(dayMonthYear[MONTH])) {
					int monthIndex = Integer.parseInt(dayMonthYear[MONTH]) - 1;

					if (monthIndex < MONTHS.length) {
						dateTime += MONTHS[monthIndex] + " ";
					}
				} else {
					dateTime += dayMonthYear[MONTH] + " ";
				}
				
				dateTime += dayMonthYear[DAY] + " ";
			}
			
			if (groupNumber == DAY_MONTH_YEAR_SIZE) {
				dateTime += dayMonthYear[YEAR];
			}
		}
		
		return dateTime;
	}
	
	private static String getTime(String input) {
		String regexAmPm = ".*?([0-9]{1,2})(am|pm).*";
		String regexColon = ".*?([0-9]{1,2}:[0-9]{1,2}).*";
		
		Pattern pattern = Pattern.compile(regexAmPm, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			return matcher.group(1) + matcher.group(2);
		}
		
		pattern = Pattern.compile(regexColon);
		matcher = pattern.matcher(input);
		
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return null;
	}
	
	private static boolean isConvertable(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static void main(String[] args) {
		parseBritish("fjdiaf 2/4/1992 dfjdif");
		parseBritish("16-04-1992");
		parseBritish("16/12/15");
		parseBritish("17-Apr-1992");
		parseBritish("17-Apr-92");
		parseBritish("fjdifd 18/Apr/1992 dfjidf");
		parseBritish("19 Apr 1992");
		parseBritish("20 Apr");
		parseBritish("dfdjaif 21 Apr fdjifd");
		
		parseBritish("4pm");
		parseBritish("2:30");
		parseBritish("6 Nov 2pm");
		parseBritish("Nov 7 2pm");
	}
}


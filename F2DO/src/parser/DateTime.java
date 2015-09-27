package parser;

import com.mdimension.jchronic.Chronic;
import com.mdimension.jchronic.utils.Span;

import java.util.Calendar;
import java.util.Date;

public class DateTime {
	public static Date parse(String input) {
		Date dateTime = null;
		Span parsedDateTime = Chronic.parse(input);
		//System.out.println("parsedDateTime: "+parsedDateTime);
		
		if(parsedDateTime != null) {
			dateTime = parsedDateTime.getEndCalendar().getTime();
		} else {
			
		}
		
		//System.out.println("test: " + Chronic.parse("Sep 30 4pm 2015"));	
		
		return dateTime;
	}
	
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
}


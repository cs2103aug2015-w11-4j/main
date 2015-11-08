package date;

import java.util.Calendar;
import java.util.Date;

public class DateTimeHelper {
	public static Date getOneWeekLater(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			calendar.add(Calendar.DAY_OF_MONTH, 7);
			date = calendar.getTime();
		}
		return date;
	}
	
	public static Date getOneYearLater(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			calendar.add(Calendar.YEAR, 1);
			date = calendar.getTime();
		}
		return date;
	}
	
	public static Date getToday() {
		Calendar todayCalendar = Calendar.getInstance();
		
		// Initialize today
		todayCalendar.set(Calendar.HOUR_OF_DAY, 23);
		todayCalendar.set(Calendar.MINUTE, 59);
		todayCalendar.set(Calendar.SECOND, 59);
		
		return todayCalendar.getTime();
	}
	
	/**
	 * Combine the date and time into a standard date format.
	 * @param date 
	 * @param time
	 * @return standard date format
	 */
	public static Date combineDateTime(Date date, Date time) {
		if (date != null && time != null) {
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
		return null;
	}
}

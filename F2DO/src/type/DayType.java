package type;

import java.util.logging.Logger;

public enum DayType {
	MONDAY,
	TUESDAY,
	WEDNESDAY,
	THURSDAY,
	FRIDAY,
	SATURDAY,
	SUNDAY,
	MON,
	TUE,
	TUES,
	WED,
	THU,
	THUR,
	THURS,
	FRI,
	SAT,
	SUN,
	INVALID;
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static DayType toDay(String word) {
		try {
			word = word.toUpperCase();
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID;
		}
	}
}

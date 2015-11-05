package type;

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
	
	public static DayType toDay(String word) {
		try {
			word = word.toUpperCase();
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID;
		}
	}
}

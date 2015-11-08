package type;

//@@author A0118005W
public enum MonthType {
	JAN,
	FEB,
	MAR,
	APR,
	MAY,
	JUN,
	JUL,
	AUG,
	SEP,
	OCT,
	NOV,
	DEC,
	JANUARY,
	FEBRUARY,
	MARCH,
	APRIL,
	JUNE,
	JULY,
	AUGUST,
	SEPTEMBER,
	OCTOBER,
	NOVEMBER,
	DECEMBER,
	INVALID;
	
	public static MonthType toMonth(String word) {
		try {
			word = word.toUpperCase();
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID;
		}
	}
	
}

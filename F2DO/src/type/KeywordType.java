package type;

//@@author A0118005W
public enum KeywordType {
	AT,
	ON,
	FROM,
	IN,
	BY,
	DUE,
	TODAY,
	TOMORROW,
	TONIGHT,
	THIS,
	NEXT,
	INVALID;
	
	public static KeywordType toType(String word) {
		try {
			word = word.toUpperCase();
			
			if (word.equals("TMR")) {
				return TOMORROW;
			}
			
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID; 
		}
	}
}

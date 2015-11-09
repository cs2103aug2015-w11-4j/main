package type;

//@@author A0118005W
public enum KeywordType {
	AT,
	ON,
	FROM,
	IN,
	BY,
	DUE,
	INVALID;
	
	public static KeywordType toType(String word) {
		try {
			word = word.toUpperCase();	
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID; 
		}
	}
}

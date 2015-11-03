//@@author Yu Ting
package type;

public enum KeywordType {
	AT,
	ON,
	FROM,
	IN,
	BY,
	DUE,
	UNTIL,
	TOMORROW,
	YESTERDAY,
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
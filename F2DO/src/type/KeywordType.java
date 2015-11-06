package type;

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
<<<<<<< HEAD
	NEXT,
	INVALID;
=======
	INVALID,
	NEXT;
>>>>>>> 5420cf62e6ed8b3db2ef4e033a437238d43a7bbc
	
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

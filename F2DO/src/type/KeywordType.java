package type;

import java.util.logging.Logger;

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

	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static KeywordType toType(String word) {
		try {
			word = word.toUpperCase();
			
			if (word.equals("TMR")) {
				return TOMORROW;
			}
			
			return valueOf(word); 
		} catch (Exception e) {
			logger.info("An invalid keyword type was specified.");
			return INVALID; 
		}
	}
}

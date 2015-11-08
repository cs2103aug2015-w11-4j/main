package type;

import java.util.logging.Logger;

public enum ShowType {
	ALL,
	DONE,
	UNDONE,
	INVALID;
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static ShowType toType(String word) {
		try {
			word = word.toUpperCase();
			return valueOf(word); 
		} catch (Exception e) {
			logger.info("An invalid show type was specified.");
			return INVALID; 
		}
	}
}
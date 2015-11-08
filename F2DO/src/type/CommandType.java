package type;

import java.util.logging.Logger;

public enum CommandType {
	ADD,
	EDIT,
	DELETE,
	SEARCH,
	SHOW,
	DONE,
	UNDONE,
	HELP,
	HOME,
	INVALID, 
	CAT, 
	MOVE;
	
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static CommandType toCmd(String word) {
		try {
			word = word.toUpperCase();
			if (word.equals("DEL")) {
				return DELETE;
			}
			
			return valueOf(word); 
		} catch (Exception e) {
			logger.info("An invalid command was detected.");
			return INVALID; 
		}
	}
}
package type;

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
	
	public static CommandType toCmd(String word) {
		try {
			word = word.toUpperCase();
			if (word.equals("DEL")) {
				return DELETE;
			}
			
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID; 
		}
	}
}
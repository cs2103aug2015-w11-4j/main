package type;

public enum CommandType {
	ADD,
	EDIT,
	DELETE,
	SEARCH,
	COMPLETE,
	INCOMPLETE,
	HELP,
	HOME,
	INVALID;
	
	public static CommandType toCmd(String word) {
		try {
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID; 
		}
	}
}

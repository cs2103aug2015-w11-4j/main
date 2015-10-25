package type;

public enum ShowType {
	ALL,
	DONE,
	UNDONE,
	INVALID;
	
	public static ShowType toType(String word) {
		try {
			return valueOf(word); 
		} catch (Exception e) {
			return INVALID; 
		}
	}
}

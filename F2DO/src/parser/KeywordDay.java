package parser;

public class KeywordDay implements IKeyword {
	private static String _input = null;
	
	public KeywordDay(String input) {
		_input = input;
	}
	
	public Result analyze() {
		
		return new Result(null, null, null);
	}

}

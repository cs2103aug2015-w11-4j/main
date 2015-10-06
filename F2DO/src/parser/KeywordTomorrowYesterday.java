package parser;

public class KeywordTomorrowYesterday implements IKeyword {
	private static String _input = null;
	
	public KeywordTomorrowYesterday(String input) {
		_input = input;
	}
	
	public Result analyze() {
		
		return new Result(null, null, null);
	}

}

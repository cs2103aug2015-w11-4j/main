package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordBy implements IKeyword {
	private static String _input = null;
	
	public KeywordBy(String input) {
		_input = input;
	}
	
	public Result analyze() {
		String regexBy = "(.*?) by (.*?)";
		
		Pattern pattern = Pattern.compile(regexBy);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeTwoInfo(false, 
															matcher.group(1), 
															matcher.group(2));
			return result;
		}
		
		return new Result(null, null, null);
	}
}

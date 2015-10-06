package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordIn implements IKeyword {
	private String _input = null;
	
	public KeywordIn(String input) {
		_input = input;
	}

	public Result analyze() {
		String regexIn = "(.*?) in (.*?)";
		
		Pattern pattern = Pattern.compile(regexIn);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeTwoInfo(false, 
															matcher.group(1), 
															"in " + matcher.group(2));
			return result;
		}
		
		
		return new Result(null, null, null);
	}

}

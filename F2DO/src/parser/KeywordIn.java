package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Result;

public class KeywordIn implements IKeyword {
	private String _input = null;
	
	public KeywordIn(String input) {
		_input = input;
	}
	
	/**
	 * Analyze the pattern that contains 'in'.
	 * Return the analyzing result.
	 */
	public Result analyze() {
		String regexIn = "(.*?) in (.*?)";
		
		final int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
		
		Pattern pattern = Pattern.compile(regexIn, flags);
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

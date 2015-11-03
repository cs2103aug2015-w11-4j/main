//@@author Yu Ting
package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Result;

public class KeywordOn implements IKeyword {
	
	private String _input = null;
	
	public KeywordOn(String input) {
		_input = input;
	}
	
	/**
	 * Analyze the pattern that contains 'on'.
	 * Return the analyzing result.
	 */
	public Result analyze() {
		String regexOnFromTo = "(.*?) on (.*?) from (.*) to (.*?)";
		String regexOn = "(.*?) on (.*?)";
		String regexOnlyOn = "on (.*?)";
		
		final int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
		
		Pattern pattern = Pattern.compile(regexOnFromTo, flags);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeFourInfo(matcher.group(1),
					matcher.group(3),
					matcher.group(4),
					matcher.group(2));
			return result;
		}
		
		pattern = Pattern.compile(regexOn, flags);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeTwoInfo(true, 
					matcher.group(1), 
					matcher.group(2));
			return result;
		}
		
		pattern = Pattern.compile(regexOnlyOn, flags);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeOneInfo(true, 
					matcher.group(1));
			return result;
		}
		
		return new Result(null, null, null);
	}
}

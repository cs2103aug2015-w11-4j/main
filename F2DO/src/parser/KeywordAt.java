//@@author Yu Ting
package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Result;

public class KeywordAt implements IKeyword {
	private static String _input = null;
	
	public KeywordAt(String input) {
		_input = input;
	}
	
	/**
	 * Analyze the pattern that contains 'at'.
	 * Return the analyzing result.
	 */
	public Result analyze() {
		String regexAtOn = "(.*?) at (.*?) on (.*)";
		String regexAt = "(.*?) at (.*?)";
		
		final int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
		
		Pattern pattern = Pattern.compile(regexAtOn, flags);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeThreeInfo(true, 
					matcher.group(1),
					matcher.group(2),
					matcher.group(3));
			return result;
		}
		
		pattern = Pattern.compile(regexAt, flags);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeTwoInfo(true, 
					matcher.group(1), 
					matcher.group(2));
			return result;
		}
		
		return new Result(null, null, null);
	}
}

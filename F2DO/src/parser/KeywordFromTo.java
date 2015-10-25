package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Result;

public class KeywordFromTo implements IKeyword{
	private static String _input = null;
	
	public KeywordFromTo(String input) {
		_input = input;
	}
	
	/**
	 * Analyze the pattern that contains 'from' and 'to'.
	 * Return the analyzing result.
	 */
	public Result analyze() {
		String regexFromToOn = "(.*?) from (.*?) to (.*) on (.*?)";
		String regexFromTo = "(.*?) from (.*?) to (.*)";
		
		Pattern pattern = Pattern.compile(regexFromToOn);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeFourInfo(matcher.group(1),
					matcher.group(2),
					matcher.group(3),
					matcher.group(4));
			return result;
		}
		
		pattern = Pattern.compile(regexFromTo);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeThreeInfo(matcher.group(1), 
					matcher.group(2), 
					matcher.group(3));
			return result;
		}
		
		return new Result(null, null, null);
	}
}

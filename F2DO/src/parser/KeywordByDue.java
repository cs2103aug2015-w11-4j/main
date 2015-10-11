package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordByDue implements IKeyword {
	private static String _input = null;
	
	public KeywordByDue(String input) {
		_input = input;
	}
	
	/**
	 * Analyze the pattern that contains 'by' and 'due'.
	 * Return the analyzing result.
	 */
	public Result analyze() {
		String regexBy = "(.*?) by (.*?)";
		String regexDue = "(.*?) due (.*?)";
		
		String[] regex = {regexBy, regexDue};
		
		for (int i = 0; i < regex.length; i++) {
			Result regexResult = getMatcher(regex[i]);
			
			if (regexResult != null) {
				return regexResult;
			}
		}
		
		return new Result(null, null, null);
	}
	
	private Result getMatcher(String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeTwoInfo(false, 
					matcher.group(1), 
					matcher.group(2));
			return result;
		}
		
		return null;
	}
}

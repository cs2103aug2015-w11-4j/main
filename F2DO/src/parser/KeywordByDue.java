package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.Result;

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
		String regexDueOn = "(.*?) due on (.*?)";
		String regexOnlyBy = "by (.*?)";
		String regexOnlyDue = "due (.*?)";
		String regexOnlyDueOn = "due on (.*?)";
		
		String[] regex1 = {regexBy, regexDue, regexDueOn};
		String[] regex2= { regexOnlyBy, regexOnlyDue, regexOnlyDueOn};
		
		for (int i = 0; i < regex1.length; i++) {
			Result regexResult = getMatcher(regex1[i]);
			
			if (regexResult != null) {
				return regexResult;
			}
		}
		
		for (int i = 0; i < regex1.length; i++) {
			Result regexResult = getMatcher2(regex2[i]);
			
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
	
	private Result getMatcher2(String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = KeywordHelper.analyzeOneInfo(false, 
					matcher.group(1));
			return result;
		}
		
		return null;
	}
}

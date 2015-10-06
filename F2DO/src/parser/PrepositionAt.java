package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrepositionAt implements IPreposition {
	private static String _input = null;
	
	public PrepositionAt(String input) {
		_input = input;
	}
	
	public Result analyze() {
		String regexAtOn = "(.*?) at (.*?) on (.*)";
		String regexAt = "(.*?) at (.*?)";
		
		Pattern pattern = Pattern.compile(regexAtOn);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = PrepositionHelper.analyzeThreeInfo(true, 
												matcher.group(1),
												matcher.group(2),
												matcher.group(3));
			return result;
		}
		
		pattern = Pattern.compile(regexAt);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = PrepositionHelper.analyzeTwoInfo(true, 
															matcher.group(1), 
															matcher.group(2));
			return result;
		}
		
		return new Result(null, null, null);
	}
}

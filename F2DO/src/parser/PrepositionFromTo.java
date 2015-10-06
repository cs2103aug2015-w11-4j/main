package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrepositionFromTo implements IPreposition{
	private static String _input = null;
	
	public PrepositionFromTo(String input) {
		_input = input;
	}
	
	public Result analyze() {
		String regexFromToOn = "(.*?) from (.*?) to (.*) on (.*?)";
		String regexFromTo = "(.*?) from (.*?) to (.*)";
		
		Pattern pattern = Pattern.compile(regexFromToOn);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = PrepositionHelper.analyzeFourInfo(matcher.group(1),
																matcher.group(2),
																matcher.group(3),
																matcher.group(4));
			return result;
		}
		
		pattern = Pattern.compile(regexFromTo);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = PrepositionHelper.analyzeThreeInfo(matcher.group(1), 
																matcher.group(2), 
																matcher.group(3));
			return result;
		}
		
		return new Result(null, null, null);
	}
}

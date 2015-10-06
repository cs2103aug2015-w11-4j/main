package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrepositionOn implements IPreposition {
	
	private String _input = null;
	
	public PrepositionOn(String input) {
		_input = input;
	}
	
	public Result analyze() {
		String regexOnFromTo = "(.*?) on (.*?) from (.*) to (.*?)";
		String regexOn = "(.*?) on (.*?)";
		
		Pattern pattern = Pattern.compile(regexOnFromTo);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = PrepositionHelper.analyzeFourInfo(matcher.group(1),
																matcher.group(3),
																matcher.group(4),
																matcher.group(2));
			return result;
		}
		
		pattern = Pattern.compile(regexOn);
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

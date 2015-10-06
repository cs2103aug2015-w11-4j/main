package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrepositionIn implements IPreposition {
	private String _input = null;
	
	public PrepositionIn(String input) {
		_input = input;
	}

	public Result analyze() {
		String regexIn = "(.*?) in (.*?)";
		
		Pattern pattern = Pattern.compile(regexIn);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = PrepositionHelper.analyzeTwoInfo(false, 
															matcher.group(1), 
															"in " + matcher.group(2));
			return result;
		}
		
		
		return new Result(null, null, null);
	}

}

package parser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrepositionOn implements IPreposition {
	//private List<String> _splitWords = null;
	
	private String _input = null;
	
	public PrepositionOn(String input) {
		//_splitWords = Arrays.asList(input.split(" on "));
		_input = input;
	}
	
	public Result analyze() {
		Result result = null;
		
		Pattern patternFromToOn = Pattern.compile("(.*?)from(.*?)to(.*)on(.*?)");
		//Pattern patternFromToOn = Pattern.compile("from\\w+");
		//Pattern patternOnFromTo = Pattern.compile("(?<1>..)on(?<2>..)from(?<3>..)to(?<4>..)");
		//Pattern patternOn = Pattern.compile("(?<1>..)on(?<2>..)");
		
		Pattern[] patterns = { patternFromToOn };//, patternOnFromTo, patternOn};
		
		for (int i = 0; i < patterns.length; i++) {
			Pattern pattern = patterns[i];
			Matcher matcher = pattern.matcher(_input);
			
			if (matcher.matches()) {
				System.out.println("index: " + i);
				System.out.println(matcher.group(1).replaceAll("\\s+$", ""));
				System.out.println(matcher.group(2).replaceAll("\\s+$", ""));
				System.out.println(matcher.group(3).replaceAll("\\s+$", ""));
				System.out.println(matcher.group(4).replaceAll("\\s+$", ""));
				break;
			}
		}
		
		return result;
	}
	
	/*public Result analyze() {
		String title = null;
		Date startDate = null;
		Date endDate = null;
		
		if (_splitWords.size() > 0) {
			title = _splitWords.get(0);
		}
		
		if (_splitWords.size() > 1) {
			String secondString = _splitWords.get(1);
			startDate = DateTime.parse(secondString);
			
			if (startDate == null) {
				Result result = furtherAnalyze(secondString);
				startDate = result.getStartDate();
				endDate = result.getEndDate();
			}
		}
		
		return new Result(title, startDate, endDate);
	}
	
	private static Result furtherAnalyze(String input) {
		Date startDate = null;
		Date endDate = null;
		
		List<String> parsedString = Arrays.asList(input.split(" "));
		
		if (parsedString.contains("from")) {	
			PrepositionFromTo prepFromTo = new PrepositionFromTo(input);
			Result tempResult = prepFromTo.analyze();
			Date date = DateTime.parse(tempResult.getTitle());
			
			if (tempResult.getStartDate() != null) {
				startDate = DateTime.combineDateTime(date, tempResult.getStartDate());
			}
			
			if (tempResult.getEndDate() != null) {
				endDate = DateTime.combineDateTime(date, tempResult.getEndDate());
			}
		}
		
		return new Result(null, startDate, endDate);
	}*/
}

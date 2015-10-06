package parser;

import java.util.Date;
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
		//Result result = null;
		String regexFromToOn = "(.*?)from(.*?)to(.*)on(.*?)";
		String regexOnFromTo = "(.*?)on(.*?)from(.*)to(.*?)";
		String regexOn = "(.*?)on(.*?)";
		
		Pattern pattern = Pattern.compile(regexFromToOn);
		Matcher matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = analyzeFromToOn(matcher.group(1).replaceAll("\\s+$", ""),
											matcher.group(2),
											matcher.group(3),
											matcher.group(4));
			return result;
		}
		
		pattern = Pattern.compile(regexOnFromTo);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Result result = analyzeFromToOn(matcher.group(1).replaceAll("\\s+$", ""),
											matcher.group(3),
											matcher.group(4),
											matcher.group(2));
			return result;
		}
		
		pattern = Pattern.compile(regexOn);
		matcher = pattern.matcher(_input);
		
		if (matcher.matches()) {
			Date date = DateTime.parse(matcher.group(2));
			return new Result(matcher.group(1), date, null);
		}
		
		//Pattern patternFromToOn = Pattern.compile("(.*?)from(.*?)to(.*)on(.*?)");
		//Pattern patternFromToOn = Pattern.compile("from\\w+");
		//Pattern patternOnFromTo = Pattern.compile("(?<1>..)on(?<2>..)from(?<3>..)to(?<4>..)");
		//Pattern patternOn = Pattern.compile("(?<1>..)on(?<2>..)");
		
		//Pattern[] patterns = { patternFromToOn };//, patternOnFromTo, patternOn};
		
		/*for (int i = 0; i < patterns.length; i++) {
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
		}*/
		
		return new Result(null, null, null);
	}
	
	private static Result analyzeFromToOn(String title, String startTimeString, 
			String endTimeString, String dateString) {
		Date startDate = null;
		Date endDate = null;
		Date startTime = DateTime.parse(startTimeString);
		Date endTime = DateTime.parse(endTimeString);
		Date date = DateTime.parse(dateString);
		
		if (startTime != null && date != null) {
			startDate = DateTime.combineDateTime(date, startTime);
		}
		
		if (endTime != null && date != null) {
			endDate = DateTime.combineDateTime(date, endTime);
		}
		
		return new Result(title, startDate, endDate);
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

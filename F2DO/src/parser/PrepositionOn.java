package parser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PrepositionOn implements IPreposition {
	private List<String> _splitWords = null;
	
	public PrepositionOn(String input) {
		_splitWords = Arrays.asList(input.split(" on "));
	}
	
	public Result analyze() {
		String title = null;
		Date startDate = null;
		Date endDate = null;
		
		if (_splitWords.size() > 0) {
			title = _splitWords.get(0);
			//System.out.println("title2: "+title);
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
		
		List<String> parsedSecondString = Arrays.asList(input.split(" "));
		
		if (parsedSecondString.contains("from")) {	
			PrepositionFromTo prepFromTo = new PrepositionFromTo(input);
			Result secondResult = prepFromTo.analyze();
			Date date = DateTime.parse(secondResult.getTitle());
			
			if (secondResult.getStartDate() != null) {
				startDate = DateTime.combineDateTime(date, secondResult.getStartDate());
			}
			
			if (secondResult.getEndDate() != null) {
				endDate = DateTime.combineDateTime(date, secondResult.getEndDate());
			}
		}
		
		return new Result(null, startDate, endDate);
	}
}

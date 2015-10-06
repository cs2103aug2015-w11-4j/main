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
		String removeOnInput = "";
		Date startDate = null;
		Date endDate = null;
		
		for (int i = 0 ; i < _splitWords.size(); i++) {
			removeOnInput += _splitWords.get(i) + " ";
		}
		
		if (_splitWords.size() > 0) {
			title = _splitWords.get(0);
			//System.out.println("title2: "+title);
		}
		
		if (_splitWords.size() > 1) {
			String secondString = _splitWords.get(1);
			startDate = DateTime.parse(secondString);
			
			if (startDate == null) {
				Result result = furtherAnalyze(removeOnInput);
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
	}
}

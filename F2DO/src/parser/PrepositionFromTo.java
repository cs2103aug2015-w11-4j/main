package parser;

import java.util.Date;

public class PrepositionFromTo implements IPreposition{
	private String[] _splitWords = null;
	
	public PrepositionFromTo(String input) {
		_splitWords = input.split(" from ");
		//for(int i = 0 ; i < _splitWords.length; i++) {
		//	System.out.println(_splitWords[i]);
		//}
	}
	
	public Result analyze() {
		Date startDate = null;
		Date endDate = null;
		String from = null;
		String to = null;
		String title = null;
		
		if (_splitWords.length > 0) {
			title = _splitWords[0];
		}
		
		if (_splitWords.length > 1) {
			if (_splitWords[1].contains("to")) {
				_splitWords = _splitWords[1].split(" to ");
				from = _splitWords[0];
				to = _splitWords[1];
			} else {
				from = _splitWords[1];
			}
		}
		
		startDate = DateTime.parse(from);
		endDate = DateTime.parse(to);
		
		//System.out.println("from: " + startDate);
		//System.out.println("to: " + endDate);
		
		return new Result(title, startDate, endDate);
	}
}

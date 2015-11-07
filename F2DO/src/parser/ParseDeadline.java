package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import type.KeywordType;

public class ParseDeadline implements IParseDateTime {
	private String _input = null;

	public ParseDeadline(String input) {
		_input = input;
	}

	@Override
	public DatePair analyze() {
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(_input.split("\\s")));
		String input = _input;
		
		if (words.size() > 0) {
			KeywordType keyword = KeywordType.toType(words.get(0));
			
			if (keyword == KeywordType.DUE || keyword == KeywordType.BY) {
				words.remove(0);
				input = String.join(" ", words);
			}
		}
		
		Date dateTime = DateTime.parse(input);
		String timeStr = DateTime.getTime(input);
		
		if (timeStr == null) {
			Date time = DateTime.parse("23:59");
			dateTime = DateTime.combineDateTime(dateTime, time);
		}
		
		DatePair datePair = new DatePair(null, dateTime);
		
		if (dateTime != null) {
			datePair.setDateString(_input);
		}
		
		return datePair;
	}

}

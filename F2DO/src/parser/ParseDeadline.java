package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import date.DatePair;
import date.DateTime;
import date.ParsedDate;
import type.KeywordType;

//@@author A0118005W
public class ParseDeadline implements IParseDateTime {
	private static final String JOIN_DELIMITER = " ";
	private static final String SPLIT_DELIMITER = "\\s+";
	
	private String _input = null;

	public ParseDeadline(String input) {
		_input = input;
	}

	@Override
	public DatePair analyze() {
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(_input.split(SPLIT_DELIMITER)));
		String input = _input;
		
		if (words.size() > 0) {
			KeywordType keyword = KeywordType.toType(words.get(0));
			
			if (keyword == KeywordType.DUE || keyword == KeywordType.BY) {
				words.remove(0);
				input = String.join(JOIN_DELIMITER, words);
			}
		}
		
		ParsedDate result = DateTime.parse(input);
		Date dateTime = result.getDate();
		String timeStr = DateTime.getTime(input);
		
		if (timeStr == null) {
			Date time = DateTime.parse("23:59").getDate();
			dateTime = DateTime.combineDateTime(dateTime, time);
		}
		
		Date now = new Date();
		if (dateTime != null && !result.isAbsolute() && dateTime.compareTo(now) < 0) {
			dateTime = DateTime.getOneYearLater(dateTime);
		}
		
		DatePair datePair = new DatePair(null, dateTime);
		
		if (dateTime != null) {
			datePair.setDateString(_input);
		}
		
		System.out.println(result.isValid());
		
		if (!result.isValid()) {
			datePair.setErrorMsg(ParserHelper.ERROR_INVALID_DATE);
		}
		
		return datePair;
	}

}

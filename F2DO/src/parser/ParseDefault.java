package parser;

import java.util.Date;

import date.DatePair;
import date.DateTime;
import date.ParsedDate;

//@@author A0118005W
public class ParseDefault implements IParseDateTime {
	
	private String _input = null;
	
	public ParseDefault(String input) {
		_input = input;
	}

	@Override
	public DatePair analyze() {
		ParsedDate result = DateTime.parse(_input);
		Date startDate = result.getDate();
		Date endDate = null;
		String dateStr = result.getDateString();
		String timeStr = DateTime.getTime(_input);
		
		// If no time is given, set default event time from 8am to 11pm
		if (timeStr == null) {
			Date startTime = DateTime.parse("08:00").getDate();
			Date endTime = DateTime.parse("23:00").getDate();

			startDate = DateTime.combineDateTime(startDate, startTime);
			endDate = DateTime.combineDateTime(startDate, endTime);
		}
		
		if (isInteger(dateStr)) {
			startDate = null;
			endDate = null;
		}
	
		DatePair datePair = new DatePair(startDate, endDate);
		
		if (startDate != null) {
			datePair.setDateString(dateStr);
		}
		
		if (!result.isValid()) {
			datePair.setErrorMsg(ParserHelper.ERROR_INVALID_DATE);
		}
		
		return datePair;
	}
	
	/**
	 * Check if the input string is integer.
	 * @param input - possible integer string
	 * @return true if input is an integer; false otherwise
	 */
	private boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}

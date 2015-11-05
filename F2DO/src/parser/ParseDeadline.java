package parser;

import java.util.Date;

public class ParseDeadline implements IParseDateTime {
	private String _input = null;

	public ParseDeadline(String input) {
		_input = input;
	}

	@Override
	public DatePair analyze() {
		Date dateTime = DateTime.parse(_input);
		String timeStr = DateTime.getTime(_input);
		
		if (timeStr == null) {
			Date time = DateTime.parse("23:59");
			dateTime = DateTime.combineDateTime(dateTime, time);
		}
		
		return new DatePair(null, dateTime);
	}

}

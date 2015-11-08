package parser;

import date.DatePair;

//@@author A0118005W
public class ParseFloating implements IParseDateTime {
	
	public ParseFloating() {
	}

	@Override
	public DatePair analyze() {
		return new DatePair(null, null);
	}

}

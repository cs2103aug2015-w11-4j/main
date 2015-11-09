package parser;

import date.DatePair;
import type.KeywordType;

//@@author A0118005W
public interface IParseDateTime {
	public static IParseDateTime getFunction(KeywordType keywordType, String input) {
		IParseDateTime function = null;
		
		switch (keywordType) {
			case AT: case IN: case ON: case FROM:
				function = new ParseEvent(input);
				break;
			case BY: case DUE:
				function = new ParseDeadline(input);
				break;
			default:
				function = new ParseDefault(input);
				break;
		}
		
		return function;
	}
	
	public DatePair analyze();
}

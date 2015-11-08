package parser;

import date.DatePair;
import type.KeywordType;

public interface IParseDateTime {
	public static IParseDateTime getFunction(KeywordType keywordType, String input) {
		IParseDateTime function = null;
		
		switch (keywordType) {
			case AT: case IN: case ON: case FROM: case THIS: case NEXT:
				case TODAY: case TOMORROW: case TONIGHT:
				function = new ParseEvent(input);
				break;
			case BY: case DUE:
				function = new ParseDeadline(input);
				break;
			default:
				function = new ParseFloating();
				break;
		}
		
		return function;
	}
	
	public DatePair analyze();
}

package parser;

public class ParseFloating implements IParseDateTime {
	
	public ParseFloating() {
	}

	@Override
	public DatePair analyze() {
		return new DatePair(null, null);
	}

}

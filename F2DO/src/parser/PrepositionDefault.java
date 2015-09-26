package parser;

public class PrepositionDefault implements IPreposition {
	String _input = null;
	
	public PrepositionDefault(String input) {
		this._input = input;
	}
	
	public Result analyze() {
		return new Result(_input, null, null);
	}
}

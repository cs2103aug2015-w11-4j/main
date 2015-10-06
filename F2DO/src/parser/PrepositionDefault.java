package parser;

public class PrepositionDefault implements IPreposition {
	String _input = null;
	
	public PrepositionDefault(String input) {
		// remove the redundant spacing
		this._input = input.replaceAll("\\s+$", "");
	}
	
	public Result analyze() {
		return new Result(_input, null, null);
	}
}

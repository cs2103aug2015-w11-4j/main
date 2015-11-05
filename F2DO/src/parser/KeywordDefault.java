//@@author Yu Ting
package parser;

import object.Result;

public class KeywordDefault implements IKeyword {
	String _input = null;
	
	public KeywordDefault(String input) {
		// remove the redundant spacing
		this._input = input.replaceAll("\\s+$", "");
	}
	
	public Result analyze() {
		return new Result(_input, null, null);
	}
}

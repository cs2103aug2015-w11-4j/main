package parser;

import java.util.Date;

public class PrepositionIn implements IPreposition {
	private String _input = null;
	private String[] _splitWords = null;
	
	public PrepositionIn(String input) {
		this._input = input;
		this._splitWords = input.split(" in ");
	}

	public Result analyze() {
		String title = null;
		Date endDate = null;
		
		if (_splitWords.length > 0) {
			title = _splitWords[0];
		}
		
		endDate = DateTime.parse(_input);
		
		return new Result(title, null, endDate);
	}

}

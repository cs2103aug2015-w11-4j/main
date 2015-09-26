package parser;

public class PrepositionAt implements IPreposition {
	private String[] _splitWords = null;
	private String atPhrase = "";
	
	public PrepositionAt(String input) {
		_splitWords = input.split(" at ");
	}
	
	public Result analyze() {
		String title = null;
		
		if (_splitWords.length > 0) {
			title = _splitWords[0];
		}
		return new Result(title, null, null);
	}

}

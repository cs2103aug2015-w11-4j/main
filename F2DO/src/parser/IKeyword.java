//@@author Yu Ting
package parser;

import object.Result;
import type.KeywordType;

import java.util.HashMap;

public interface IKeyword {	
	public static IKeyword parseKeyword(KeywordType keywordType, String input) {
		IKeyword keywordFunc = new KeywordDefault(input);
		HashMap<KeywordType, IKeyword> functions = KeywordHelper.getFunctions(input);

		if (functions.containsKey(keywordType)) {
			keywordFunc = functions.get(keywordType);
		}
		return keywordFunc;
	}
	
	public Result analyze();
}

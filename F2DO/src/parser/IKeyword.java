package parser;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import type.KeywordType;

import java.util.HashMap;
import java.util.Iterator;

public interface IKeyword {
	public static IKeyword parsedPreposition(String input) {
		IKeyword preposition = new KeywordDefault(input);
		
		TreeMap<Integer, KeywordType> keywordIndex = KeywordHelper.getKeywordIndex(input);
		HashMap<KeywordType, IKeyword> functions = KeywordHelper.getFunctions(input);
		Set<Entry<Integer, KeywordType>> entrySet = keywordIndex.entrySet();
		Iterator<Entry<Integer, KeywordType>> setIterator = entrySet.iterator();
		
		if (setIterator.hasNext()) {
			Map.Entry<Integer, KeywordType> entry = setIterator.next();
			KeywordType prepType = entry.getValue();
			
			preposition = functions.get(prepType);
		}
		
		return preposition;
	}
	
	public Result analyze();
}

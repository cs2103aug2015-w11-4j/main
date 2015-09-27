package parser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import type.PrepositionType;

import java.util.HashMap;
import java.util.Iterator;

public interface IPreposition {
	public static IPreposition parsedPreposition(String input) {
		IPreposition preposition = new PrepositionDefault(input);
		
		TreeMap<Integer, PrepositionType> keywordIndex = PrepositionHelper.getKeywordIndex(input);
		HashMap<PrepositionType, IPreposition> functions = PrepositionHelper.getFunctions(input);
		Set<Entry<Integer, PrepositionType>> entrySet = keywordIndex.entrySet();
		Iterator<Entry<Integer, PrepositionType>> setIterator = entrySet.iterator();
		
		if(setIterator.hasNext()) {
			Map.Entry<Integer, PrepositionType> entry = setIterator.next();
			PrepositionType prepType = entry.getValue();
			
			preposition = functions.get(prepType);
		}
		
		return preposition;
	}
	
	public Result analyze();
}

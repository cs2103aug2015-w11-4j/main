package parser;

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
		boolean isFromOn = keywordIndex.containsValue(PrepositionType.FROM) && keywordIndex.containsValue(PrepositionType.ON);
		
		if (isFromOn) {
			preposition = functions.get(PrepositionType.ON);
		}
		
		if (setIterator.hasNext() && !isFromOn) {
			Map.Entry<Integer, PrepositionType> entry = setIterator.next();
			PrepositionType prepType = entry.getValue();
			
			preposition = functions.get(prepType);
		}
		
		return preposition;
	}
	
	public Result analyze();
}

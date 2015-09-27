package parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import type.PrepositionType;


public class PrepositionHelper {
	public static TreeMap<Integer, PrepositionType> getKeywordIndex(String input) {
		TreeMap<Integer, PrepositionType> keywordIndex = new TreeMap<Integer, PrepositionType>();
		List<String> splitWords = Arrays.asList(input.split(" "));
		
		for (PrepositionType type: PrepositionType.values()) {
			String keyword = type.toString().toLowerCase();
			int index = splitWords.indexOf(keyword);

			if (index >= 0) {
				keywordIndex.put(index, type);
			}
		}
		
		/*for(int key: keywordIndex.keySet()) {
			System.out.println("key: " + key + " value: " + keywordIndex.get(key));
		}*/
		
		return keywordIndex;
	}
	
	public static HashMap<PrepositionType, IPreposition> getFunctions(String input) {
		HashMap<PrepositionType, IPreposition> functions = new HashMap<PrepositionType, IPreposition>();
		
		functions.put(PrepositionType.AT, new PrepositionAt(input));
		functions.put(PrepositionType.ON, new PrepositionOn(input));
		functions.put(PrepositionType.FROM, new PrepositionFromTo(input));
		functions.put(PrepositionType.IN, new PrepositionIn(input));
		functions.put(PrepositionType.BY, new PrepositionBy(input));
		
		
		/*ON,
		FROM,
		IN,
		BY,
		UNTIL,
		TOMORROW,
		YESTERDAY*/
		return functions;
	}
}

package parser;

import java.util.ArrayList;
import java.util.TreeMap;

import type.KeywordType;

//@@author A0118005W
public class ParserHelper {
	public static final String ERROR_INVALID_COMMAND = "Feedback: The command entered does not exist!";
	public static final String ERROR_TASK_ID = "Feedback: The entered number does not exist!";
	public static final String ERROR_END_DATE_EARLIER = "Feedback: End date is earlier than start date!";
	public static final String ERROR_INVALID_DATE = "Feedback: The date entered is invalid!";
	
	public static TreeMap<Integer, KeywordType> getKeywordIndex(ArrayList<String> splitWords) {
		TreeMap<Integer, KeywordType> keywordIndex = new TreeMap<Integer, KeywordType>();
		boolean isConsecutive = false;
		for (int i = 0; i < splitWords.size(); i++) {
			String word = splitWords.get(i);
			KeywordType toType = KeywordType.toType(word);
			
			if (toType != KeywordType.INVALID && !isConsecutive) {
				keywordIndex.put(i, toType);
				isConsecutive = true;
			} else {
				isConsecutive = false;
			}
		}
		
		return keywordIndex;
	}
}

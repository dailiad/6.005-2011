package piwords;

import java.util.HashMap;
import java.util.Map;

public class WordFinder {
    /**
     * Given a String (the haystack) and an array of Strings (the needles), return a
     * Map<String, Integer>, where keys in the map correspond to elements of needles
     * that were found as substrings of haystack, and the value for each key is the
     * lowest index of haystack at which that needle was found. A needle that was
     * not found in the haystack should not be returned in the output map.
     * 
     * @param haystack The string to search into.
     * @param needles  The array of strings to search for. This array is not
     *                 mutated.
     * @return The list of needles that were found in the haystack.
     */
    public static Map<String, Integer> getSubstrings(String haystack, String[] needles) {
        Map<String, Integer> subStrings = new HashMap<>();
        char firstChar = 0;
        int matchIndex = 0;
        int fromIndex = 0;
        for (String needle : needles) {
            fromIndex = 0;
            matchIndex = 0;
            if (needle.length() == 0) {
                continue;
            }
            firstChar = needle.charAt(0);
            while (matchIndex < haystack.length()) {
                matchIndex = haystack.indexOf(firstChar, fromIndex);
                if (matchIndex != -1 && matchIndex + needle.length() <= haystack.length()) {
                    String subString = haystack.substring(matchIndex, matchIndex + needle.length());
                    if (subString.equals(needle)) {
                        subStrings.put(needle, matchIndex);
                        break;
                    } else {
                        fromIndex = matchIndex + 1;
                    }
                } else {
                    break;
                }
            }

        }

        return subStrings;
    }
}

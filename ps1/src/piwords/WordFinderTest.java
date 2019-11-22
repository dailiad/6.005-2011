package piwords;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class WordFinderTest {
    @Test
    public void basicGetSubstringsTest() {
        String haystack = "abcde";
        String[] needles = {"ab", "abc", "de", "fg"};

        Map<String, Integer> expectedOutput = new HashMap<String, Integer>();
        expectedOutput.put("ab", 0);
        expectedOutput.put("abc", 0);
        expectedOutput.put("de", 3);

        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack,
                                                              needles));
    }

    // test strategy for Map<String, Integer> getSubstrings(String haystack,
    //                                                      String[] needles):
    // haystack: haystack.length() = 0, haystack.length() > 0
    // needles: needles.length = 0, needles.length > 0
    // needles elements's length: 1, >1
    // times needles[i] appears in haystack: 0, 1, n
    // locations needles[i] appears in haystack: first, middle, end
    
    // test haystack.length() = 0
    @Test
    public void testEmptyHaystack() {
        String haystack = "";
        String[] needles = {"ab", "bc"};
        
        Map<String, Integer> expectedOutput = new HashMap<>();
        
        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack, needles));
    }
    
    // test needles.length = 0
    @Test
    public void testEmptyNeedles() {
        String haystack = "abcdefg";
        String[] needles = {};
        
        Map<String, Integer> expectedOutput = new HashMap<>();
        
        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack, needles));
    }
    
    // test times needles[i] appears in haystack: 0, 1, n
    @Test
    public void testTimesAppearsInHaystack() {
        // "ab" 3 times, "bcd" 1 times, "ij" 0 times
        String haystack = "abcdeabfghiabj";
        String[] needles = {"ab", "bcd", "ij"};
        
        Map<String, Integer> expectedOutput = new HashMap<>();
        expectedOutput.put("ab", 0);
        expectedOutput.put("bcd", 1);
        
        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack, needles));
    }
    
    // test needles elements's length appears in needles: 1, >1
    @Test
    public void testLengthOfNeedlesElementAppearINNeedles() {
        // length "": 0, "a": 1, "ab": 2, "def": 3
        String haystack = "abcdefg";
        String[] needles = {"", "a", "bc", "def"};
        
        Map<String, Integer> expectedOutput = new HashMap<>();
        expectedOutput.put("a", 0);
        expectedOutput.put("bc", 1);
        expectedOutput.put("def", 3);
    }
    
    // test locations needles[i] appears in haystack: first, middle, end
    @Test
    public void testLocationsElementAppearsInHaystack() {
        // "abc":first, "de":middle, "g":end
        String haystack = "abcdefg";
        String[] needles = {"abc", "de", "g"};
        
        Map<String, Integer> expectedOutput = new HashMap<>();
        expectedOutput.put("abc", 0);
        expectedOutput.put("de", 3);
        expectedOutput.put("g", 6);
        
        assertEquals(expectedOutput, WordFinder.getSubstrings(haystack, needles));
    }
}

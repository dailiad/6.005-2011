package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class DigitsToStringConverterTest {
    @Test
    public void basicNumberSerializerTest() {
        // Input is a 4 digit number, 0.123 represented in base 4
        int[] input = {0, 1, 2, 3};

        // Want to map 0 -> "d", 1 -> "c", 2 -> "b", 3 -> "a"
        char[] alphabet = {'d', 'c', 'b', 'a'};

        String expectedOutput = "dcba";
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 4, alphabet));
    }

    // test strategy for 
    //   String convertDigitsToString(int[] digits, int base, char[] alphabet)
    // digits: empty, not empty
    // base: <2, >=2
    // digits[i]:
    //   digits[i] >= base
    //   digits[i] < base
    //   digits[i] < 0
    // base vs. alphabet:
    //   base == alphabet.length
    //   base != alphabet.length
    
    // test base
    @Test
    public void testInvalidBase() {
        // base=1
        int[] input = {0};
        char[] alphabet = {'a'};
        
        assertEquals(null, 
                     DigitsToStringConverter.convertDigitsToString(input, 1, alphabet));
        
        
    }
    
    // test empty input
    @Test
    public void testEmptyInput() {
        int[] input = {};
        char[] alphabet = {'a', 'b', 'c'};
        int base = 3;
        String expectedOutput = "";
        
        assertEquals(expectedOutput, 
                     DigitsToStringConverter.convertDigitsToString(input, base, alphabet));
        
    }
    
    // test input[i] >= base
    @Test
    public void testInputIGreaterThanBase() {
        int[] input = {1, 3, 5};
        char[] alphabet = {'a', 'b', 'c'};
        int base = 3;
        String expectedOutput = null;
        assertEquals(expectedOutput, 
                DigitsToStringConverter.convertDigitsToString(input, base, alphabet));
    }
    
    // test input[i] < 0
    @Test
    public void testInputINegtive() {
        int[] input = {1, 2, -2};
        char[] alphabet = {'d', 'c', 'a'};
        int base = 3;
        String expectedOutput = null;
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(input, base, alphabet));
    }
    
    // test base != alphabet.length
    @Test
    public void testBaseNotEqualToAlphabetLength() {
        int[] input = {1, 0, 1, 1};
        char[] alphabet = {'a', 'b', 'c'};
        int base = 2;
        String expectedOutput = null;
        assertEquals(expectedOutput, 
                DigitsToStringConverter.convertDigitsToString(input, base, alphabet));
    
    }
}

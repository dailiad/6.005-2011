package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class AlphabetGeneratorTest {
    @Test
    public void generateFrequencyAlphabetTest() {
        // Expect in the training data that Pr(a) = 2/5, Pr(b) = 2/5,
        // Pr(c) = 1/5.
        String[] trainingData = {"aa", "bbc"};
        // In the output for base 10, they should be in the same proportion.
        char[] expectedOutput = {'a', 'a', 'a', 'a',
                                 'b', 'b', 'b', 'b',
                                 'c', 'c'};
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(
                        10, trainingData));
    }


    // test strategy for char[] generateFrequencyAlphabet(int base, String[] trainingData):
    // base: neg, 0, pos
    // trainingData.vals.length: 0, 1, 2+
    // characters outside the range a-z:0, 1, 2+
    // characters inside the range a-z:1, 2+   when no character is inside a-z, the behaver of this method
    // is not defined! so there is no test for this.
    
    // test negtive base
    @Test
    public void testNegtiveBase() {
        String[] trainingData = {"aa", "bbc", "def"};
        int base = -1;
        
        char[] expectedOutput = null;
        
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(base, trainingData));
    }
    
    @Test
    public void testZeroBase() {
        String[] trainingData = {"aa", "bbc", "def"};
        int base = 0;
        
        char[] expectedOutput = {};
        
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(base, trainingData));
    }
    
    // test trainingDate.vals.length
    @Test
    public void testTrainingDataValueLength() {
        String[] trainingData = {"", "a", "abbc"};
        int base = 10;
        
        char[] expectedOutput = {'a', 'a', 'a', 'a',
                                 'b', 'b', 'b', 'b',
                                 'c', 'c'};
        
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(base, trainingData));
    }
    
    // test characters outside range of a-z
    @Test
    public void testOneCharacterOutsideRangeOfaToz() {
        String[] trainingData = {"", "a", "aAbbc"};
        int base = 10;
        
        char[] expectedOutput = {'a', 'a', 'a', 'a',
                                 'b', 'b', 'b', 'b',
                                 'c', 'c'};
        
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(base, trainingData));
    }
    
    @Test
    public void testTwoPlusCharacterOutsideRangeOfaToz() {
        String[] trainingData = {"FF", "a./?", "aAbbc"};
        int base = 10;
        
        char[] expectedOutput = {'a', 'a', 'a', 'a',
                                 'b', 'b', 'b', 'b',
                                 'c', 'c'};
        
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(base, trainingData));
    }
    
    // test characters inside range of a-z
    @Test
    public void testOneCharacterInsideRangeOfaToz() {
        String[] trainingData = {"SD", ".?MM", "AnNSD"};
        int base = 4;
        
        char[] expectedOutput = {'n', 'n', 'n', 'n'};
        
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(base, trainingData));
    }
}

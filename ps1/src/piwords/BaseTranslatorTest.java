package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseTranslatorTest {
    @Test
    public void basicBaseTranslatorTest() {
        // Expect that .01 in base-2 is .25 in base-10
        // (0 * 1/2^1 + 1 * 1/2^2 = .25)
        int[] input = {0, 1};
        int[] expectedOutput = {2, 5};
        assertArrayEquals(expectedOutput,
                          BaseTranslator.convertBase(input, 2, 10, 2));
    }

    // test strategy for int[] output = convertBase(input, baseA, baseB, precisionB)
    // input: empty, not empty;
    // input[i]: neg, pos
    // baseA: < 2, >= 2
    // baseB: < 2, >= 2
    // precision: 0, 1, 2+
    // relationship between precisonB and input.length:
    //   precisionB > input.length
    //   precisionB == input.length
    //   precisionB < input.length
    // relationship between baseA and baseB:
    //   baseA > baseB
    //   baseA == baseB
    //   baseA < baseB
    // relationship between input[i] and baseA:
    //   input[i] < baseA
    //   input[i] >= baseA
    
    @Test
    public void testEmptyInput() {
        int[] input = {};
        
        assertArrayEquals(null,
                          BaseTranslator.convertBase(input, 2, 8, 1));
    }
    
    @Test
    public void testNegInputI() {
        int[] input = {1, -3};
        assertEquals(null, 
                     BaseTranslator.convertBase(input, 4, 2, 2));
    }
    
    @Test
    public void testBaseLessThanTwo() {
        int[] input = {8, 3, 2, 1};
        // baseA < 2
        assertEquals(null,
                     BaseTranslator.convertBase(input, 1, 3, 3));
        // baseB < 2
        assertEquals(null, 
                     BaseTranslator.convertBase(input, 10, 1, 3));
    }
    
    @Test
    public void testRelationshipBetweenAandB() {
        
        // baseA < baseB
        // baseA = 2
        // baseB = 10
        int[] input1 = {1, 0};
        int[] expectedOutput1 = {5, 0};
        assertArrayEquals(expectedOutput1, 
                          BaseTranslator.convertBase(input1, 2, 10, 2));
        
        // baseA = baseB precisionB > input.length
        int[] input2 = {3, 4, 5};
        int[] expectedOutput2 = {3, 4, 5, 0};
        assertArrayEquals(expectedOutput2,
                          BaseTranslator.convertBase(input2, 6, 6, 4));
        
        // baseA > baseB
        // baseA = 16
        // baseB = 10
        int[] input3 = {2, 4};
        int[] expectedOutput3 = {1, 4, 0, 6, 2, 5};
        assertArrayEquals(expectedOutput3, 
                     BaseTranslator.convertBase(input3, 16, 10, 6));
    }
    
    @Test
    public void testRelationshipBetweenInputAndBaseA() {
        // input[i] > BaseA
        int[] input1 = {1, 3};
        int[] expectedOutput1 = null;
        assertArrayEquals(expectedOutput1, 
                     BaseTranslator.convertBase(input1, 2, 3, 2));
        
        // input[i] = BaseA
        int[] input2 = {1, 4};
        assertEquals(null,
                     BaseTranslator.convertBase(input2, 4, 5, 2));
    }
    
    @Test
    public void testPrecision() {
        // precisionB < 1
        int[] input = {1, 3};
        assertEquals(null,
                     BaseTranslator.convertBase(input, 4, 6, 0));
        // precisionB = 1 && precisionB < input.length
        int[] input2 = { 1, 0};
        int[] expectedOutput2 = {5};
        assertArrayEquals(expectedOutput2, 
                          BaseTranslator.convertBase(input2, 2, 10, 1));
        
    }
}

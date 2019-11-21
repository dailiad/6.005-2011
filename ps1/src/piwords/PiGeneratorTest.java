package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class PiGeneratorTest {
    @Test
    public void basicPowerModTest() {
        // 5^7 mod 23 = 17
        assertEquals(17, PiGenerator.powerMod(5, 7, 23));
    }

    // TODO: Write more tests (Problem 1.a, 1.c)
    // test strategy for i = powerMod(a, b, m)
    // a: neg, 0, 1, pos
    // b: neg, 0, 1, pos
    // m: neg, 0, 1, pos
    // i: -1, 0, 1, pos
    
    // a ^ b < m
    // a ^ b == m
    // a ^ b < m
    
    @Test
    public void testNegtiveArguments() {
        // a is neg
        assertEquals(-1, PiGenerator.powerMod(-1, 3, 2));
        // b is neg
        assertEquals(-1, PiGenerator.powerMod(2, -1, 3));
        // m is neg
        assertEquals(-1, PiGenerator.powerMod(3, 2, -1));
    }
    
    @Test
    public void testZero() {
        // all zero
        assertEquals(0, PiGenerator.powerMod(0, 0, 0));
    }
    
    @Test
    public void testOne() {
        // all 1
        assertEquals(0, PiGenerator.powerMod(1, 1, 1));
        
    }
    
    @Test
    public void testLessThan() {
        assertEquals(8, PiGenerator.powerMod(2, 3, 10));
    }
    
    @Test
    public void testEqual() {
        assertEquals(0, PiGenerator.powerMod(3, 2, 9));
    }
    
    // test strategy for int[] i = computePiInHex(precision):
    // precision: neg, 0, 1, 2+
    // i.length: 0, 1, 2+
    @Test
    public void testPrecisionNeg() {
        assertEquals(null, PiGenerator.computePiInHex(-2));
    }
    
    @Test
    public void testPrecisionZero() {
        assertEquals(0, PiGenerator.computePiInHex(0).length);
    }
    
    @Test
    public void testPrecisionOne() {
        assertEquals(2, PiGenerator.computePiInHex(1)[0]);
        assertEquals(1, PiGenerator.computePiInHex(1).length);
    }
    
    @Test
    public void testPrecision4() {
        assertEquals(4, PiGenerator.computePiInHex(4).length);
        assertEquals(2, PiGenerator.computePiInHex(4)[0]);
        assertEquals(4, PiGenerator.computePiInHex(4)[1]);
        assertEquals(3, PiGenerator.computePiInHex(4)[2]);
        assertEquals(15, PiGenerator.computePiInHex(4)[3]);
    }
    
}

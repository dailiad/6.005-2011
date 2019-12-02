package calculator;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import calculator.Lexer.TokenMismatchException;
import calculator.Parser.ParserException;

public class ParserTest {
    // test strategy:
    // input expression:
    //     普通算术计算：
    //          整数计算，小数计算
    //          四种运算符
    //          scalar,inch,point组合计算
    //     单位转换计算:
    //          inch -> point, point -> inch, scalar -> point/inch
    //     括号的正确使用：单层，嵌套
    
    private Lexer lexer;
    private Parser parser;
    
    public void setUp(String input) {
        lexer = new Lexer(input);
        parser = new Parser(lexer);
    }
    
    @Test
    public void testComputeOnlyScalar() throws ParserException, TokenMismatchException{
        String plusExp = "1 + 2";
        setUp(plusExp);
        assertEquals("3.0", parser.evaluate().toString());
        
        String minusExp = "23.89 - 32";
        setUp(minusExp);
        assertEquals("-8.11", parser.evaluate().toString());
        
        String multiplyExp = "2 * 2";
        setUp(multiplyExp);
        assertEquals("4.0", parser.evaluate().toString());
        
        String divideExp = "3 / 2";
        setUp(divideExp);
        assertEquals("1.5", parser.evaluate().toString());
    }
    
    @Test
    public void testComputeWithUnit() throws ParserException, TokenMismatchException{
        String scalarWithPt = "2 * 3.3pt";
        setUp(scalarWithPt);
        assertEquals("6.6 pt", parser.evaluate().toString());
        
        String scalarPlusInch = "3.3in + 1";
        double expected = (3.3 * 72 + 72) / 72;
        setUp(scalarPlusInch);
        assertEquals(expected + " in", parser.evaluate().toString());
        
        String scalarMultiplyInch = "1.1 * 2in";
        setUp(scalarMultiplyInch);
        assertEquals("2.2 in", parser.evaluate().toString());
        
        // use units of the first operand : pt + in -> pt
        String ptTimesInch = "3pt * 2.4in";
        setUp(ptTimesInch);
        assertEquals("518.4 pt", parser.evaluate().toString());
        
        // as a comparison with last test  : in + pt -> in
        String inchPlusPt = "1in + 72pt";
        setUp(inchPlusPt);
        assertEquals("2.0 in", parser.evaluate().toString());
        
        String inchDivideInch = "4in / 2in";
        setUp(inchDivideInch);
        assertEquals("2.0", parser.evaluate().toString());
        
        String inchMultiplyInch = "2.4in * 2in";
        setUp(inchMultiplyInch);
        assertEquals((2.4 * 72 * 2 * 72) / 72 + " in", parser.evaluate().toString());
        
        String inchDividePt = "1in / 36pt";
        setUp(inchDividePt);
        assertEquals("2.0", parser.evaluate().toString());
        
    }
    
    @Test
    public void testWithParenthese() throws ParserException, TokenMismatchException {
        String singleParenthese = "(1 * 2) + 2.2";
        setUp(singleParenthese);
        assertEquals("4.2", parser.evaluate().toString());
        
        String twoParenthesis = "(1in * 2in) / (2 - 1.8)";
        setUp(twoParenthesis);
        assertEquals(((72 * 2 * 72) / ((2 - 1.8) * 72)) / 72 + " in", parser.evaluate().toString());
        
        String nestedParenthesis = "(((2 - 1.5)) * 2) / 4pt";
        setUp(nestedParenthesis);
        assertEquals("0.25 pt", parser.evaluate().toString());
    }
    
    @Test
    public void testUnitConversion() throws ParserException, TokenMismatchException {
        String scalarToInch = "(2 + 2.3) in";
        setUp(scalarToInch);
        assertEquals("4.3 in", parser.evaluate().toString());
        
        String ptToInch = "(2 pt + 70) in";
        setUp(ptToInch);
        assertEquals("1.0 in", parser.evaluate().toString());
        
        String inchToPt = "(1in + 1in) pt";
        setUp(inchToPt);
        assertEquals("144.0 pt", parser.evaluate().toString());
    }
    
}

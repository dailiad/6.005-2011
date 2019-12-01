package calculator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import calculator.Lexer.Token;
import calculator.Lexer.TokenMismatchException;

public class LexerTest {
    // test strategy for next():
    //从以下几个角度来选择要测试的字符串：
    //    长度：empty, not empty
    //    各种token类型的个数：0, 1, 2+
    //      其中数字分类：int: 0, 1+
    //                    float: 0.0, 0.23, 12.33
    //    非法字符的个数：0, 1, 2+
    //    非法字符出现的位置：start, middle, end
    // test strategy for peekNext():
    //    1.测试peekNext()能否返回正确结果
    //    2.测试peekNext()是否会对next()返回结果产生影响：
    //         调用1次peekNext()再调用next()
    //         连续调用多次peekNext()再调用next()
    
    private Token token0 = new Token(Type.EOF, null);
    private Token token1 = new Token(Type.CLOSE_PARENTHESE, ")");
    private Token token2 = new Token(Type.OPEN_PARENTHESE, "(");
    private Token token3 = new Token(Type.NUMBER, "12");
    private Token token4 = new Token(Type.NUMBER, "0.23");
    private Token token5 = new Token(Type.NUMBER, "12.33");
    private Token token6 = new Token(Type.NUMBER, "0");
    private Token token7 = new Token(Type.NUMBER, "0.0");
    private Token token8 = new Token(Type.UNIT, "pt");
    private Token token9 = new Token(Type.UNIT, "in");
    private Token token10 = new Token(Type.OPERATOR, "+");
    private Token token11 = new Token(Type.OPERATOR, "-");
    private Token token12 = new Token(Type.OPERATOR, "*");
    private Token token13 = new Token(Type.OPERATOR, "/");
    
    @Test
    public void testEmpty() throws TokenMismatchException{
        String input = "";
        Lexer  lexer = new Lexer(input);
        
        assertEquals(token0, lexer.peekNext());
        assertEquals(token0, lexer.next());
    }
    
    @Test
    public void testOneForEach() throws TokenMismatchException{
        String input = "()+* 12 0.23pt";
        Lexer lexer = new Lexer(input);
        
        assertEquals(token2, lexer.peekNext());
        assertEquals(token2, lexer.next());
        
        assertEquals(token1, lexer.next());
        assertEquals(token10, lexer.next());
        assertEquals(token12, lexer.next());
        assertEquals(token3, lexer.next());
        
        assertEquals(token4, lexer.peekNext());
        assertEquals(token4, lexer.next());
        
        assertEquals(token8, lexer.next());
        assertEquals(token0, lexer.next());
    }
    
    @Test
    public void testTwoForEach()  throws TokenMismatchException{
        String input = "()+* 12 0.23pt) ( - /12.33 0 0.0in012";
        Lexer lexer = new Lexer(input);
        
        assertEquals(token2, lexer.peekNext());
        assertEquals(token2, lexer.next());
        
        assertEquals(token1, lexer.next());
        assertEquals(token10, lexer.next());
        assertEquals(token12, lexer.next());
        assertEquals(token3, lexer.next());
        assertEquals(token4, lexer.next());
        
        assertEquals(token8, lexer.peekNext());
        assertEquals(token8, lexer.next());
        assertEquals(token1, lexer.next());
        assertEquals(token2, lexer.next());
        assertEquals(token11, lexer.next());
        assertEquals(token13, lexer.next());
        assertEquals(token5, lexer.next());
        assertEquals(token6, lexer.next());
        assertEquals(token7, lexer.next());
        
        assertEquals(token9, lexer.peekNext());
        assertEquals(token9, lexer.next());
        
      //throw exception
        boolean thrown0 = false;
        try {
            lexer.peekNext();
        } catch (TokenMismatchException e) {
            thrown0 = true;
        }
        
        assertEquals(true, thrown0);
        
        // throw TokenMismatchException
        // error occures at end
        // error = wrong number format
        boolean thrown = false;
        try {
            lexer.next();
        } catch (TokenMismatchException e) {
            thrown = true;
        }
        
        assertEquals(true, thrown);
    }
    
    @Test
    public void testWrongTokenFoundAtBegin()  throws TokenMismatchException{
        // "ds" is wrong token, occures at begin
        String input = "ds()-";
        Lexer lexer = new Lexer(input);
        //throw exception
        boolean thrown0 = false;
        try {
            lexer.peekNext();
        } catch (TokenMismatchException e) {
            thrown0 = true;
        }
        
        assertEquals(true, thrown0);
        
        //throw exception
        boolean thrown = false;
        try {
            lexer.next();
        } catch (TokenMismatchException e) {
            thrown = true;
        }
        
        assertEquals(true, thrown);
        
    }
    
    @Test
    public void testWrongTokenFoundAtMiddle()  throws TokenMismatchException{
        // "01" is wrong number , "3." is wrong number,two errors, occures at middle 
        String input = "0.23/0 01 3. 23 )";
        
        Lexer lexer = new Lexer(input);
        
        assertEquals(token4, lexer.peekNext());
        assertEquals(token4, lexer.next());
        
        assertEquals(token13, lexer.next());
        
        assertEquals(token6, lexer.peekNext());
        assertEquals(token6, lexer.next());
        
      //throw exception
        boolean thrown0 = false;
        try {
            lexer.peekNext();
        } catch (TokenMismatchException e) {
            thrown0 = true;
        }
        
        assertEquals(true, thrown0);
        
        // throw exception
        boolean thrown = false;
        try {
            lexer.next();   
        } catch (TokenMismatchException e) {
            thrown = true;
        }
        
        assertEquals(true, thrown);
    }
}

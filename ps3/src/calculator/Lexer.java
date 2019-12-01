package calculator;

import calculator.Type;
import java.util.regex.Pattern;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.regex.Matcher;

/**
 * Calculator lexical analyzer.
 */
public class Lexer {
    private final String expression;
    private int i;
    private Queue<Token> tokens = new LinkedList<>();
    private static final Pattern TOKEN_REGEX = 
            Pattern.compile(
                    "^(\\()"    // OPEN_PARENTHESE
                    +"|"
                    +"^(\\))"    // CLOSE_PARENTHESE
                    +"|"
                    +"^(0\\.[0-9]+|[1-9]+[0-9]*\\.[0-9]+|[1-9]+[0-9]*|\\b0\\b)"   // NUMBER
                    +"|"
                    +"^(in|pt)"                    //UNIT
                    +"|"
                    +"^([\\+\\-\\*/])"          //OPERATOR
                    +"|"
                    +"^( )"                 // space
                    );
    
    // for constructing token object with index found in matcher.groupCount()
    private static final Type[] TOKEN_TYPE = {
        Type.OPEN_PARENTHESE,
        Type.CLOSE_PARENTHESE,
        Type.NUMBER,
        Type.UNIT,
        Type.OPERATOR
    };
    
    private final Matcher matcher;

	/**
	 * Token in the stream.
	 */
	public static class Token {
		final Type type;
		final String text;

		Token(Type type, String text) {
			this.type = type;
			this.text = text;
		}

		Token(Type type) {
			this(type, null);
		}
		
		@Override
		public boolean equals(Object other) {
		    if (!(other instanceof Token)) {
		        return false;
		    }
		    Token otherToken = (Token) other;
		    return (this.type == otherToken.type) && (Objects.equals(this.text, otherToken.text));
		}
		
		@Override
		public int hashCode() {
            return 0;
		}
	}

	@SuppressWarnings("serial")
	static class TokenMismatchException extends Exception {
	}

	/**
	 * initial matcher with the input
	 */
	public Lexer(String input) {
		i = 0;
		expression = input;
		matcher = TOKEN_REGEX.matcher(input);
	}
	
	/**
	 * return next token from the input string, if the expression reach the end, return a token whose type is EOF.
	 * @throws TokenMismatchException if next character sequence is not a valid token
	 */
	public Token next() throws TokenMismatchException {
	    if (!tokens.isEmpty()) {
	        return tokens.remove();
	    }
        return nextTokenFromInput();
	}
	
	/**
	 * peek the next token in the input, this will not affect the value return by next().
	 * @return Token: the next token in the input
	 * @throws TokenMismatchException : if next character sequence is not a valid token.
	 */
	public Token peekNext() throws TokenMismatchException {
	    if (!tokens.isEmpty()) {
	        return tokens.element();
	    }
	    Token t = nextTokenFromInput();
	    tokens.add(t);
	    return t;
	}
	
	/**
	 * a help method for next() and peekNext(), get the next token from the original input string
	 * @return Token : the next token from the original input string
	 * @throws TokenMismatchException
	 */
	private Token nextTokenFromInput() throws TokenMismatchException {
	    if (i >= expression.length()) {
            return new Token(Type.EOF, null);
        }
        
        String token;
        // ignore white spaces
        while (matcher.find()) {
            token = matcher.group();
            this.i = matcher.end();
            // if next token is white space, jump it and search again
            if (!(token.equals(" "))) {
                for (int i = 1; i < matcher.groupCount(); i++) {
                    if (matcher.group(i) != null) {
                        Token t = new  Token(TOKEN_TYPE[i-1], matcher.group(i));
                        matcher.region(this.i, expression.length());
                        return t;
                    }
                }
            }
            matcher.region(this.i, expression.length());
        }
        
        throw new TokenMismatchException();
	}
}

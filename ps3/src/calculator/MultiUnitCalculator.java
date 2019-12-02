package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import calculator.Lexer.TokenMismatchException;
import calculator.Parser.ParserException;
import calculator.Parser.Value;

/**
 * Multi-unit calculator.
 */
public class MultiUnitCalculator {

	/**
	 * @param expression
	 *            a String representing a multi-unit expression, as defined in
	 *            the problem set
	 * @return the value of the expression, as a number possibly followed by
	 *         units, e.g. "72pt", "3", or "4.882in"
	 */
	public String evaluate(String expression) throws ParserException, TokenMismatchException {
	    Lexer lexer = new Lexer(expression);
	    Parser parser = new Parser(lexer);
	    Value value = parser.evaluate();
        return value.toString();
	}

	/**
	 * Repeatedly reads expressions from the console, and outputs the results of
	 * evaluating them. Inputting an empty line will terminate the program.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) throws IOException {
		MultiUnitCalculator calculator;
		String result;

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String expression;
		while (true) {
			// display prompt
			System.out.print("> ");
			// read input
			expression = in.readLine();
			// terminate if input empty
			if (expression.equals(""))
				break;

			// evaluate
			calculator = new MultiUnitCalculator();
			try {
                result = calculator.evaluate(expression);
                // display result
                System.out.println(result);
                
            } catch (ParserException e) {
                e.printStackTrace();
            } catch (TokenMismatchException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}
	}
}

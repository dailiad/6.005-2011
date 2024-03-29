package calculator;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import calculator.Lexer.TokenMismatchException;
import calculator.Parser.ParserException;

public class CalculatorTest {

    @Test
    public void testCalculator() throws ParserException, TokenMismatchException {
        MultiUnitCalculator mc = new MultiUnitCalculator();
        String expression = "(((2.1-1)*2in)/2pt)+(3-2)in";
        assertTrue(approxEquals(mc.evaluate(expression), "80.2 in", true));
    }
    
	boolean approxEquals(String expr1, String expr2, boolean compareUnits) {
		return new Value(expr1).approxEquals(new Value(expr2), compareUnits);
	}

	static class Value {
		static float delta = 0.001f;
 
		enum Unit {
			POINT, INCH, SCALAR
		}

		Unit unit;
		// in points if a length
		float value;

		Value(String value) {
			value = value.trim();
			if (value.endsWith("pt")) {
				unit = Unit.POINT;
				this.value = Float.parseFloat(value.substring(0,
						value.length() - 2).trim());
			} else if (value.endsWith("in")) {
				unit = Unit.INCH;
				this.value = 72 * Float.parseFloat(value.substring(0,
						value.length() - 2).trim());
			} else {
				unit = Unit.SCALAR;
				this.value = Float.parseFloat(value);
			}
		}

		boolean approxEquals(Value that, boolean compareUnits) {
			return (this.unit == that.unit || !compareUnits)
					&& Math.abs(this.value - that.value) < delta;
		}
	}

}

package calculator;

import calculator.Lexer;
import calculator.Lexer.Token;
import calculator.Lexer.TokenMismatchException;
import calculator.Parser.NullValueException;

/*
 * grammar:
 *     expression ::= arithmetic | conversion
 *     arithmetic ::= operand (operator operand)?
 *     conversion ::= '(' expression ')' unit
 *     operand ::= '(' expression ')' | number unit?
 *     operator = '+' | '-' | '*' | '/'
 *     unit = 'in' | 'pt'
 *     number = 0\\.[0-9]+              //0.32
 *            | [1-9]+[0-9]*\\.[0-9]+   //10.23
 *            | [1-9]+[0-9]*            //123
 *            | \\b0\\b                 //0
 *            
 *     expression ::= operand (operator operand)?
 *     operand ::= (number | parentheseExp) unit?
 *     operator ::= '+'|'-'|'*'|'/'
 *     number ::= int|float
 *     parentheseExp ::= '(' expression ')'
 *     unit ::= 'in'|'pt'
 *     
 */

/**
 * Calculator parser. All values are measured in pt.
 */
class Parser {
	
	public class NullValueException extends RuntimeException {

    }

    @SuppressWarnings("serial")
	static class ParserException extends RuntimeException {

        public ParserException(String string) {
            
        }
	}

	/**
	 * Type of values.
	 */
	private enum ValueType {
		POINTS, INCHES, SCALAR
	};

	/**
	 * Internal value is always in points.
	 */
	public class Value {
		final double value;
		final ValueType type;

		Value(double value, ValueType type) {
			this.value = value;
			this.type = type;
		}

		@Override
		public String toString() {
			switch (type) {
			case INCHES:
				return value / PT_PER_IN + " in";
			case POINTS:
				return value + " pt";
			default:
				return "" + value;
			}
		}
		
		public Value add(Value other) {
		    double value = 0, leftValue = this.value, rightValue = other.value;
            ValueType type = null, leftType = this.type, rightType = other.type;
            
            if (leftType == ValueType.SCALAR) {
                type = rightType;
            } else {
                type = leftType;
            }
            if (leftType == ValueType.INCHES && rightType == ValueType.SCALAR) {
                value = leftValue + rightValue * PT_PER_IN;
            } else if (leftType == ValueType.SCALAR && rightType == ValueType.INCHES) {
                value = leftValue * PT_PER_IN + rightValue;
            } else {
                value = leftValue + rightValue;
            }
            return new Value(value, type);
		    
		}
		
		public Value minus(Value other) {
		    Value rightOperand = new Value(-other.value, other.type);
		    
		    return add(rightOperand);
		    
		}
		
        public Value divide(Value other) throws ParserException {
            double value = 0, leftValue = this.value, rightValue = other.value;
            ValueType type = null, leftType = this.type, rightType = other.type;

            if (rightValue == 0) {
                throw new ParserException("Cannot divide by 0");
            }
            if ((leftType != ValueType.SCALAR) && (rightType != ValueType.SCALAR)) {
                type = ValueType.SCALAR;
            } else if (leftType == ValueType.SCALAR) {
                type = rightType;
            } else if (rightType == ValueType.SCALAR) {
                type = leftType;
            } else {
                type = ValueType.SCALAR;
            }
            
            if (leftType == ValueType.INCHES && rightType == ValueType.SCALAR) {
                value = leftValue / (rightValue * PT_PER_IN);
            } else if (leftType == ValueType.SCALAR && rightType == ValueType.INCHES) {
                value = (leftValue * PT_PER_IN) / rightValue;
            } else {
                value = leftValue / rightValue;
            }

            return new Value(value, type);
        }
		
		public Value multiply(Value other) {
		    double value = 0, leftValue = this.value, rightValue = other.value;
            ValueType type = null, leftType = this.type, rightType = other.type;
            
            if (leftType == ValueType.SCALAR) {
                type = rightType;
            } else {
                type = leftType;
            }
            
            if (leftType == ValueType.INCHES && rightType == ValueType.SCALAR) {
                value = leftValue * rightValue * PT_PER_IN;
            } else if (leftType == ValueType.SCALAR && rightType == ValueType.INCHES) {
                value = leftValue * PT_PER_IN * rightValue;
            } else {
                value = leftValue * rightValue;
            }
            return new Value(value, type);
		    
		}

	}

	private static final double PT_PER_IN = 72;
	private final Lexer lexer;
	private int errorPosition = 0;

	/**
	 * init a parser
	 * @param lexer
	 */
	Parser(Lexer lexer) {
        this.lexer = lexer;
	}

	/**
	 * evaluate a expression with grammar specified at the top of the class.
	 * @return Value : the value of this expression
	 * @throws ParserException: if the expression contains syntax error that can't be parsed.
	 * @throws TokenMismatchException: if the expression contains invalid tokens
	 */
	public Value evaluate() throws ParserException, TokenMismatchException{
	    Value result = evaluateExpression();
	    
	    Token nextToken = lexer.next();
	    if (nextToken.type != Type.EOF) {
	        throw new ParserException("Order of operations not made explicit.");
	    }
	    return result;
	}

	/**
	 * a help function for evaluate() to compute the whole expression
	 * @return Value: the value for the expression
	 */
    private Value evaluateExpression() throws ParserException, TokenMismatchException {
        Value leftOperand = evaluateOperand();
        Value result = null;
        
        Token nextToken = lexer.peekNext();
        if (nextToken.type == Type.OPERATOR ) {
            lexer.next();
            Value rightOperand = evaluateOperand();
            
            result = compute(leftOperand, nextToken, rightOperand);
        } else {
            result = leftOperand;
        }
        
        return result;
    }

    /**
     * 
     * @param leftOperand
     * @param nextToken
     * @param rightOperand
     * @return
     */
    private Value compute(Value leftOperand, Token operator, Value rightOperand) {
        Value value = null;
        switch (operator.text) {
            case  "+" : 
                value = leftOperand.add(rightOperand);
                break;
            case "-" :
                value = leftOperand.minus(rightOperand);
                break;
            case "*" :
                value = leftOperand.multiply(rightOperand);
                break;
            case "/" :
                value = leftOperand.divide(rightOperand);
                break;
            default:
                throw new ParserException("shouln't go there");
                   
        }
        
        return value;
    }


    /**
     * compute the operand of the expression
     * @return Value: the value of the operand of the expression
     */
    private Value evaluateOperand() throws ParserException, TokenMismatchException {
        /*
         * operand ::= (number | parentheseExp) unit?
         */
        
        // check what token this operand begin with
        Token nextToken = lexer.peekNext();
        Value operandValue = null;
        
        if (nextToken.type == Type.NUMBER) {
            operandValue = evaluateNumber();
        } else if (nextToken.type == Type.OPEN_PARENTHESE) {
            // omit "("
            lexer.next();
            Value expValue = evaluateExpression();
            // omit ")"
            nextToken = lexer.next();
            if (nextToken.type == Type.CLOSE_PARENTHESE) {
                operandValue = expValue;
            } else {
                throw new ParserException("need \")\"");
            }
        } else {
            throw new ParserException("need a number or \"(\"");
        }
        
        Value result = operandValue;
        // check if this operand has a unit, if have, convert to this unit
        nextToken = lexer.peekNext();
        if (nextToken.type == Type.UNIT) {
            lexer.next();
            result = convertUnit(operandValue, nextToken.text);
        }
        
        if (result == null) {
            throw new NullValueException();
        }
        return result;
    }
    
    /**
     * convert the value with the unit, if the unit is not a valid unit, do nothing
     * @param value : the value to be converted
     * @param unit : the new unit of the value
     * @return value
     */
    private Value convertUnit(Value operand, String unit) {
        ValueType type = null;
        double value = 0;
        if (unit.equals("in")) {
            type = ValueType.INCHES;
            if (operand.type == ValueType.SCALAR) {
                value = operand.value * PT_PER_IN;
            } else {
                value = operand.value;
            }
        } else if (unit.equals("pt")) {
            type = ValueType.POINTS;
            value = operand.value;
        } else {
            return operand;
        }
        return new Value(value, type);
    }

    /**
     * a help method for evaluateOperand(), call it only when next token.type == Type.NUMBER
     * @return
     * @throws ParserException
     * @throws TokenMismatchException
     */
    private Value evaluateNumber() throws ParserException, TokenMismatchException {
        Token nextToken = lexer.next();
        Value result = null;

        Double num = Double.parseDouble(nextToken.text);
        // check if there is a unit
        nextToken = lexer.peekNext();
        if (nextToken.type == Type.UNIT) {
            nextToken = lexer.next();
            if (nextToken.text.equals("in")) {
                result = new Value(num * PT_PER_IN, ValueType.INCHES);
            } else {
                result = new Value(num, ValueType.POINTS);
            }
        } else {
            result = new Value(num, ValueType.SCALAR);
        }
        
        return result;

    }
}

package calculator;

/*
 * symbols:'+', '-', '*', '/', 'in', 'pt', '(', ')', '0', '3', '4.5' ...
 * types:
 *     NUMBER: '0', '3', '4.5', ...
 *     UNIT: 'in', 'pt'
 *     OPEN_PARENTHESE: '('
 *     CLOSE_PARENTHESE: ')'
 *     PLUSANDMINUS: '+', '-'
 *     DIVIDEANDMULTIPLY: '/', '*'
 *     
 */

/**
 * Token type.
 */
enum Type {
	NUMBER,     // '0'
	UNIT,       // 'in', 'pt'
	OPEN_PARENTHESE,  // '('
	CLOSE_PARENTHESE, // ')'
	OPERATOR,     // '+', '-' , '*', '/'
	EOF               //end of file
}
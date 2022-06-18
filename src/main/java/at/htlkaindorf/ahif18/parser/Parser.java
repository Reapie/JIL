package at.htlkaindorf.ahif18.parser;

import at.htlkaindorf.ahif18.ast.AST;
import at.htlkaindorf.ahif18.ast.nodes.*;
import at.htlkaindorf.ahif18.lexer.Lexer;
import at.htlkaindorf.ahif18.tokens.Token;
import at.htlkaindorf.ahif18.tokens.TokenCategory;
import at.htlkaindorf.ahif18.tokens.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Recursive Descent Parser implementation
 *
 * @author Martin Juritsch
 * @version 1.3
 * @since 1.0
 */
public class Parser {

    private final LinkedList<Token> tokens = new LinkedList<>();
    /**
     * The current Token
     */
    private Token lookahead;
    private final AST syntaxTree = new AST();

    public Parser(LinkedList<Token> tokens) {
        this.tokens.addAll(tokens);
        lookahead = this.tokens.getFirst();
    }

    /**
     * Advances to the next token
     *
     * @since 1.0
     */
    private void nextToken() {
        tokens.pop();
        // at the end of input we return an epsilon token
        if (tokens.isEmpty())
            lookahead = new Token("EOF", TokenType.TK_EOF, lookahead.getLineNumber());
        else {
            lookahead = tokens.getFirst();
        }
    }

    // returns the Token ahead of the current lookahead
    /**
     * Looks ahead of the current token
     *
     * @return the Token ahead of the current lookahead
     * @since 1.0
     */
    private Token peekToken() {
        if (tokens.size() > 1) {
            return tokens.get(1);
        }
        return new Token("EOF", TokenType.TK_EOF, lookahead.getLineNumber());
    }

    /**
     * Entrypoint for the parsing
     *
     * @since 1.0
     */
    private Expr expression() throws ParserException {
        // expression -> signed_term sum_op
        return boolOp(sum());
    }

    /**
     * Parses boolean expressions
     * Sixth checked option
     *
     * @return BinaryExpr of category OP_COMPAR
     * @since 1.0
     */
    private Expr boolOp(Expr expr) throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.OP_COMPAR) {
            // bool_op -> COMPAR
            Token op = lookahead;
            nextToken();
            return new BinaryExpr(op, expr, expression());
        }
        return expr;
    }

    private Expr sum() throws ParserException {
        return sumOp(factor());
    }

    /**
     * Parses sums
     * Fifth checked option
     *
     * @return BinaryExpr of category OP_PLUSMIN
     * @since 1.0
     */
    private Expr sumOp(Expr expr) throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.OP_PLUSMIN) {
            // sum_op -> PLUSMINUS term sum_op
            Token op = lookahead;
            nextToken();
            return new BinaryExpr(op, expr, sum());
        }
        return expr;
    }

    private Expr factor() throws ParserException {
        // term -> factor term_op
        return factorOp(power());
    }

    /**
     * Parses factors
     * Fourth checked option
     *
     * @return BinaryExpr of category  OP_MULDIV
     * @since 1.0
     */
    private Expr factorOp(Expr expression) throws ParserException {
        // term_op -> MULTDIV factor term_op
        if (lookahead.getType().getCategory() == TokenCategory.OP_MULDIV) {
            Token op = lookahead;
            nextToken();
            Expr prod = new BinaryExpr(op, expression, factor());
            return factorOp(prod);
        }

        // term_op -> EPSILON
        return expression;
    }

    private Expr power() throws ParserException {
        // factor -> argument factor_op
        return powerOp(argument());
    }

    /**
     * Parses powers and exponents
     * Third checked option
     *
     * @return BinaryExpr of type factor_op
     * @since 1.0
     */
    private Expr powerOp(Expr expr) throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.OP_POWER) {
            // factor_op -> RAISED expression
            Token op = lookahead;
            nextToken();
            return new BinaryExpr(op, expr, power());
        }
        return expr;
    }

    /**
     * Parses functions, their parameters and brackets
     * Second checked option
     *
     * @return FunctionExpr
     * @since 1.0
     */
    private Expr argument() throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.STD_FUNC) {
            // argument -> FUNCTION argument(s)
            Token identifier = lookahead;
            ArrayList<Expr> arguments = new ArrayList<>();
            do {
                nextToken();
                // allow function calls without arguments because why not
                if (lookahead.getType() == TokenType.TK_OPEN && peekToken().getType() == TokenType.TK_CLOSE) {
                    break;
                }
                arguments.add(argument());
            } while (lookahead.getType() == TokenType.TK_COMMA);
            nextToken();
            return new FunctionExpr(identifier, arguments);
        } else if (lookahead.getType() == TokenType.TK_OPEN) {
            // argument -> OPEN_BRACKET anything CLOSE_BRACKET
            // argument -> COMMA anything {CLOSE_BRACKET | COMMA}
            nextToken();
            Expr expr = expression();
            if (lookahead.getType() != TokenType.TK_CLOSE && lookahead.getType() != TokenType.TK_COMMA) {
                throw new ParserException("Closing brackets expected and " + lookahead.getLexeme() + " found instead");
            }
            //nextToken();
            return expr;
        }
        // argument -> expression
        return value();
    }

    /**
     * Parses literals and variables
     * First checked option
     *
     * @return LiteralExpression or VariableExpression
     * @since 1.0
     */
    private Expr value() throws ParserException {
        if (lookahead.getType() == TokenType.LT_NUMBER ||
                lookahead.getType() == TokenType.LT_STRING ||
                lookahead.getType() == TokenType.LT_BOOL) {
            // argument -> NUMBER
            Expr expr = new LiteralExpr(lookahead);
            nextToken();
            return expr;
        } else if (lookahead.getType() == TokenType.TK_MINUS) {
            // number is negative
            //Token minus = lookahead;
            nextToken();
            Expr expr;
            if (lookahead.getType() == TokenType.LT_NUMBER)
                expr = new LiteralExpr(lookahead).negate();
            else
                throw new ParserException("Unexpected symbol " + lookahead.getLexeme() + " found");
            nextToken();
            return expr;
        } else if (lookahead.getType() == TokenType.IDENTIFIER) {
            // argument -> VARIABLE OR CUSTOM FUNCTION
            nextToken();
        } else {
            throw new ParserException("Unexpected symbol " + lookahead.getLexeme() + " found");
        }
        return null;
    }

    /**
     * Starts parsing process and builds root node
     *
     * @return AbstractSyntaxTree
     * @since 1.0
     */
    public AST parse() {
        try {
            syntaxTree.setRoot(expression());
            return syntaxTree;
        } catch (ParserException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    /**
     * Method for testing and demo purposes
     *
     * @since 1.1
     */
    public static void main(String[] args) {
        Lexer l = new Lexer("1+2*3**4");
        var tokens = l.lex();
        Parser p = new Parser(tokens);
        p.parse().print();
    }

}

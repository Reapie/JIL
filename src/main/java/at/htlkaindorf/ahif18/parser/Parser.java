package at.htlkaindorf.ahif18.parser;

import at.htlkaindorf.ahif18.ast.AST;
import at.htlkaindorf.ahif18.ast.nodes.*;
import at.htlkaindorf.ahif18.lexer.Lexer;
import at.htlkaindorf.ahif18.tokens.Token;
import at.htlkaindorf.ahif18.tokens.TokenCategory;
import at.htlkaindorf.ahif18.tokens.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;

public class Parser {

    private final LinkedList<Token> tokens = new LinkedList<>();
    private Token lookahead;
    private final AST syntaxTree = new AST();

    public Parser(LinkedList<Token> tokens) {
        this.tokens.addAll(tokens);
        lookahead = this.tokens.getFirst();
    }

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
    private Token peekToken() {
        System.out.println(tokens);
        if (tokens.size() > 1) {
            return tokens.get(1);
        }
        return new Token("EOF", TokenType.TK_EOF, lookahead.getLineNumber());
    }

    private Expr expression() throws ParserException {
        // expression -> signed_term sum_op
        return boolOp(sum());
    }

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
        return sumOp(term());
    }

    private Expr sumOp(Expr expr) throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.OP_PLUSMIN) {
            // sum_op -> PLUSMINUS term sum_op
            Token op = lookahead;
            nextToken();
            return new BinaryExpr(op, expr, sum());
        }
        return expr;
    }

    private Expr term() throws ParserException {
        // term -> factor term_op
        return termOp(power());
    }

    private Expr termOp(Expr expression) throws ParserException {
        // term_op -> MULTDIV factor term_op
        if (lookahead.getType().getCategory() == TokenCategory.OP_MULDIV) {
            Token op = lookahead;
            nextToken();
            Expr prod = new BinaryExpr(op, expression, term());
            return termOp(prod);
        }

        // term_op -> EPSILON
        return expression;
    }

    private Expr power() throws ParserException {
        // factor -> argument factor_op
        return powerOp(argument());
    }

    private Expr powerOp(Expr expr) throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.OP_POWER) {
            // factor_op -> RAISED expression
            Token op = lookahead;
            nextToken();
            return new BinaryExpr(op, expr, power());
        }
        return expr;
    }

    private Expr argument() throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.STD_FUNC) {
            // argument -> FUNCTION argument(s)
            Token identifier = lookahead;
            ArrayList<Expr> arguments = new ArrayList<>();
            while (lookahead.getType() != TokenType.TK_CLOSE) {
                nextToken();
                // allow function calls without arguments because why not
                if (lookahead.getType() == TokenType.TK_OPEN && peekToken().getType() == TokenType.TK_CLOSE)
                    break;
                arguments.add(argument());
            }
            nextToken();
            return new FunctionExpr(identifier, arguments);
        } else if (lookahead.getType() == TokenType.TK_OPEN || lookahead.getType() == TokenType.TK_COMMA) {
            // argument -> OPEN_BRACKET anything CLOSE_BRACKET
            nextToken();
            Expr expr = expression();

            if (lookahead.getType() != TokenType.TK_CLOSE && lookahead.getType() != TokenType.TK_COMMA
                    && lookahead.getType() != TokenType.TK_EOF) {
                throw new ParserException("Closing brackets or Comma expected and " + lookahead.getLexeme() + " found instead");
            }

            //nextToken(); //needs to stay commented out, otherwise the parser will not recognize the closing bracket
            return expr;
        }
        // argument -> value
        return value();
    }

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

    public AST parse() {
        try {
            syntaxTree.add(expression());
            return syntaxTree;
        } catch (ParserException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Lexer l = new Lexer("\"Hello\" + \"World\"");
        var tokens = l.lex();
        Parser p = new Parser(tokens);
        p.parse().print();
    }

}

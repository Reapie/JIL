package at.htlkaindorf.ahif18.parser;

import at.htlkaindorf.ahif18.ast.AST;
import at.htlkaindorf.ahif18.lexer.Lexer;
import at.htlkaindorf.ahif18.tokens.Token;
import at.htlkaindorf.ahif18.tokens.TokenCategory;
import at.htlkaindorf.ahif18.tokens.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    private LinkedList<Token> tokens;
    private Token lookahead;
    private AST output = new AST();

    public Parser(LinkedList<Token> tokens) {
        this.tokens = (LinkedList<Token>) tokens.clone();
        lookahead = this.tokens.getFirst();

    }

    private void nextToken() {
        tokens.pop();
        // at the end of input we return an epsilon token
        if (tokens.isEmpty())
            lookahead = new Token("", TokenType.TK_EOF, lookahead.getLineNumber());
        else
            lookahead = tokens.getFirst();
    }

    private void expression() throws ParserException {
        // expression -> signed_term sum_op
        signedTerm();
        sumOp();
    }

    private void sumOp() throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.OP_PLUSMIN) {
            // sum_op -> PLUSMINUS term sum_op
            nextToken();
            term();
            sumOp();
        } else {
            // sum_op -> EPSILON
        }
    }

    private void signedTerm() throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.OP_PLUSMIN) {
            // signed_term -> PLUSMINUS term
            nextToken();
            term();
        } else {
            // signed_term -> term
            term();
        }
    }

    private void term() throws ParserException {
        // term -> factor term_op
        factor();
        termOp();
    }

    private void termOp() throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.OP_MULDIV) {
            // term_op -> MULTDIV factor term_op
            nextToken();
            signedFactor();
            termOp();
        } else {
            // term_op -> EPSILON
        }
    }

    private void signedFactor() throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.OP_PLUSMIN) {
            // signed_factor -> PLUSMINUS factor
            nextToken();
            factor();
        } else {
            // signed_factor -> factor
            factor();
        }
    }

    private void factor() throws ParserException {
        // factor -> argument factor_op
        argument();
        factorOp();
    }

    private void factorOp() throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.OP_POWER) {
            // factor_op -> RAISED expression
            nextToken();
            signedFactor();
        } else {
            // factor_op -> EPSILON
        }
    }

    private void argument() throws ParserException {
        if (lookahead.getType().getCategory() == TokenCategory.STD_FUNC) {
            // argument -> FUNCTION argument
            nextToken();
            argument();
        } else if (lookahead.getType() == TokenType.TK_OPEN) {
            // argument -> OPEN_BRACKET sum CLOSE_BRACKET
            nextToken();
            expression();

            if (lookahead.getType() != TokenType.TK_CLOSE)
                throw new ParserException("Closing brackets expected and " + lookahead.getLexeme() + " found instead");

            nextToken();
        } else {
            // argument -> value
            value();
        }
    }

    private void value() throws ParserException {
        if (lookahead.getType() == TokenType.LT_NUMBER) {
            // argument -> NUMBER
            nextToken();
        } else if (lookahead.getType() == TokenType.LT_STRING) {
            // argument -> STRING
            nextToken();
        } else if (lookahead.getType() == TokenType.IDENTIFIER) {
            // argument -> VARIABLE
            nextToken();
        } else {
            throw new ParserException("Unexpected symbol " + lookahead.getLexeme() + " found");
        }
    }

    public void parse() {
        try {
            expression();
        } catch (ParserException pe) {
            pe.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Lexer l = new Lexer("3 ** (1 + 1)");
        var tokens = l.lex();
        Parser p = new Parser(tokens);
        p.parse();
    }

}

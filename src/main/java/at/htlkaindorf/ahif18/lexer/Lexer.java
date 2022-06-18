package at.htlkaindorf.ahif18.lexer;

import at.htlkaindorf.ahif18.tokens.Token;
import at.htlkaindorf.ahif18.tokens.TokenType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Responsible for turning an input String into a list of Tokens
 * Could be done way more efficiently but is done via regex-matching instead
 *
 * @author Martin Juritsch
 * @version 1.0
 * @since 1.0
 */
public class Lexer {

    private Token token;
    /**
     * Every character that will be ignored is added here in the constructor
     * these craracters are: \r \t \n ' '
     */
    private final Set<Character> blankChars = new HashSet<Character>();
    private boolean done = false;
    private final int pos = 0;
    private final StringBuilder input; // String Builder gives us more methods than String
    private String originalInput;
    private String errorMessage = "";
    private int currentLine = 1;

    public Lexer(String input) {
        this.input = new StringBuilder(input);
        originalInput = input;

        blankChars.add('\r');
        blankChars.add('\t');
        blankChars.add('\n');
        blankChars.add(' ');

        next();
    }

    public Token currentToken() {
        return token;
    }

    /**
     * Advances to the next token
     *
     * @since 1.0
     */
    public void next() {
        if (input.length() == 0)
            done = true;

        if (done)
            return;

        ignoreWhiteSpaces();

        if (findNextToken())
            return;

        done = true;

        if (input.length() > 0)
            errorMessage = "Unexpected symbol: '" + input.charAt(0) + "'";
    }

    /**
     * Deletes characters in blankChars from the input
     *
     * @since 1.0
     */
    private void ignoreWhiteSpaces() {
        int charsToDelete = 0;

        while (blankChars.contains(input.charAt(charsToDelete))) {
            if (input.charAt(charsToDelete) == '\n')
                ++currentLine;
            ++charsToDelete;
        }

        if (charsToDelete > 0) {
            input.delete(0, charsToDelete);
        }
    }

    private boolean findNextToken() {
        for (TokenType t : TokenType.values()) {
            int end = t.endOfMatch(input.toString());

            if (t != TokenType.TK_EOF && end != -1) {
                token = new Token(input.substring(0, end), t, currentLine);
                input.delete(0, end);
                return true;
            }
        }

        return false;
    }

    /**
     * Starts the lexing process
     *
     * @return List of tokens in correct order
     * @since 1.0
     */
    public LinkedList<Token> lex() {
        LinkedList<Token> tokens = new LinkedList<>();
        while (!done) {
            tokens.add(currentToken());
            next();
        }

        return tokens;
    }

    /**
     * Method for testing and demo purposes
     *
     * @since 1.1
     */
    public static void main(String[] args) {
        String input = "sqrt(15 + 10)";
        Lexer lexer = new Lexer(input);
        System.out.println(input);
        LinkedList<Token> tokens = lexer.lex();
        for (Token t : tokens) {
            System.out.printf("%6s : %s\n", t.getLexeme(), t.getType());
        }
    }

}

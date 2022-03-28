package at.htlkaindorf.ahif18.lexer;

import at.htlkaindorf.ahif18.tokens.Token;
import at.htlkaindorf.ahif18.tokens.TokenType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/*
 * Lexer class,
 * Responsible for turning an input String into a list of Tokens
 */
public class Lexer {

    private Token token;
    private Set<Character> blankChars = new HashSet<Character>();
    private boolean done = false;
    private int pos = 0;
    private StringBuilder input; // String Builder gives us more methods than String
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

    public LinkedList<Token> lex() {
        System.out.printf("Lexing Expression \n%s\n", originalInput);
        LinkedList<Token> tokens = new LinkedList<>();
        while (!done) {
            tokens.add(currentToken());
            next();
        }

        return tokens;
    }

    public static void main(String[] args) {
        Lexer lexer = new Lexer("var burger = 3735928559");
        LinkedList<Token> tokens = lexer.lex();
        for (Token t : tokens) {
            System.out.printf("%16s : %s (%d) %d\n", t.getLexeme(), t.getType(),
                    t.getType().getCategory().getPriority(), t.getLineNumber());
        }
    }

}

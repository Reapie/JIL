package at.htlkaindorf.ahif18.lexer;

import at.htlkaindorf.ahif18.tokens.Token;
import at.htlkaindorf.ahif18.tokens.TokenType;

import java.util.ArrayList;
import java.util.HashSet;
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

    public Lexer(String input) {
        this.input = new StringBuilder(input);
        originalInput = input;

        blankChars.add('\r');
        blankChars.add('\n');
        blankChars.add('\t');
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
            charsToDelete++;
        }

        if (charsToDelete > 0) {
            input.delete(0, charsToDelete);
        }
    }

    private boolean findNextToken() {
        for (TokenType t : TokenType.values()) {
            int end = t.endOfMatch(input.toString());

            if (end != -1) {
                token = new Token(input.substring(0, end), t);
                input.delete(0, end);
                return true;
            }
        }

        return false;
    }

    public ArrayList<Token> lex() {
        System.out.printf("Lexing Expression \n%s\n", originalInput);
        ArrayList<Token> tokens = new ArrayList<>();
        while (!done) {
            tokens.add(currentToken());
            next();
        }

        return tokens;
    }

    public static void main(String[] args) {
        Lexer lexer = new Lexer("var wrtgzu123 =        15");
        ArrayList<Token> tokens = lexer.lex();
        for (Token t : tokens) {
            System.out.printf("%16s : %s (%d)\n", t.getLexeme(), t.getType(),
                    t.getType().getCategory().getPriority());
        }
    }

}

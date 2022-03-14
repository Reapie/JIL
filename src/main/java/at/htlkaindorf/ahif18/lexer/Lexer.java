package at.htlkaindorf.ahif18.lexer;

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
    private String lexeme;
    private String errorMessage = "";

    public Lexer(String input) {
        this.input = new StringBuilder(input);
        originalInput = input;

        // found this on stackoverflow, no clue what these chars are,
        // but i guess they cant be that harmful to include
        blankChars.add('\r');
        blankChars.add('\n');
        blankChars.add((char) 8);
        blankChars.add((char) 9);
        blankChars.add((char) 11);
        blankChars.add((char) 12);
        blankChars.add((char) 32);

        next();
    }

    public Token currentToken() {
        return token;
    }

    public String currentLexeme() {
        return lexeme;
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
        for (Token t : Token.values()) {
            int end = t.endOfMatch(input.toString());

            if (end != -1) {
                token = t;
                lexeme = input.substring(0, end);
                input.delete(0, end);
                return true;
            }
        }

        return false;
    }

    public void lex() {
        System.out.printf("Lexing Expression %s: \n", originalInput);
        while (!done) {
            System.out.printf("%16s : %s\n", currentLexeme(), currentToken());
            next();
        }
    }

    public static void main(String[] args) {
        Lexer lexer = new Lexer("const bool = x < 1 or x > 5");
        lexer.lex();
    }

}

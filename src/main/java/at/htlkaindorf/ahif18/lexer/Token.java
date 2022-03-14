package at.htlkaindorf.ahif18.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This Class describes the Grammar of our language,
 * keep in mind that the fist tokens will be checked fist, so you have to eg: put REAL before INT
 */
public enum Token {

    TK_MINUS ("-"),
    TK_PLUS ("\\+"),
    TK_MUL ("\\*"),
    TK_DIV ("/"),
    TK_NOT ("~"),
    TK_AND ("(&&|and)"),
    TK_OR ("(\\|\\||or)"),
    TK_LESS ("<"),
    TK_LEG ("<="),
    TK_GT (">"),
    TK_GEQ (">="),
    TK_EQ ("=="),
    TK_DIFFERENT ("!="),
    TK_ASSIGN ("="),
    TK_OPEN ("\\("),
    TK_CLOSE ("\\)"),
    TK_SEMI (";"),
    TK_COMMA (","),
    TK_KEY_DEFINE ("define"),
    TK_KEY_IS ("is"),
    TK_KEY_IF ("if"),
    TK_KEY_THEN ("then"),
    TK_KEY_ELSE ("else"),
    TK_KEY_ENDIF ("endif"),
    TK_VAR_DECL ("var"),
    TK_CONST_DECL ("const"),
    TK_OPEN_BRACKET ("\\{"),
    TK_CLOSE_BRACKET ("\\}"),

    LT_REAL ("(\\d*)\\.\\d+"),
    LT_INTEGER ("\\d+"),
    LT_STRING ("\"[^\"]+\""),
    IDENTIFIER ("\\w+");

    private final Pattern pattern;

    private Token(String regex) {
        pattern = Pattern.compile("^" + regex);
    }

    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);
        if (m.find())
            return m.end();

        return -1;
    }
}
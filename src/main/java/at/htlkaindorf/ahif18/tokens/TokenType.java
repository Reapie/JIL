package at.htlkaindorf.ahif18.tokens;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This Class describes the Grammar of our language,
 * keep in mind that the fist tokens will be checked fist, so you have to eg: put ** before *
 */
public enum TokenType {

    TK_MINUS ("-", TokenCategory.OP_PLUSMIN),
    TK_PLUS ("\\+", TokenCategory.OP_PLUSMIN),
    TK_POW ("(\\*\\*|pow)", TokenCategory.OP_POWER),
    TK_MUL ("\\*", TokenCategory.OP_MULDIV),
    TK_DIV ("/", TokenCategory.OP_MULDIV),

    TK_NOT ("(!|not)", TokenCategory.OP_BOOL),
    TK_AND ("(&&|and)", TokenCategory.OP_BOOL),
    TK_OR ("(\\|\\||or)", TokenCategory.OP_BOOL),
    TK_LESS ("<", TokenCategory.OP_COMPAR),
    TK_LEG ("<=", TokenCategory.OP_COMPAR),
    TK_GT (">", TokenCategory.OP_COMPAR),
    TK_GEQ (">=", TokenCategory.OP_COMPAR),
    TK_EQ ("==", TokenCategory.OP_COMPAR),
    TK_DIFFERENT ("!=", TokenCategory.OP_COMPAR),
    TK_ASSIGN ("="),
    TK_OPEN ("\\(", TokenCategory.OP_GROUP),
    TK_CLOSE ("\\)", TokenCategory.OP_GROUP),
    TK_SEMI (";"),
    TK_COMMA (","),
    TK_KEY_DEFINE ("define"),
    TK_KEY_IS ("is"),
    TK_KEY_IF ("if"),
    TK_KEY_THEN ("then"),
    TK_KEY_ELSE ("else"),
    TK_KEY_ENDIF ("endif"),
    TK_KEY_VAR("var"),
    TK_KEY_CONST("const"),
    TK_OPEN_BRACKET ("\\{"),
    TK_CLOSE_BRACKET ("\\}"),

    LT_REAL ("(\\d*)\\.\\d+"),
    LT_INTEGER ("\\d+"),
    LT_STRING ("\"[^\"]+\""),
    IDENTIFIER ("\\w+");

    private final Pattern pattern;
    private final TokenCategory category;

    private TokenType(String regex) {
        pattern = Pattern.compile("^" + regex);
        category = TokenCategory.DEFAULT;
    }

    private TokenType(String regex, TokenCategory category) {
        pattern = Pattern.compile("^" + regex);
        this.category = category;
    }

    public int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);
        if (m.find())
            return m.end();

        return -1;
    }

    public TokenCategory getCategory() {
        return category;
    }

}
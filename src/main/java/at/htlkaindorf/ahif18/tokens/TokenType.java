package at.htlkaindorf.ahif18.tokens;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This Class describes the Grammar of our language
 * Every type of token and its regex is here
 * keep in mind that the fist tokens will be checked fist, so you have to eg: put ** before *
 *
 * @author Martin Juritsch
 * @version 1.3
 * @since 1.0
 */
public enum TokenType {

    TK_PLUS ("\\+", TokenCategory.OP_PLUSMIN),
    TK_MINUS ("-", TokenCategory.OP_PLUSMIN),
    TK_POW ("(\\*\\*|pow)", TokenCategory.OP_POWER),
    TK_MUL ("\\*", TokenCategory.OP_MULDIV),
    TK_DIV ("/", TokenCategory.OP_MULDIV),

    TK_LEG ("<=", TokenCategory.OP_COMPAR),
    TK_LESS ("<", TokenCategory.OP_COMPAR),
    TK_GEQ (">=", TokenCategory.OP_COMPAR),
    TK_GT (">", TokenCategory.OP_COMPAR),
    TK_EQ ("==", TokenCategory.OP_COMPAR),
    TK_NOTEQ ("!=", TokenCategory.OP_COMPAR),
    TK_NOT ("(!|not)", TokenCategory.OP_BOOL),
    TK_AND ("(&&|and)", TokenCategory.OP_BOOL),
    TK_OR ("(\\|\\||or)", TokenCategory.OP_BOOL),
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

    TK_STD_HYPOT("hypot", TokenCategory.STD_FUNC),
    TK_STD_SQRT("sqrt", TokenCategory.STD_FUNC),
    TK_STD_SIN("sin", TokenCategory.STD_FUNC),
    TK_STD_COS("cos", TokenCategory.STD_FUNC),
    TK_STD_TAN("tan", TokenCategory.STD_FUNC),
    TK_STD_FLOOR("floor", TokenCategory.STD_FUNC),
    TK_STD_CEIL("ceil", TokenCategory.STD_FUNC),

    TK_OPEN_BRACKET ("\\{"),
    TK_CLOSE_BRACKET ("\\}"),
    TK_EOF(""),

    LT_BOOL ("true|false"),
    LT_NUMBER ("\\d+(?:\\.\\d+)?"),
    LT_STRING ("((\")([^\"]*)(\"))|((')([^']*)('))"),
    IDENTIFIER ("\\w+");

    private final Pattern pattern;
    private final TokenCategory category;

    TokenType(String regex) {
        pattern = Pattern.compile("^" + regex);
        category = TokenCategory.DEFAULT;
    }

    TokenType(String regex, TokenCategory category) {
        pattern = Pattern.compile("^" + regex);
        this.category = category;
    }

    /**
     * Method for lexer to know when a match ends
     *
     * @return index of when the match ends
     * @since 1.0
     */
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
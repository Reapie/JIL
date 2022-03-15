package at.htlkaindorf.ahif18.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This Class describes the Grammar of our language,
 * keep in mind that the fist tokens will be checked fist, so you have to eg: put REAL before INT
 */
public enum TokenType {

    TK_MINUS ("-", Category.OP_PLUSMIN),
    TK_PLUS ("\\+", Category.OP_PLUSMIN),
    TK_MUL ("\\*", Category.OP_MULDIV),
    TK_DIV ("/", Category.OP_MULDIV),
    TK_NOT ("(!|not)", Category.OP_BOOL),
    TK_AND ("(&&|and)", Category.OP_BOOL),
    TK_OR ("(\\|\\||or)", Category.OP_BOOL),
    TK_LESS ("<", Category.OP_COMPAR),
    TK_LEG ("<=", Category.OP_COMPAR),
    TK_GT (">", Category.OP_COMPAR),
    TK_GEQ (">=", Category.OP_COMPAR),
    TK_EQ ("==", Category.OP_COMPAR),
    TK_DIFFERENT ("!=", Category.OP_COMPAR),
    TK_ASSIGN ("="),
    TK_OPEN ("\\(", Category.OP_GROUP),
    TK_CLOSE ("\\)", Category.OP_GROUP),
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
    private final Category category;

    private TokenType(String regex) {
        pattern = Pattern.compile("^" + regex);
        category = Category.DEFAULT;
    }

    private TokenType(String regex, Category category) {
        pattern = Pattern.compile("^" + regex);
        this.category = category;
    }

    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);
        if (m.find())
            return m.end();

        return -1;
    }

    public Category getCategory() {
        return category;
    }

    public enum Category {
        OP_COMPAR(1),
        OP_BOOL(1),
        OP_MULDIV(3),
        OP_PLUSMIN(2),
        OP_GROUP(5),
        DEFAULT(0);

        private final int priority;

        private Category(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }
}
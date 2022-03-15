package at.htlkaindorf.ahif18.lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This Class describes the Grammar of our language,
 * keep in mind that the fist tokens will be checked fist, so you have to eg: put REAL before INT
 */
public enum Token {

    TK_MINUS ("-", TYPE.OP_PLUSMIN),
    TK_PLUS ("\\+", TYPE.OP_PLUSMIN),
    TK_MUL ("\\*", TYPE.OP_MULDIV),
    TK_DIV ("/", TYPE.OP_MULDIV),
    TK_NOT ("(!|not)", TYPE.OP_BOOL),
    TK_AND ("(&&|and)", TYPE.OP_BOOL),
    TK_OR ("(\\|\\||or)", TYPE.OP_BOOL),
    TK_LESS ("<", TYPE.OP_COMPAR),
    TK_LEG ("<=", TYPE.OP_COMPAR),
    TK_GT (">", TYPE.OP_COMPAR),
    TK_GEQ (">=", TYPE.OP_COMPAR),
    TK_EQ ("==", TYPE.OP_COMPAR),
    TK_DIFFERENT ("!=", TYPE.OP_COMPAR),
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
    TK_KEY_VAR("var"),
    TK_KEY_CONST("const"),
    TK_OPEN_BRACKET ("\\{"),
    TK_CLOSE_BRACKET ("\\}"),

    LT_REAL ("(\\d*)\\.\\d+"),
    LT_INTEGER ("\\d+"),
    LT_STRING ("\"[^\"]+\""),
    IDENTIFIER ("\\w+");

    private final Pattern pattern;
    private final TYPE type;

    private Token(String regex) {
        pattern = Pattern.compile("^" + regex);
        type = TYPE.DEFAULT;
    }

    private Token(String regex, TYPE type) {
        pattern = Pattern.compile("^" + regex);
        this.type = type;
    }

    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);
        if (m.find())
            return m.end();

        return -1;
    }

    public enum TYPE {
        OP_COMPAR(1),
        OP_BOOL(1),
        OP_MULDIV(3),
        OP_PLUSMIN(2),
        DEFAULT(0);

        private final int priority;

        private TYPE(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
    }
}
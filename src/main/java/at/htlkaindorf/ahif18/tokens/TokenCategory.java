package at.htlkaindorf.ahif18.tokens;

/**
 * Groups TokenType into categories
 * Used by parser
 *
 * @author Martin Juritsch
 * @version 1.1
 * @since 1.0
 */
public enum TokenCategory {
        OP_COMPAR(),
        OP_BOOL(),
        OP_MULDIV(),
        OP_PLUSMIN(),
        OP_POWER(),
        OP_GROUP(),
        STD_FUNC(),
        DEFAULT();
}

package at.htlkaindorf.ahif18.tokens;

public enum TokenCategory {
        OP_COMPAR(1),
        OP_BOOL(1),
        OP_MULDIV(3),
        OP_PLUSMIN(2),
        OP_POWER(4),
        OP_GROUP(5),
        STD_FUNC(5),
        DEFAULT(0);

        private final int priority;

        private TokenCategory(int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
}

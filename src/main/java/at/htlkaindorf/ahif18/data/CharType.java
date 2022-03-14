package at.htlkaindorf.ahif18.data;

public enum CharType {
    
    STRING_CONTAINERS("\"'"),
    SEPARATORS(" \t\n\r\f"),
    OPERATORS("+-*/%<>=!&|^~"),
    IDENTIFIERS("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_"),
    NUMBERS("0123456789"),
    SPECIAL_CHARS(".,;:[](){}"),
    DEFAULT("");

    private final String chars;

    private CharType(String chars) {
        this.chars = chars;
    }

    public String getChars() {
        return chars;
    }

    /*
     * Determines what type of character the passed element is
     * @param the character to check
     */
    public static CharType getCharType(char c) {
        for (CharType t : CharType.values())
            if (t.chars.indexOf(c) != -1)
                return t;
        return DEFAULT;
    }

}

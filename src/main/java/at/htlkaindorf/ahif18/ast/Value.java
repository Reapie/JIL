package at.htlkaindorf.ahif18.ast;

import at.htlkaindorf.ahif18.parser.ParserException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Value {

    public enum TYPES {NUMBER, STRING, BOOL, DEFAULT};

    private TYPES type;
    private double numValue = 0;
    private String strValue = "";
    private boolean boolValue = false;

    public Value(Double value) {
        type = TYPES.NUMBER;
        this.numValue = value;
    }

    public Value(String value, boolean sanitize) {
        type = TYPES.STRING;
        // Not very nice, but it works :)
        if (sanitize)
            this.strValue = value.replace(value.substring(0, 1), "");
        else
            this.strValue = value;
    }

    public Value(boolean value) {
        type = TYPES.BOOL;
        this.boolValue = value;
    }

    public Value() {
        type = TYPES.DEFAULT;
    }

    // Convenience for getting the bool because I dont like "isBoolValue"
    public boolean getBoolValue() {
        return boolValue;
    }

    @Override
    public String toString() {
        return switch (type) {
            case NUMBER -> String.valueOf(numValue);
            case STRING -> strValue;
            case BOOL -> String.valueOf(boolValue);
            default -> "NOVAL";
        };
    }


}

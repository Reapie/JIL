package at.htlkaindorf.ahif18.ast;

import at.htlkaindorf.ahif18.parser.ParserException;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Value {
    public enum TYPES {NUMBER, STRING, DEFAULT};

    private TYPES type;
    private double numValue = 0;
    private String strValue = "";

    public Value(Double value) {
        type = TYPES.NUMBER;
        this.numValue = value;
    }

    public Value(String value) {
        type = TYPES.STRING;
        // Not very nice, but it works :)
        this.strValue = value.replace(value.substring(0, 1), "");
    }

    public Value() {
        type = TYPES.DEFAULT;
    }

    public Value add(Value other) throws ParserException {
        if (type == other.getType()) {
            if (type == TYPES.NUMBER) {
                this.numValue += other.getNumValue();
                return this;
            } else if (type == TYPES.STRING) {
                strValue = strValue.concat(other.getStrValue());
                return this;
            }
        }
        return new Value();
    }

    public Value sub(Value other) throws ParserException {
        if (type == other.getType()) {
            if (type == TYPES.NUMBER) {
                this.numValue -= other.getNumValue();
                return this;
            }
        }
        return new Value();
    }

    public Value mul(Value other) throws ParserException {
        if (type == other.getType()) {
            if (type == TYPES.NUMBER) {
                this.numValue *= other.getNumValue();
                return this;
            }
            // not very pretty but it works :)
        } else if (type == TYPES.STRING && other.getType() == TYPES.NUMBER) {
            this.strValue = new String(new char[(int) other.getNumValue()]).replace("\0", strValue);
            this.type = TYPES.STRING;
            return this;
        } else if (type == TYPES.NUMBER && other.getType() == TYPES.STRING) {
            this.strValue = new String(new char[(int) numValue]).replace("\0", other.getStrValue());
            this.type = TYPES.STRING;
            return this;
        }
        return new Value();
    }

    public Value div(Value other) throws ParserException {
        if (type == other.getType()) {
            if (type == TYPES.NUMBER) {
                this.numValue /= other.getNumValue();
                return this;
            }
        }
        return new Value();
    }

    public Value pow(Value other) throws ParserException {
        if (type == other.getType()) {
            if (type == TYPES.NUMBER) {
                this.numValue = Math.pow(this.numValue, other.getNumValue());
                return this;
            }
        }
        return new Value();
    }

    @Override
    public String toString() {
        return switch (type) {
            case NUMBER -> String.valueOf(numValue);
            case STRING -> String.format("\"%s\"", strValue);
            default -> "WTF";
        };
    }


}

package at.htlkaindorf.ahif18.ast;

import at.htlkaindorf.ahif18.eval.ValueMismatchException;
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
        type = TYPES.NUMBER;
        this.strValue = value;
    }

    public Value() {
        type = TYPES.DEFAULT;
    }

    public Value add(Value other) throws ParserException {
        if (type == other.getType()) {
            if (type == TYPES.NUMBER) {
                this.numValue += other.getNumValue();
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
        if (type == TYPES.NUMBER) {
            return String.valueOf(numValue);
        }
        return strValue;
    }


}

package at.htlkaindorf.ahif18.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Value<T> {
    public enum types {NUMBER, STRING};

    private types type;
    private T value;

    public Value(T value) {
        if (value instanceof String) {
            type = types.STRING;
        } else if (value instanceof Double) {
            type = types.NUMBER;
        }
        this.value = value;
    }



}

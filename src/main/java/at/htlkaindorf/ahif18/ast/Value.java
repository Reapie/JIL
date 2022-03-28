package at.htlkaindorf.ahif18.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Value<T> {
    public enum types {NUMBER, STRING};

    private types type;
    private T value;

    public Value(T t) {
        if (t instanceof String) {
            type = types.STRING;
        }
    }



}

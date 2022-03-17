package at.htlkaindorf.ahif18.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Value<T> {
    private boolean constant;
    private T value;
}

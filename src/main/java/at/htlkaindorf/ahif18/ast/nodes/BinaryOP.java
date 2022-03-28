package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.tokens.Token;
import lombok.Data;

@Data

public class BinaryOP extends Expr {

    private Token type;
    private Expr left, right;

    public Value evaluate() {
        return new Value<>(0);
    }

}

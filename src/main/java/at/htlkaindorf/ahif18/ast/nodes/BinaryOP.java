package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.eval.ValueMismatchException;
import at.htlkaindorf.ahif18.parser.ParserException;
import at.htlkaindorf.ahif18.tokens.Token;
import lombok.Data;

@Data

public class BinaryOP extends Expr {

    private Token type;
    private Expr left, right;

    public Value evaluate() {
        switch (type.getType()) {
            case TK_PLUS -> {
                try {
                    return left.evaluate().add(right.evaluate());
                } catch (ParserException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Value();
    }

}

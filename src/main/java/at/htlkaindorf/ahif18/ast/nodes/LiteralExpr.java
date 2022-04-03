package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.tokens.Token;
import lombok.Data;

@Data
public class LiteralExpr extends Expr {

    private Value value;

    public LiteralExpr(Token token) {
        switch (token.getType()) {
            case LT_NUMBER -> {
                value = new Value(Double.parseDouble(token.getLexeme()));
            }
            case LT_STRING -> {
                value = new Value(token.getLexeme());
            }
            default -> {
                value = new Value();
            }
        }
    }

    public LiteralExpr negate() {
        setValue(new Value(-getValue().getNumValue()));
        return this;
    }

    @Override
    public Value eval() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.tokens.Token;
import lombok.Data;

/**
 * Expression that holds a literal value
 *
 * @author Martin Juritsch
 * @version 1.2
 * @since 1.0
 */
@Data
public class LiteralExpr extends Expr {

    private Value value;

    public LiteralExpr(Token token) {
        value = switch (token.getType()) {
            case LT_NUMBER -> new Value(Double.parseDouble(token.getLexeme()));
            case LT_STRING ->  new Value(token.getLexeme(), true);
            case LT_BOOL -> new Value(Boolean.parseBoolean(token.getLexeme()));
            default -> new Value();
        };
    }

    /**
     * Inverts the sign of the numeric value
     *
     * @return self
     * @since 1.0
     */
    public LiteralExpr negate() {
        setValue(new Value(-getValue().getNumValue()));
        return this;
    }

    /**
     * Eval method
     * Returns the unchanged value
     *
     * @return value that this expression holds
     * @since 1.0
     */
    @Override
    public Value eval() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

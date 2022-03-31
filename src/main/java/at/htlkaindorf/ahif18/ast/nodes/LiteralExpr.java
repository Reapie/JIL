package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.tokens.Token;
import at.htlkaindorf.ahif18.tokens.TokenType;
import lombok.Data;

@Data
public class LiteralExpr extends Expr {

    private Value value;

    public LiteralExpr(Token token) {
        switch (token.getType()) {
            case LT_NUMBER : {
                value = new Value(Double.parseDouble(token.getLexeme()));
                break;
            }
            case LT_STRING : {
                value = new Value(token.getLexeme().replace("\"", ""));
                break;
            }
            default: {
                value = new Value();
            }
        }
    }

    @Override
    public Value eval() {
        return value;
    }

    @Override
    public String print() {
        return value.toString();
    }
}

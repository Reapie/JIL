package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.parser.ParserException;
import at.htlkaindorf.ahif18.tokens.Token;
import lombok.Data;

@Data
public class BinaryExpr extends Expr {

    private Token type;
    private Expr left, right;

    public BinaryExpr(Token type, Expr left, Expr right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }

    @Override
    public Value eval() {
        try {
            switch (type.getType()) {
                case TK_PLUS -> {
                    return left.eval().add(right.eval());
                }
                case TK_MINUS -> {
                    return left.eval().sub(right.eval());
                }
                case TK_MUL -> {
                    return left.eval().mul(right.eval());
                }
                case TK_DIV -> {
                    return left.eval().div(right.eval());
                }
                case TK_POW -> {
                    return left.eval().pow(right.eval());
                }
                default -> {
                    throw new ParserException("Unknown binary operator: '" + type.getLexeme() + "'");
                }
            }
        } catch (ParserException e) {
            e.printStackTrace();
            return new Value();
        }
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + type.getLexeme() + " " + right.toString() + ")";
    }

}

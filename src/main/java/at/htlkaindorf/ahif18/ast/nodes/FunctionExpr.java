package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.tokens.Token;

public class FunctionExpr extends Expr {

    public enum FUNCTION {
        SQRT, PYTAG,
        SIN, COS, TAN,
        FLOOR, CEIL,

        CUSTOM
    }

    private Token identifier;
    private FUNCTION type;
    private Expr argument;

    public FunctionExpr(Token identifier, Expr argument) {
        this.identifier = identifier;
        this.argument = argument;
        type = switch (identifier.getLexeme().toLowerCase()) {
            case "sqrt" -> FUNCTION.SQRT;
            case "pytag" -> FUNCTION.PYTAG;
            case "sin" -> FUNCTION.SIN;
            case "cos" -> FUNCTION.COS;
            case "tan" -> FUNCTION.TAN;
            case "floor" -> FUNCTION.FLOOR;
            case "ceil" -> FUNCTION.CEIL;
            default -> FUNCTION.CUSTOM;
        };
    }

    @Override
    public Value eval() {
        return switch (type) {
            case SQRT -> sqrt();
            case SIN -> sin();
            case COS -> cos();
            case TAN -> tan();
            default -> null;
        };
    }

    private Value sqrt() {
        return new Value(Math.sqrt(argument.eval().getNumValue()));
    }

    private Value sin() {
        return new Value(Math.sin(argument.eval().getNumValue()));
    }

    private Value cos() {
        return new Value(Math.cos(argument.eval().getNumValue()));
    }

    private Value tan() {
        return new Value(Math.tan(argument.eval().getNumValue()));
    }

    @Override
    public String toString() {
        return null;
    }
}

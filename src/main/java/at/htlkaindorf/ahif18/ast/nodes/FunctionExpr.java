package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.eval.EvaluatorException;
import at.htlkaindorf.ahif18.tokens.Token;

import java.util.ArrayList;

public class FunctionExpr extends Expr {

    public enum FUNCTION {
        SQRT, HYPOT,
        SIN, COS, TAN,
        FLOOR, CEIL,

        CUSTOM
    }

    private Token identifier;
    private FUNCTION type;
    private ArrayList<Expr> params;

    public FunctionExpr(Token identifier, ArrayList<Expr> params) {
        this.identifier = identifier;
        this.params = params;
        type = switch (identifier.getLexeme().toLowerCase()) {
            case "sqrt" -> FUNCTION.SQRT;
            case "hypot" -> FUNCTION.HYPOT;
            case "sin" -> FUNCTION.SIN;
            case "cos" -> FUNCTION.COS;
            case "tan" -> FUNCTION.TAN;
            case "floor" -> FUNCTION.FLOOR;
            case "ceil" -> FUNCTION.CEIL;
            default -> FUNCTION.CUSTOM;
        };
    }

    @Override
    public Value eval() throws EvaluatorException {
        return switch (type) {
            case HYPOT -> hypot();
            case SQRT -> sqrt();
            case SIN -> sin();
            case COS -> cos();
            case TAN -> tan();
            default -> null;
        };
    }

    private void sizeCheck(int size) throws EvaluatorException {
        if (params.size() != size) {
            throw new EvaluatorException("Function " + identifier.getLexeme() + " expects " + size + " parameters, but got " + params.size());
        }
    }

    private Value hypot() throws EvaluatorException {
        sizeCheck(2);
        return new Value(Math.sqrt(Math.pow(params.get(0).eval().getNumValue(), 2) + Math.pow(params.get(1).eval().getNumValue(), 2)));
    }

    private Value sqrt() throws EvaluatorException {
        sizeCheck(1);
        return new Value(Math.sqrt(params.get(0).eval().getNumValue()));
    }

    private Value sin() throws EvaluatorException {
        sizeCheck(1);
        return new Value(Math.sin(params.get(0).eval().getNumValue()));
    }

    private Value cos() throws EvaluatorException {
        sizeCheck(1);
        return new Value(Math.cos(params.get(0).eval().getNumValue()));
    }

    private Value tan() throws EvaluatorException {
        sizeCheck(1);
        return new Value(Math.tan(params.get(0).eval().getNumValue()));
    }

    @Override
    public String toString() {
        return identifier.getLexeme() + "(" + params.toString() + ")";
    }
}

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
                    return add(right.eval());
                }
                case TK_MINUS -> {
                    return sub(right.eval());
                }
                case TK_MUL -> {
                    return mul(right.eval());
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

    private Value add(Value other) throws ParserException {

        Value.TYPES type = left.eval().getType();
        String strValue = left.eval().getStrValue();
        double numValue = left.eval().getNumValue();

        if (type == other.getType()) {
            if (type == Value.TYPES.NUMBER) {
                numValue+= right.eval().getNumValue();
                return new Value(numValue);
            } else if (type == Value.TYPES.STRING) {
                strValue = strValue.concat(other.getStrValue());
                return new Value(strValue);
            }
        }
        return new Value();
    }

    private Value sub(Value other) throws ParserException {
        Value.TYPES type = left.eval().getType();
        String strValue = left.eval().getStrValue();
        double numValue = left.eval().getNumValue();

        if (type == other.getType()) {
            if (type == Value.TYPES.NUMBER) {
                numValue -= other.getNumValue();
                return new Value(numValue);
            }
        }
        return new Value();
    }

    private Value mul(Value other) throws ParserException {

        Value.TYPES type = left.eval().getType();
        String strValue = left.eval().getStrValue();
        double numValue = left.eval().getNumValue();

        if (type == other.getType()) {
            if (type == Value.TYPES.NUMBER) {
                numValue *= other.getNumValue();
                return new Value(numValue);
            }
            // not very pretty but it works :)
        } else if (type == Value.TYPES.STRING && other.getType() == Value.TYPES.NUMBER) {
            double num = other.getNumValue();
            if (num < 0) {other.setNumValue(num * -1);}
            strValue = new String(new char[(int) other.getNumValue()]).replace("\0", strValue);

            return new Value(strValue);
        } else if (type == Value.TYPES.NUMBER && other.getType() == Value.TYPES.STRING) {
            double num = numValue;
            if (num < 0) {numValue = num * -1;}
            strValue = new String(new char[(int) numValue]).replace("\0", other.getStrValue());
           
            return new Value(strValue);
        }
        return new Value();
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + type.getLexeme() + " " + right.toString() + ")";
    }

}

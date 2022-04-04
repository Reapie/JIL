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
                    return add();
                }
                case TK_MINUS -> {
                    return sub();
                }
                case TK_MUL -> {
                    return mul();
                }
                case TK_DIV -> {
                    return div();
                }
                case TK_POW -> {
                    return pow();
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

    private Value add() throws ParserException {

        Value leftVal = left.eval();
        Value rightVal = right.eval();

        if (leftVal.getType() == rightVal.getType()) {
            if (leftVal.getType() == Value.TYPES.NUMBER) {
                return new Value(leftVal.getNumValue() + rightVal.getNumValue());
            } else if (leftVal.getType() == Value.TYPES.STRING) {
                String newVal = leftVal.getStrValue().concat(rightVal.getStrValue());
                return new Value(newVal);
            }
        }
        return new Value();
    }

    private Value sub() throws ParserException {

        Value leftVal = left.eval();
        Value rightVal = right.eval();

        if (leftVal.getType() == rightVal.getType()) {
            if (leftVal.getType() == Value.TYPES.NUMBER) {
                return new Value(leftVal.getNumValue() - rightVal.getNumValue());
            }
        }
        return new Value();
    }

    private Value mul() throws ParserException {

        Value leftVal = left.eval();
        Value rightVal = right.eval();

        if (leftVal.getType() == rightVal.getType()) {
            if (leftVal.getType() == Value.TYPES.NUMBER) {
                return new Value(leftVal.getNumValue() * rightVal.getNumValue());
            }
            // not very pretty but it works :)
        } else if (leftVal.getType() == Value.TYPES.STRING && rightVal.getType() == Value.TYPES.NUMBER) {
            double num = rightVal.getNumValue();
            // dont allow string multiplication with negative numbers
            if (num < 0) {rightVal.setNumValue(num * -1);}
            String strValue = new String(new char[(int) rightVal.getNumValue()])
                    .replace("\0", leftVal.getStrValue());
            return new Value(strValue);
        } else if (leftVal.getType() == Value.TYPES.NUMBER && rightVal.getType() == Value.TYPES.STRING) {
            double num = leftVal.getNumValue();
            // dont allow string multiplication with negative numbers
            if (num < 0) {leftVal.setNumValue(num * -1);}
            String strValue = new String(new char[(int) leftVal.getNumValue()])
                    .replace("\0", rightVal.getStrValue());
            return new Value(strValue);
        }
        return new Value();
    }

    private Value div() throws ParserException {

        Value leftVal = left.eval();
        Value rightVal = right.eval();

        if (leftVal.getType() == rightVal.getType()) {
            if (leftVal.getType() == Value.TYPES.NUMBER) {
                return new Value(leftVal.getNumValue() / rightVal.getNumValue());
            }
        }
        return new Value();
    }

    public Value pow() throws ParserException {

        Value leftVal = left.eval();
        Value rightVal = right.eval();

        if (leftVal.getType() == rightVal.getType()) {
            if (leftVal.getType() == Value.TYPES.NUMBER) {
                return new Value(Math.pow(leftVal.getNumValue(), rightVal.getNumValue()));
            }
        }
        return new Value();
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + type.getLexeme() + " " + right.toString() + ")";
    }

}

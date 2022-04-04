package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.parser.ParserException;
import at.htlkaindorf.ahif18.tokens.Token;
import lombok.Data;

@Data
public class BinaryExpr extends Expr {

    private Token type;
    private Expr left, right;
    private Value leftV, rightV;

    public BinaryExpr(Token type, Expr left, Expr right) {
        this.type = type;
        this.left = left;
        this.right = right;
        leftV = left.eval();
        rightV = right.eval();
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
                case TK_LESS -> {
                    return less();
                }
                case TK_GT -> {
                    return greater();
                }
                case TK_LEG -> {
                    return lessEqual();
                }
                case TK_GEQ -> {
                    return greaterEqual();
                }
                case TK_EQ -> {
                    return equal();
                }
                case TK_NOTEQ -> {
                    return notEqual();
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

    // BASIC MATH

    private Value add() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(leftV.getNumValue() + rightV.getNumValue());
            } else if (leftV.getType() == Value.TYPES.STRING) {
                String strValue = leftV.getStrValue().concat(rightV.getStrValue());
                return new Value(strValue, false);
            }
        }
        return new Value();
    }

    private Value sub() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(leftV.getNumValue() - rightV.getNumValue());
            }
        }
        return new Value();
    }

    private Value mul() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(leftV.getNumValue() * rightV.getNumValue());
            }
            // not very pretty but it works :)
        } else if (leftV.getType() == Value.TYPES.STRING && rightV.getType() == Value.TYPES.NUMBER) {
            double num = rightV.getNumValue();
            // dont allow string multiplication with negative numbers
            if (num < 0) {
                rightV.setNumValue(num * -1);
            }
            String strValue = new String(new char[(int) rightV.getNumValue()])
                    .replace("\0", leftV.getStrValue());
            return new Value(strValue, false);
        } else if (leftV.getType() == Value.TYPES.NUMBER && rightV.getType() == Value.TYPES.STRING) {
            double num = leftV.getNumValue();
            // dont allow string multiplication with negative numbers
            if (num < 0) {
                leftV.setNumValue(num * -1);
            }
            String strValue = new String(new char[(int) leftV.getNumValue()])
                    .replace("\0", rightV.getStrValue());
            return new Value(strValue, false);
        }
        return new Value();
    }

    private Value div() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(leftV.getNumValue() / rightV.getNumValue());
            }
        }
        return new Value();
    }

    public Value pow() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(Math.pow(leftV.getNumValue(), rightV.getNumValue()));
            }
        }
        return new Value();
    }

    // BOOLEANS AND COMPARISONS

    private Value less() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(leftV.getNumValue() < rightV.getNumValue());
            } else if (leftV.getType() == Value.TYPES.STRING) {
                return new Value(leftV.getStrValue().compareTo(rightV.getStrValue()) < 0);
            }
        }
        return new Value();
    }

    private Value greater() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(leftV.getNumValue() > rightV.getNumValue());
            } else if (leftV.getType() == Value.TYPES.STRING) {
                return new Value(leftV.getStrValue().compareTo(rightV.getStrValue()) > 0);
            }
        }
        return new Value();
    }

    private Value lessEqual() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(leftV.getNumValue() <= rightV.getNumValue());
            } else if (leftV.getType() == Value.TYPES.STRING) {
                return new Value(leftV.getStrValue().compareTo(rightV.getStrValue()) <= 0);
            }
        }
        return new Value();
    }

    private Value greaterEqual() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(leftV.getNumValue() >= rightV.getNumValue());
            } else if (leftV.getType() == Value.TYPES.STRING) {
                return new Value(leftV.getStrValue().compareTo(rightV.getStrValue()) >= 0);
            }
        }
        return new Value();
    }

    private Value equal() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(leftV.getNumValue() == rightV.getNumValue());
            } else if (leftV.getType() == Value.TYPES.STRING) {
                return new Value(leftV.getStrValue().equals(rightV.getStrValue()));
            } else if (leftV.getType() == Value.TYPES.BOOL) {
                return new Value(leftV.getBoolValue() == rightV.getBoolValue());
            }
        }
        return new Value();
    }

    private Value notEqual() throws ParserException {
        if (leftV.getType() == rightV.getType()) {
            if (leftV.getType() == Value.TYPES.NUMBER) {
                return new Value(leftV.getNumValue() != rightV.getNumValue());
            } else if (leftV.getType() == Value.TYPES.STRING) {
                return new Value(!leftV.getStrValue().equals(rightV.getStrValue()));
            } else if (leftV.getType() == Value.TYPES.BOOL) {
                return new Value(leftV.getBoolValue() != rightV.getBoolValue());
            }
        }
        return new Value();
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + type.getLexeme() + " " + right.toString() + ")";
    }

}

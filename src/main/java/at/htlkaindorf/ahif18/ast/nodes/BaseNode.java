package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.eval.ValueMismatchException;

public abstract class BaseNode {

    public Value evaluate() throws ValueMismatchException {
        return null;
    }

}

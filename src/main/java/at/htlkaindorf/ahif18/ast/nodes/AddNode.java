package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.eval.ValueMismatchException;

public class AddNode extends BaseNode {

    private BaseNode left, right;

    @Override
    public Value evaluate() throws ValueMismatchException {
        if (left.evaluate().getValue() instanceof String ls) {
            if (right.evaluate().getValue() instanceof String rs)
                left.evaluate().setValue(ls.concat(rs));
            else
                throw new ValueMismatchException();
        }
        return
    }
}

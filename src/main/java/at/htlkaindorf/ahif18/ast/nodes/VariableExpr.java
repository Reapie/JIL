package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;

/**
 * Expression that holds a variable
 * Unused
 *
 * @author Martin Juritsch
 * @version 1.2
 * @since 1.0
 */
public class VariableExpr extends Node {

    private String identifier;

    @Override
    public Value eval() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}

package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;

public abstract class Node {

    int lineNumber;

    public Value evaluate() {
        return new Value();
    }

}

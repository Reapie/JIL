package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;

public abstract class Node {

    int lineNumber;

    abstract public Value eval();

    abstract public String print();
}

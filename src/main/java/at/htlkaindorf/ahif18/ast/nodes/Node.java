package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.eval.EvaluatorException;

public abstract class Node {

    int lineNumber;

    abstract public Value eval() throws EvaluatorException;

    abstract public String toString();
}

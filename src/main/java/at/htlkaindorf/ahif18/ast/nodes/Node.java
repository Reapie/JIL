package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.eval.EvaluatorException;

/**
 * Base Node Class
 * Unused and intended for nods that are not expressions
 *
 * @author Martin Juritsch
 * @version 1.2
 * @since 1.0
 */
public abstract class Node {

    int lineNumber;

    abstract public Value eval() throws EvaluatorException;

    abstract public String toString();
}

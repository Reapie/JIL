package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.eval.EvaluatorException;

/**
 * Base Expression Class
 *
 * @author Martin Juritsch
 * @version 1.2
 * @since 1.0
 */
public abstract class Expr extends Node {
    public abstract Value eval() throws EvaluatorException;
}

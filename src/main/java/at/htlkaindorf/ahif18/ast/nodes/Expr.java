package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.eval.EvaluatorException;

public abstract class Expr extends Node {
    public abstract Value eval() throws EvaluatorException;
}

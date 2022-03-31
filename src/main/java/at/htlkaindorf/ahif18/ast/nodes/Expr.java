package at.htlkaindorf.ahif18.ast.nodes;

import at.htlkaindorf.ahif18.ast.Value;

public abstract class Expr extends Node {
    public abstract Value eval();
}

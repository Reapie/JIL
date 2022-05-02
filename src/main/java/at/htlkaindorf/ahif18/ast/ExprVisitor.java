package at.htlkaindorf.ahif18.ast;

import at.htlkaindorf.ahif18.ast.nodes.BinaryExpr;
import at.htlkaindorf.ahif18.ast.nodes.FunctionExpr;
import at.htlkaindorf.ahif18.ast.nodes.LiteralExpr;
import at.htlkaindorf.ahif18.ast.nodes.VariableExpr;

public interface ExprVisitor {

    public void visit(VariableExpr node);
    public void visit(LiteralExpr node);
    public void visit(BinaryExpr node);
    public void visit(FunctionExpr node);

}

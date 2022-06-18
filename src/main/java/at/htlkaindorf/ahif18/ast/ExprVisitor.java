package at.htlkaindorf.ahif18.ast;

import at.htlkaindorf.ahif18.ast.nodes.BinaryExpr;
import at.htlkaindorf.ahif18.ast.nodes.FunctionExpr;
import at.htlkaindorf.ahif18.ast.nodes.LiteralExpr;
import at.htlkaindorf.ahif18.ast.nodes.VariableExpr;

/**
 * Visitor for variable Assignment
 * Unused
 *
 * @author Martin Juritsch
 * @version 1.2
 * @since 1.2
 */
public interface ExprVisitor {

    public void visit(VariableExpr node);
    public void visit(LiteralExpr node);
    public void visit(BinaryExpr node);
    public void visit(FunctionExpr node);

}

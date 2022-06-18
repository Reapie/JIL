package at.htlkaindorf.ahif18.ast;

import lombok.Data;


import at.htlkaindorf.ahif18.ast.nodes.Node;

import java.util.TreeMap;
/**
 * Abstract Syntax Tree
 * Created by Parser and used by Evaluator
 *
 * @author Fabian Ladenhaufen
 * @version 1.2
 * @since 1.0
 */
public class AST {

    private Node root;

    private TreeMap<String, Value> variables = new TreeMap<>();

    /**
     * Setter for the root node
     *
     * @param node the new root node
     * @since 1.0
     */
    public void setRoot(Node node) {
        root = node;
    }

    /**
     * Getter for the root node
     *
     * @return the root node
     * @since 1.0
     */
    public Node getRoot() {
        return root;
    }

    /**
     * Recursively prints the syntax tree
     *
     * @since 1.0
     */
    public void print() {
        System.out.println(root.toString());
    }

}

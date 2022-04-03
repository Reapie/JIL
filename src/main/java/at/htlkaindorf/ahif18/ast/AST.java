package at.htlkaindorf.ahif18.ast;

import lombok.Data;


import at.htlkaindorf.ahif18.ast.nodes.Node;

import java.util.TreeMap;
@Data
public class AST {

    private Node root;

    private TreeMap<String, Value> variables = new TreeMap<>();

    public void add(Node node) {
        root = node;
    }

    public void print() {
        System.out.println(root.toString());
    }

}

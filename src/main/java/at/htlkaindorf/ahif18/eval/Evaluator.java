package at.htlkaindorf.ahif18.eval;

import at.htlkaindorf.ahif18.ast.AST;
import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.ast.nodes.BinaryExpr;
import at.htlkaindorf.ahif18.lexer.Lexer;
import at.htlkaindorf.ahif18.parser.Parser;
import at.htlkaindorf.ahif18.tokens.TokenType;

public class Evaluator {

    private final AST ast;

    private Evaluator(AST ast) {
        this.ast = ast;
    }

    public Value evaluate() {
        return ast.getRoot().eval();
    }

    public static void main(String[] args) {
        String input = "(-3 + 5) * 'Hello'";
        System.out.println("Input: " + input);
        Lexer l = new Lexer(input);
        var tokens = l.lex();
        Parser p = new Parser(tokens);
        AST ast = p.parse();
        Evaluator e = new Evaluator(ast);
        ast.print();
        System.out.println(e.evaluate());
    }

}

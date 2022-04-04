package at.htlkaindorf.ahif18.eval;

import at.htlkaindorf.ahif18.ast.AST;
import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.ast.nodes.BinaryExpr;
import at.htlkaindorf.ahif18.lexer.Lexer;
import at.htlkaindorf.ahif18.parser.Parser;
import at.htlkaindorf.ahif18.tokens.TokenType;

import java.util.Scanner;

public class Evaluator {

    private final AST ast;

    private Evaluator(AST ast) {
        this.ast = ast;
    }

    public Value evaluate() {
        return ast.getRoot().eval();
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String input;
        Lexer l;
        Parser p;
        Evaluator e;
        try {
            do {
                System.out.println("Enter the expression to evaluate:");
                input = scan.nextLine();
                l = new Lexer(input);
                p = new Parser(l.lex());
                e = new Evaluator(p.parse());
                System.out.println(e.evaluate());
            } while(!"END".equals(input));
        } catch (Exception ignored) {}
    }
}

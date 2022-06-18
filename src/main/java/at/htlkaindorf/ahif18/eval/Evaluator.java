package at.htlkaindorf.ahif18.eval;

import at.htlkaindorf.ahif18.ast.AST;
import at.htlkaindorf.ahif18.ast.Value;
import at.htlkaindorf.ahif18.ast.nodes.BinaryExpr;
import at.htlkaindorf.ahif18.lexer.Lexer;
import at.htlkaindorf.ahif18.parser.Parser;
import at.htlkaindorf.ahif18.tokens.TokenType;

import java.util.Scanner;
/**
 * Recursive Descent Parser implementation
 *
 * @author Fabian Ladenhaufen
 * @version 1.0
 * @since 1.0
 */
public class Evaluator {

    private final AST ast;

    private Evaluator(AST ast) {
        this.ast = ast;
    }

    /**
     * Starts the lexing process
     * Descent over syntax tree starts here
     *
     * @return value of the syntax tree
     * @since 1.0
     */
    public Value evaluate() {
        try {
            return ast.getRoot().eval();
        } catch (EvaluatorException e) {
            return new Value(e.getMessage(), false);
        }
    }

    /**
     * Starts the lexing process
     * Descent over syntax tree starts here
     *
     * @return List of tokens in correct order
     * @since 1.0
     */
    public static String pipeline(String input) {
        Lexer l = new Lexer(input);
        Parser p = new Parser(l.lex());
        AST ast = p.parse();
        //ast.print();
        Evaluator e = new Evaluator(ast);
        return e.evaluate().toString();
    }

    /**
     * Method for testing and demo purposes
     *
     * @since 1.1
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String input;
        Lexer l;
        Parser p;
        Evaluator e;
        AST ast;
        try {
            do {
                System.out.println("Enter the expression to evaluate (END to exit):");
                input = scan.nextLine();
                if ("END".equals(input))
                    break;
                l = new Lexer(input);
                p = new Parser(l.lex());
                ast = p.parse();
                e = new Evaluator(ast);
                ast.print();
                System.out.println(e.evaluate());
            } while(true);
        } catch (Exception ignored) {}
    }
}

/*
 * Atishin Sadaf
 * Lexer/Parser/Interpreter
 * 
 * This program is a basic implementation of a 
 * language interpreter that includes a 
 * Lexer, Parser, and Interpreter for a 
 * simple scripting language similar to BASIC
 */

import java.util.LinkedList;

public class Basic 
{
    public static void main(String[] args) 
    {
        if (args.length == 0) 
        {
            System.out.println("input file");
            return;
        }

        // Lexical analysis - Lexer
        Lexer lexer = new Lexer();
        LinkedList<Token> tokens = lexer.lex(args[0]);

        // Syntax analysis - Parser
        Parser parser = new Parser(tokens);
        Node ast = parser.parse();

        // Interpreter
        Interpreter interpreter = new Interpreter(ast);
        interpreter.buildLinkedList((StatementsNode) ast);  // Setup the linked list
        try 
        {
            interpreter.interpret(((StatementsNode) ast).getStatements().getFirst());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}


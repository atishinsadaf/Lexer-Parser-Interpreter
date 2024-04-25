import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;

public class LexerTest 
{
    @Test
    void printLexerOutput() 
    {
        Lexer lexer = new Lexer();
        String file = "test_input.txt";
        LinkedList<Token> tokens = lexer.lex(file);

        // Print the TokenType and value of each token
        for (Token token : tokens) 
        {
            String output = token.getTokenType().toString(); // Get the token type
            if (token.getValue() != null && !token.getValue().isEmpty())
            {
                output = output + " (" + token.getValue() + ")"; // Token value
            }
            System.out.println(output);
        }
    }
   
    @Test
    public void testHandleStringLiteral() 
    {
        Lexer lexer = new Lexer();
        String file = "test_stringliteral.txt";
        LinkedList<Token> tokens = lexer.lex(file);

        // Print the TokenType and value of each token
        for (Token token : tokens) 
        {
            String output = token.getTokenType().toString(); // Get the token type
            if (token.getValue() != null && !token.getValue().isEmpty())
            {
                output = output + " (" + token.getValue() + ")"; // Token value
            }
            System.out.println(output);
        }

    }
    
}

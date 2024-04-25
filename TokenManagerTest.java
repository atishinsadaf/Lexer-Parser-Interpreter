import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

public class TokenManagerTest 
{
    private TokenManager tokenManager;
    private LinkedList<Token> tokens;

    @BeforeEach // Run beofore the other test methods
    public void beforeEach() 
    {
        tokens = new LinkedList<>();
        
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUS, "+", 0, 0));
        tokenManager = new TokenManager(tokens);
    }

    @Test
    public void testPeek() 
    {
        assertEquals("5", tokenManager.Peek(0).get().getValue()); // Check if 5 exists
    }

    @Test
    public void testPeekInvalidIndex() 
    {
        assertTrue(tokenManager.Peek(3).isEmpty()); // There is no 3
    }

    @Test
    public void testMoreTokens() 
    {
        assertTrue(tokenManager.MoreTokens());
    }

    @Test
    public void testMatchAndRemove() 
    {
        assertTrue(tokenManager.MatchAndRemove(Token.TokenType.NUMBER).isPresent());
        assertTrue(tokenManager.MoreTokens()); // After removing the NUMBER token, there should still be one token left, the PLUS token.
    }
}

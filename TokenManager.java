import java.util.LinkedList;
import java.util.Optional;

public class TokenManager 
{
	private LinkedList<Token> token;
	
	public TokenManager(LinkedList<Token> token)
	{
		this.token = token;
	}
	
	public Optional<Token> Peek(int j)
	{
	    if (j >= 0 && j < token.size()) 
	    {
	        // If j is a valid index, get the token at this position in the list
	        return Optional.of(token.get(j));
	    }
	    else 
	    {
	        // If j is not a valid index return null to indicate there's no token to peek at
	        return Optional.empty();
	    }
	}
	
	public boolean MoreTokens()
	{
		if (!token.isEmpty())
		{
			return true;
		}
		else
			return false;
	}
	
	public Optional<Token> MatchAndRemove(Token.TokenType t)
	{
		if (!token.isEmpty() && token.getFirst().getTokenType() == t) // Check if the token list is not empty and the first token's type matches
		{
			return Optional.of(token.removeFirst()); // Remove and return the first token in an Optional
		}
		return Optional.empty();
		
	}
}

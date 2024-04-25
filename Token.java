
public class Token 
{
	public enum TokenType
	{
		WORD, NUMBER, ENDOFLINE, STRINGLITERAL, LABEL,
		PRINT, READ, INPUT, DATA, GOSUB, FOR, TO, STEP, NEXT, RETURN, IF, THEN, FUNCTION, WHILE, END,
		EQUALS, PLUS, MINUS, MULTIPLY, DIVIDE, LESSTHAN, GREATERTHAN, LPAREN, RPAREN, COMMA, MODULO, DOLLARSIGN, SEMICOLON,
		LESSTHANOREQUAL, GREATERTHANOREQUAL, NOTEQUALS, STRING;
	}
	private TokenType token;
	private String value;
	private int lineNumber;
	private int position;
	
	Token(TokenType token, int lineNumber, int position)
	{
		this.token = token;
		this.lineNumber = lineNumber;
		this.position = position;
	}
	
	Token(TokenType token, String value, int lineNumber, int position)
	{
		this.token = token;
		this.value = value;
		this.lineNumber = lineNumber;
		this.position = position;
	}
	
	@Override
	public String toString() 
	{
	    if (value != null) // Check if the value is not null to add it to the string
	    {
	        return token + " (" + value + ")";
	    } 
	    else 
	    {
	        return token.toString(); // For tokens without a value, return the token type
	    }
	}

    // Getter methods
	int getLineNumber()
	{
		return lineNumber;
	}
	int getPosition()
	{
		return position;
	}
	String getValue()
	{
		return value;
	}
	TokenType getTokenType()
	{
		return token;
	}
}

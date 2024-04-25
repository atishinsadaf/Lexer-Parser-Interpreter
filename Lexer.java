import java.util.HashMap;
import java.util.LinkedList;

public class Lexer 
{
	private HashMap<String, Token.TokenType> knownWords = new HashMap<>();
	private HashMap<String, Token.TokenType> singleCharacterSymbols = new HashMap<>();
	private HashMap<String, Token.TokenType> doubleCharacterSymbols = new HashMap<>();
	
	public Lexer()
	{
		populateHashMaps(); // Initialize HashMaps with values
	}
	
	private void populateHashMaps() // Populate hash maps with tokens and symbols
	{
		knownWords.put("PRINT", Token.TokenType.PRINT);
		knownWords.put("READ", Token.TokenType.READ);
		knownWords.put("INPUT", Token.TokenType.INPUT);
		knownWords.put("DATA", Token.TokenType.DATA);
		knownWords.put("GOSUB", Token.TokenType.GOSUB);
		knownWords.put("FOR", Token.TokenType.FOR);
		knownWords.put("TO", Token.TokenType.TO);
		knownWords.put("STEP", Token.TokenType.STEP);
		knownWords.put("NEXT", Token.TokenType.NEXT);
		knownWords.put("RETURN", Token.TokenType.RETURN);
		knownWords.put("IF", Token.TokenType.IF);
		knownWords.put("THEN", Token.TokenType.THEN);
		knownWords.put("FUNCTION", Token.TokenType.FUNCTION);
		knownWords.put("WHILE", Token.TokenType.WHILE);
		knownWords.put("END", Token.TokenType.END);
		
		singleCharacterSymbols.put("=", Token.TokenType.EQUALS);
		singleCharacterSymbols.put("+", Token.TokenType.PLUS);
		singleCharacterSymbols.put("-", Token.TokenType.MINUS);
		singleCharacterSymbols.put("*", Token.TokenType.MULTIPLY);
		singleCharacterSymbols.put("/", Token.TokenType.DIVIDE);
		singleCharacterSymbols.put("<", Token.TokenType.LESSTHAN);
		singleCharacterSymbols.put(">", Token.TokenType.GREATERTHAN);
		singleCharacterSymbols.put("(", Token.TokenType.LPAREN);
		singleCharacterSymbols.put(")", Token.TokenType.RPAREN);
		singleCharacterSymbols.put(",", Token.TokenType.COMMA);
		singleCharacterSymbols.put("%", Token.TokenType.MODULO);
		singleCharacterSymbols.put("$", Token.TokenType.DOLLARSIGN);
		singleCharacterSymbols.put(";", Token.TokenType.SEMICOLON);
		doubleCharacterSymbols.put("<>", Token.TokenType.NOTEQUALS);
		doubleCharacterSymbols.put("<=", Token.TokenType.LESSTHANOREQUAL);
		doubleCharacterSymbols.put(">=", Token.TokenType.GREATERTHANOREQUAL);
	}

	public LinkedList<Token> lex(String file) // Tokenize the input string
	{
		CodeHandler codeHandler = CodeHandler.create(file);
		LinkedList<Token> token = new LinkedList<>(); // List to store the tokens
		int lineNumber = 1;
		int position = 1;
		
		while(!codeHandler.isDone()) // While there is data in CodeHandler
		{
			char character = codeHandler.Peek(0);
			
			// Move for space for tab
			if (character == ' ' || character == '\t')
			{
				position++;
				codeHandler.GetChar();
			}
			// Move for line feed
			else if (character == '\n')
			{
				Token endOfLineToken = new Token(Token.TokenType.ENDOFLINE, lineNumber, position);
				token.add(endOfLineToken);
				lineNumber++;
				position = 1; // new line
				codeHandler.GetChar();
			}
			// Consume the carriage return
			else if (character == '\r')
			{
				codeHandler.GetChar();
			}
		    // Check for the start of a string literal
		    else if (character == '"') 
		    {
		        Token stringLiteralToken = handleStringLiteral(codeHandler, lineNumber, position);
		        token.add(stringLiteralToken);
		        // After handling, update the position based on the length of the string literal
		        position += stringLiteralToken.getValue().length() + 2; // Account for the opening and closing quotes
		    }
			// If character is a letter, call proccessWord, add to list of tokens
			else if (character >= 'A' && character <= 'Z' || character >= 'a' && character <= 'z') 
			{
			    Token wordToken = processWord(codeHandler, lineNumber, position);
			    token.add(wordToken);
			}
			// If character is a number, call processNumber, add to list of tokens
			else if (character >= '0' && character <= '9')
			{
				Token numbers = processNumber(codeHandler, lineNumber, position);
				token.add(numbers);
			}
			else 
			{
			    // Check for double-character symbols
			    if (!codeHandler.isDone() && doubleCharacterSymbols.containsKey("" + character + codeHandler.Peek(1))) 
			    {
			        token.add(new Token(doubleCharacterSymbols.get("" + character + codeHandler.Peek(1)), lineNumber, position));
			        codeHandler.GetChar(); // Consume the first and second character of the symbol
			        codeHandler.GetChar(); 
			        position += 2; // Increment twice since it is a double symbol
			    } 
			    // Check for single-character symbols
			    else if (singleCharacterSymbols.containsKey(String.valueOf(character))) 
			    {
			        token.add(new Token(singleCharacterSymbols.get(String.valueOf(character)), lineNumber, position));
			        codeHandler.GetChar(); // Consume the character
			        position++;
			    }
			    // Handle unrecognized characters, throw an exception
			    else 
			    {
			        throw new RuntimeException("Unrecognized character: " + character + " line: " + lineNumber + " position: " + position);
			    }
			}
			//System.out.println("token " + token); //debug
			position++;
		}
		//System.out.println("tokens printed " + token); //debug

		return token;
	}
	
	private Token processWord(CodeHandler codeHandler, int lineNumber, int position) 
	{
	    StringBuilder word = new StringBuilder();
	    int startPosition = position; // Start position of the word

	    // While the next character is a letter, digit, or a symbol.
	    while (!codeHandler.isDone() &&
	          (Character.isLetterOrDigit(codeHandler.Peek(0)) ||
	           codeHandler.Peek(0) == '_' || codeHandler.Peek(0) == '$' ||
	           codeHandler.Peek(0) == '%')) 
	    {
	        word.append(codeHandler.GetChar()); // Append and consume the current character
	        position++; // Move forward in the input
	    }

	    String wordStr = word.toString();
	    // Check if the next character is a colon, then it is a LABEL
	    if (!codeHandler.isDone() && codeHandler.Peek(0) == ':') 
	    {
	        codeHandler.GetChar(); // Consume the colon
	        return new Token(Token.TokenType.LABEL, wordStr, lineNumber, startPosition); // Return a LABEL token
	    }
	    
	    if (knownWords.containsKey(wordStr.toUpperCase())) 
	    {
	        // If a known keyword, create a token with the correct TokenType
	        return new Token(knownWords.get(wordStr.toUpperCase()), null, lineNumber, startPosition);
	    } else 
	    {
	        // If not a known keyword, create a WORD token with the word as its value
	        return new Token(Token.TokenType.WORD, wordStr, lineNumber, startPosition);
	    }
	}

	private Token processNumber(CodeHandler codeHandler, int lineNumber, int position) 
    {
        StringBuilder number = new StringBuilder();
        int startPosition = position;

        while (!codeHandler.isDone() && (Character.isDigit(codeHandler.Peek(0)) || (codeHandler.Peek(0) == '.' && number.indexOf(".") == -1))) 
        {
            char currentChar = codeHandler.GetChar();
            number.append(currentChar);
            position++;
        }

        return new Token(Token.TokenType.NUMBER, number.toString(), lineNumber, startPosition);
    }
    
    private Token handleStringLiteral(CodeHandler codeHandler, int lineNumber, int position)
    {
    	StringBuilder stringLiteral = new StringBuilder();
    	codeHandler.GetChar();
    	
    	while (!codeHandler.isDone())
    	{
    		char currentCharacter = codeHandler.Peek(0);
    		
    		if (currentCharacter == '\\')
    		{
    			codeHandler.GetChar();
    			if (!codeHandler.isDone())
    			{
    				char nextCharacter = codeHandler.GetChar();
    				stringLiteral.append(nextCharacter);
    			}	
    		}
    		else if (currentCharacter == '"')
    		{
    			codeHandler.GetChar();
    			break;
    		}
    		else
    		{
    			stringLiteral.append(currentCharacter);
    			codeHandler.GetChar();
    		}
    		
    	}
    	
    	return new Token(Token.TokenType.STRINGLITERAL, stringLiteral.toString(), lineNumber, position);
    }
}

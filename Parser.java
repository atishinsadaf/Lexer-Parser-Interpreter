import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Parser 
{
	private TokenManager tokenManager;
	
	public Parser(LinkedList<Token> token) // Constructor
	{
		this.tokenManager = new TokenManager(token);
	}
	
	public boolean AcceptSeparators() // Consume and ignore seperator tokens like EndOfLine
	{
		boolean seperator = false;
		Optional<Token> nextToken = tokenManager.Peek(0);
		
		while(nextToken.isPresent() && nextToken.get().getTokenType() == Token.TokenType.ENDOFLINE)
		{
			tokenManager.MatchAndRemove(Token.TokenType.ENDOFLINE); // Remove the seperator 
			seperator = true;
			nextToken = tokenManager.Peek(0); // Peek and check if there is another seperator
		}
		return seperator; // Return true if atleast one seperator was found and then remove		
	}
	
	public Node parse()
	{
		AcceptSeparators();

		return Statements();
	}
	
	private Node expression() // Handles addition and subtraction operations
	{
		Optional<Token> currentToken = tokenManager.Peek(0);
	    Optional<Token> nextToken = tokenManager.Peek(1);

	    if (currentToken.isPresent() && currentToken.get().getTokenType() == Token.TokenType.WORD &&
	        nextToken.isPresent() && nextToken.get().getTokenType() == Token.TokenType.LPAREN) // Only Parsing a function call if the current token is a word followed by a left parenthesis
	    {
	        Node result = functionInvocation();
	        if (result != null) 
	        {
	            return result;
	        }
	    }

	    Node result = term();
	    Optional<Token> token = tokenManager.Peek(0);
	    
	    while (token.isPresent()) // Loop and handle adding and subtracting, while checking for operator precedence
	    {
	        Token.TokenType currentType = token.get().getTokenType();
	        if (currentType == Token.TokenType.PLUS || currentType == Token.TokenType.MINUS) 
	        {
	            tokenManager.MatchAndRemove(currentType);
	            Node right = term(); // Parse right side of the operation
	            if (currentType == Token.TokenType.PLUS) 
	            {
	                result = new MathOpNode(MathOpNode.MathOperation.ADD, result, right);
	            } 
	            else 
	            { // currentType is Token.TokenType.MINUS
	                result = new MathOpNode(MathOpNode.MathOperation.SUBTRACT, result, right);
	            }
	            token = tokenManager.Peek(0);
	        } 
	        else 
	        {
	            break; // Exit the loop if the token is not PLUS or MINUS
	        }
	    }
	    return result;
	}

	private Node term() // Handles multiplication and division operations
	{
		Node result = factor();
		Optional<Token> token = tokenManager.Peek(0);
		
		while (token.isPresent()) // Loop and handle multiplying and dividing while checking for operator precedence
		{
			Token.TokenType currentType = token.get().getTokenType();
			if(currentType == Token.TokenType.MULTIPLY || currentType == Token.TokenType.DIVIDE)
			{
				tokenManager.MatchAndRemove(currentType);
				Node right = factor();
				if (currentType == Token.TokenType.MULTIPLY) 
				{
	                result = new MathOpNode(MathOpNode.MathOperation.MULTIPLY, result, right);
	            } 
				else 
	            { 
	                result = new MathOpNode(MathOpNode.MathOperation.DIVIDE, result, right); // currentType is Token.TokenType.DIVIDE
	            }
				token = tokenManager.Peek(0);
			}
			else
			{
				break; // Exit the loop if the token is not a MULTIPLY or DIVIDE
			}
		}
		return result;
	}
	
	private Node factor() 
	{
	    Optional<Token> token = tokenManager.Peek(0);

	    if (!token.isPresent()) 
	    {
	        return null; // Return absense of a token
	    }

	    switch (token.get().getTokenType()) 
	    {
	        case NUMBER:
	            String value = tokenManager.MatchAndRemove(Token.TokenType.NUMBER).get().getValue();
	            
	            if (value.contains(".")) // Check if the value contains a decimal point to decide between FloatNode and IntegerNode
	            {
	                return new FloatNode(Float.parseFloat(value));
	            } 
	            else 
	            {
	                return new IntegerNode(Integer.parseInt(value));
	            }
	            
	        case LPAREN: // If token = '(' then remove it and parse the expression inside
	            tokenManager.MatchAndRemove(Token.TokenType.LPAREN);
	            Node inner = expression();
	            tokenManager.MatchAndRemove(Token.TokenType.RPAREN); // Check and remove the ')'
	            return inner;
	        
	        case MINUS:
	            tokenManager.MatchAndRemove(Token.TokenType.MINUS); // For unary minus consume the minus token and apply it to the next factor
	            Node negated = factor();
	            
	            return new MathOpNode(MathOpNode.MathOperation.MULTIPLY, new IntegerNode(-1), negated); // Represent the negation of the factor
	        
	        case WORD: 
	        	String name = tokenManager.MatchAndRemove(Token.TokenType.WORD).get().getValue();
	        	return new VariableNode(name);
	        
	        default:
	            return null;
	    }
	}
	
	private StatementsNode Statements()
	{
		StatementsNode statementsNode = new StatementsNode();
	    while (true) // Loops until it cant find any statements to parse then adds it to StatementsNode
	    {
	        AcceptSeparators(); // Handle any separators before parsing
	        Node statement = Statement();
	        if (statement == null) 
	        {
	            break; // Exit if no more statements can be parsed
	        }
	        else
	        {
	        	statementsNode.addStatement((StatementNode) statement);
	        }
	    }
	    return statementsNode;	
	}
	
	private StatementNode Statement()
	{
		Optional <Token> token = tokenManager.Peek(0);
		if(!token.isPresent())
		{
			return null;
		}
		
		Token.TokenType tokenType = token.get().getTokenType();

	    // Check for label
	    if (tokenType == Token.TokenType.LABEL) 
	    {
	        String label = tokenManager.MatchAndRemove(Token.TokenType.LABEL).get().getValue();
	        StatementNode statement = Statement(); // Recursively call Statement to get the actual statement following the label
	        return new LabeledStatementNode(label, statement);
	    } 
	    else if (tokenType == Token.TokenType.PRINT)
	    {
	        return PrintStatement();
	    } 
	    else if (tokenType == Token.TokenType.WORD && tokenManager.Peek(1).isPresent() && tokenManager.Peek(1).get().getTokenType() == Token.TokenType.EQUALS) {
	        return Assignment();
	    } 
	    else if (tokenType == Token.TokenType.READ)
	    {
	        return ReadStatement();
	    } 
	    else if (tokenType == Token.TokenType.DATA)
	    {
	        return DataStatement();
	    } 
	    else if (tokenType == Token.TokenType.INPUT)
	    {
	        
	    	return InputStatement();
	    } 
	    else if (tokenType == Token.TokenType.GOSUB)
	    {
	        return GOSUBStatement();
	    } 
	    else if (tokenType == Token.TokenType.RETURN)
	    {
	        return RETURNStatement();
	    } 
	    else if (tokenType == Token.TokenType.FOR) 
	    {
	        return FORStatement();
	    } 
	    else if (tokenType == Token.TokenType.NEXT) 
	    {
	        return NEXTStatement();
	    } 
	    else if (tokenType == Token.TokenType.IF) 
	    {
	        return IFStatement();
	    } 
	    else if (tokenType == Token.TokenType.WHILE)
	    {
	        return WHILEStatement();
	    } 
	    else if (tokenType == Token.TokenType.END)
	    {
	        return ENDStatement();
	    } 
	    else 
	    {
	        // If the token does not match any known statement type
	        return null;
	    }		
	}
	
	private PrintNode PrintStatement()
	{
		tokenManager.MatchAndRemove(Token.TokenType.PRINT); // Consume the PRINT token
		LinkedList<Node> printItems = PrintList();
		return new PrintNode(printItems);
	}
	
	private LinkedList<Node> PrintList() 
	{
	    LinkedList<Node> items = new LinkedList<>();
	    
	    do 
	    {
	        Optional<Token> nextToken = tokenManager.Peek(0);
	        Node item = null;

	        // Check the type of the next token and act accordingly
	        if (nextToken.isPresent()) 
	        {
	            switch (nextToken.get().getTokenType()) 
	            {
	                case STRING: // If next token is a string, create a StringNode
	                    String stringValue = tokenManager.MatchAndRemove(Token.TokenType.STRING).get().getValue();
	                    item = new StringNode(stringValue);
	                    break;
	                case NUMBER: // If the next token is a number, create the IntegerNode or FloatNode
	                case PLUS:   
	                case MINUS:
	                case LPAREN:
	                case WORD: 
	                    item = expression(); // expression() handles NUMBER, MINUS (for negative numbers), LPAREN, and WORD
	                    break;
	                default:
	                    break;
	            }

	            if (item != null) 
	            {
	                items.add(item);
	            }
	            nextToken = tokenManager.Peek(0); // Look for a comma to check if there are more items in the list
	            if (nextToken.isPresent() && nextToken.get().getTokenType() == Token.TokenType.COMMA) 
	            {
	                tokenManager.MatchAndRemove(Token.TokenType.COMMA); // Consume the comma
	            } 
	            else 
	            {
	                break; // End of the print list
	            }
	        } 
	        else 
	        {
	            break;
	        }
	    } while (true); 

	    return items;
	}

	private AssignmentNode Assignment() // Correct form should be "VARIABLE EQUALS expression"
	{
	    Optional<Token> token = tokenManager.Peek(0);// Peek at the next token to check it's a WORD
	    if (!token.isPresent() || token.get().getTokenType() != Token.TokenType.WORD) 
	    {
	        return null; 
	    }
	    
	    String variableName = tokenManager.MatchAndRemove(Token.TokenType.WORD).get().getValue(); // Consume the variable name token

	    token = tokenManager.Peek(0); // Peek for equals sign
	    if (!token.isPresent() || token.get().getTokenType() != Token.TokenType.EQUALS) 
	    {
	        return null; // Equals sign is missing
	    }
	   
	    tokenManager.MatchAndRemove(Token.TokenType.EQUALS); // Consume the equals sign
	    
	    Node rightSide = expression(); // Parse the right hand side expression
	    if (rightSide == null) 
	    {
	        return null;
	    }
	    return new AssignmentNode(new VariableNode(variableName), rightSide);
	}
	
	/*
	 * Parses a READ statement which consists of the READ keyword followed by list of variable names
	 * Consumes the READ token type then gathers all the variable names until a comma or word is encountered.
	 * Each variable name then converted into VariableNode and added to a LinkedList then returns
	 * a ReadNode containing the list of VariableNodes
	 */
	private ReadNode ReadStatement() 
	{
	    tokenManager.MatchAndRemove(Token.TokenType.READ); // Consume the READ token
	    LinkedList<VariableNode> variables = new LinkedList<>();

	    Optional<Token> token; // Hold current token being looked at
	    do 
	    {
	        token = tokenManager.Peek(0);
	        if (token.isPresent() && token.get().getTokenType() == Token.TokenType.WORD) 
	        {
	            String variableName = tokenManager.MatchAndRemove(Token.TokenType.WORD).get().getValue();
	            variables.add(new VariableNode(variableName));
	            
	            Optional<Token> nextToken = tokenManager.Peek(0); // Peek at next token to see if it's a comma, indicating more variables
	            if (nextToken.isPresent() && nextToken.get().getTokenType() == Token.TokenType.COMMA) 
	            {
	                tokenManager.MatchAndRemove(Token.TokenType.COMMA); // Consume comma if present and continue the loop for more variables
	            } 
	            else
	            {
	                break; 
	            }
	        } 
	        else 
	        {
	            break; 
	        }
	    } while (true);

	    return new ReadNode(variables);
	}
	    
	/*
	 * A DATA statement starts with the DATA keyword and followed by a list of data items 
	 * (strings, integers, or floats) separated by commas
	 */
	private DataNode DataStatement() 
	{
	    tokenManager.MatchAndRemove(Token.TokenType.DATA); // Consume DATA token
	    LinkedList<Node> dataValues = new LinkedList<>();

	    while (true) 
	    {
	        Optional<Token> nextToken = tokenManager.Peek(0);
	        if (!nextToken.isPresent()) break; // End of input or error

	        Node dataItem = null;
	        if (nextToken.get().getTokenType() == Token.TokenType.STRING) // Check if next tokentype is a String, then create a StringNode
	        {
	            String stringValue = tokenManager.MatchAndRemove(Token.TokenType.STRING).get().getValue();
	            dataItem = new StringNode(stringValue);
	        } 
	        else if (nextToken.get().getTokenType() == Token.TokenType.NUMBER) // Check if next tokentype is a number and creates either IntegerNode or FloatNode
	        {
	            String numberValue = tokenManager.MatchAndRemove(Token.TokenType.NUMBER).get().getValue();
	            if (numberValue.contains(".")) 
	            {
	                dataItem = new FloatNode(Float.parseFloat(numberValue));
	            } else 
	            {
	                dataItem = new IntegerNode(Integer.parseInt(numberValue));
	            }
	        } 
	        else 
	        {
	            break;
	        }

	        if (dataItem != null) 
	        {
	            dataValues.add(dataItem);
	        }
	        
	        Optional<Token> checkForComma = tokenManager.Peek(0); // If comma not present, end of DATA items
	        if (checkForComma.isPresent() && checkForComma.get().getTokenType() == Token.TokenType.COMMA) 
	        {
	            tokenManager.MatchAndRemove(Token.TokenType.COMMA); // Consume comma
	        } 
	        else 
	        {
	            break;
	        }
	    }

	    return new DataNode(dataValues);
	}

	/*
	 * An INPUT statement consists of the INPUT keyword optionally followed by a string prompt 
	 * and then a list of variable names separated by commas
	 */
	private InputNode InputStatement() 
	{
	    tokenManager.MatchAndRemove(Token.TokenType.INPUT); // Consume INPUT token
	    Node parameter = null;
	    LinkedList<VariableNode> variables = new LinkedList<>();

	    Optional<Token> firstToken = tokenManager.Peek(0); // Check for optional string prompt
	    
	    if (firstToken.isPresent() && firstToken.get().getTokenType() == Token.TokenType.STRING) // If a string token is present, consume and create a StringNode
	    {
	        parameter = new StringNode(firstToken.get().getValue());
	        tokenManager.MatchAndRemove(Token.TokenType.STRING); // Consume string
	    }

	    Optional<Token> token; // Parse list of variables
	    
	    do 
	    {
	        token = tokenManager.Peek(0);
	        if (token.isPresent() && token.get().getTokenType() == Token.TokenType.WORD) // If WORD token is present then it represents a variable name
	        {
	            String variableName = tokenManager.MatchAndRemove(Token.TokenType.WORD).get().getValue();
	            variables.add(new VariableNode(variableName));
	            
	            Optional<Token> nextToken = tokenManager.Peek(0);
	            
	            if (nextToken.isPresent() && nextToken.get().getTokenType() == Token.TokenType.COMMA) 
	            {
	                tokenManager.MatchAndRemove(Token.TokenType.COMMA); // Consume comma if present
	            } 
	            else 
	            {
	                break;
	            }
	        } 
	        else 
	        {
	            break;
	        }
	    } while (true);

	    return new InputNode(parameter, variables);
	}
	
	private StatementNode GOSUBStatement() 
	{
	    tokenManager.MatchAndRemove(Token.TokenType.GOSUB);
	    String label = tokenManager.MatchAndRemove(Token.TokenType.WORD).get().getValue();
	    return new GOSUBNode(label); // Return a GOSUB node with the label, representing the GOSUB statement in the AST
	}

	private StatementNode RETURNStatement() 
	{
	    tokenManager.MatchAndRemove(Token.TokenType.RETURN);
	    return new RETURNNode(); // Create and return a RETURN node, representing the RETURN statement in the AST
	}

	private StatementNode FORStatement() 
	{
	    tokenManager.MatchAndRemove(Token.TokenType.FOR);

	    Optional<Token> variableToken = tokenManager.MatchAndRemove(Token.TokenType.WORD);
	    if (!variableToken.isPresent()) 
	    {
	        throw new RuntimeException("Expected variable name after FOR");
	    }
	    String variable = variableToken.get().getValue();

	    tokenManager.MatchAndRemove(Token.TokenType.EQUALS); // Remove and consume the EQUALS sign token indicating the start value assignment of the loop

	    Node startExpression = expression();
	    tokenManager.MatchAndRemove(Token.TokenType.TO); // Remove and consume the TO token, indicating the end of the loop

	    Node endExpression = expression(); 

	    Optional<Token> stepToken = tokenManager.Peek(0); // Peeks at the next token without removing it
	    Node stepExpression = null;
	    if (stepToken.isPresent() && stepToken.get().getTokenType() == Token.TokenType.STEP) 
	    {
	        tokenManager.MatchAndRemove(Token.TokenType.STEP); // Removes the STEP token if present
	        stepExpression = expression(); // Parses the step expression if STEP is present
	    }
	    return new FORNode(variable, startExpression, endExpression, stepExpression);
	}

	private StatementNode NEXTStatement() 
	{
	    tokenManager.MatchAndRemove(Token.TokenType.NEXT);
	    String variable = tokenManager.MatchAndRemove(Token.TokenType.WORD).get().getValue();
	    return new NEXTNode(variable); // Create and return a NEXT node with the variable representing the NEXT statement in the AST
	}

	private StatementNode ENDStatement() 
	{
	    tokenManager.MatchAndRemove(Token.TokenType.END);
	    return new ENDNode(); // Create and return an END node, representing the END statement in the AST
	}

	private StatementNode IFStatement() 
	{
	    tokenManager.MatchAndRemove(Token.TokenType.IF);
	    Node condition = booleanExpression();
	    tokenManager.MatchAndRemove(Token.TokenType.THEN);
	    StatementNode thenStatement = Statement(); 
	    return new IFNode(condition, thenStatement); // Create and return an IF node with the condition and then statement, representing the IF statement in the AST
	}

	private StatementNode WHILEStatement() 
	{
	    tokenManager.MatchAndRemove(Token.TokenType.WHILE);
	    Node condition = booleanExpression();
	    StatementNode body = Statement();
	    return new WHILENode(condition, body); // Create and return a WHILE node with the condition and body, representing the WHILE loop in the AST
	}

	private Node booleanExpression() // Used in conditionals like IF and WHILE statements
	{
	    Node left = expression(); // Parse the left side of the boolean expression
	    
	    Token operatorToken = tokenManager.Peek(0).orElseThrow(() -> new RuntimeException("Expected operator in boolean expression"));

	    switch (operatorToken.getTokenType()) // Check if the next token is an operator valid in a boolean expression
	    {
	        case GREATERTHAN:
	        case LESSTHAN:
	        case GREATERTHANOREQUAL:
	        case LESSTHANOREQUAL:
	        case EQUALS:
	        case NOTEQUALS:
	            tokenManager.MatchAndRemove(operatorToken.getTokenType()); // Valid operator for a boolean expression, consume it
	            break;
	        default:
	            throw new RuntimeException("Unexpected token for boolean operator: " + operatorToken.getTokenType());
	    }

	    Node right = expression(); // Parse the right side
	    return new BooleanExpressionNode(left, operatorToken.getTokenType(), right);
	}

	private Node functionInvocation() // Looking for a function name followed by parentheses and optional parameters
	{
	    if (!tokenManager.Peek(0).isPresent() || tokenManager.Peek(0).get().getTokenType() != Token.TokenType.WORD) 
	    {
	        return null; // No valid function name found
	    }

	    String functionName = tokenManager.MatchAndRemove(Token.TokenType.WORD).get().getValue(); // Extract function name

	    if (!tokenManager.Peek(0).isPresent() || tokenManager.Peek(0).get().getTokenType() != Token.TokenType.LPAREN) // Expect a left parenthesis next
	    {
	        throw new RuntimeException("Expected '(' after function name in function invocation");
	    }
	    tokenManager.MatchAndRemove(Token.TokenType.LPAREN); // Consume LPAREN

	    List<Node> parameters = new ArrayList<>();

	    while (!tokenManager.Peek(0).isPresent() || tokenManager.Peek(0).get().getTokenType() != Token.TokenType.RPAREN) 
	    {
	        
	        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.STRINGLITERAL) // If next token is STRINGLITERAL, consume it as a parameter
	        {
	            parameters.add(new StringNode(tokenManager.MatchAndRemove(Token.TokenType.STRINGLITERAL).get().getValue()));
	        } 
	        else // Otherwise treat as expression
	        {
	            parameters.add(expression()); 
	        }

	        
	        if (tokenManager.Peek(0).isPresent() && tokenManager.Peek(0).get().getTokenType() == Token.TokenType.COMMA) // If the next token is a COMMA, consume it and move to the next parameter
	        {
	            tokenManager.MatchAndRemove(Token.TokenType.COMMA);
	        } 
	        else 
	        {
	            break; // If no COMMA is found, expect a RPAREN next
	        }
	    }

	    if (!tokenManager.Peek(0).isPresent() || tokenManager.Peek(0).get().getTokenType() != Token.TokenType.RPAREN) // Next token must be RPAREN, consume it
	    {
	        throw new RuntimeException("Expected ')' at the end of function invocation");
	    }
	    tokenManager.MatchAndRemove(Token.TokenType.RPAREN); // Consume RPAREN

	    return new FunctionInvocationNode(functionName, parameters);
	}
}
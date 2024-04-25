
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.util.LinkedList;

public class ParserTest 
{
    private Parser parser;
/*	
	// PARSER 1 UNIT TESTS BELOW
    @Test
    public void testAddition() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUS, "+", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "3", 0, 0));
        parser = new Parser(tokens);
        Node result = parser.parse();
        
        assertTrue(result instanceof MathOpNode);
        assertEquals(MathOpNode.MathOperation.ADD, ((MathOpNode)result).getOperation());
    }
   
    @Test
    public void testSubtraction() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));
        tokens.add(new Token(Token.TokenType.MINUS, "-", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "3", 0, 0));
        parser = new Parser(tokens);
        Node result = parser.parse();
        
        assertTrue(result instanceof MathOpNode);
        assertEquals(MathOpNode.MathOperation.SUBTRACT, ((MathOpNode)result).getOperation());
    }
    
    @Test
    public void testMultiplication() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.NUMBER, "4", 0, 0)); 
        tokens.add(new Token(Token.TokenType.MULTIPLY, "*", 0, 0)); 
        tokens.add(new Token(Token.TokenType.NUMBER, "2", 0, 0)); 
        parser = new Parser(tokens); 
        Node result = parser.parse(); 
        
        assertTrue(result instanceof MathOpNode); // Check the type of the node
        assertEquals(MathOpNode.MathOperation.MULTIPLY, ((MathOpNode)result).getOperation()); // Verify operation
        assertEquals("4", ((MathOpNode)result).getLeft().toString()); // Verify left adn right operand
        assertEquals("2", ((MathOpNode)result).getRight().toString()); 
    }

    @Test
    public void testDivision() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.NUMBER, "8", 0, 0)); 
        tokens.add(new Token(Token.TokenType.DIVIDE, "/", 0, 0)); 
        tokens.add(new Token(Token.TokenType.NUMBER, "2", 0, 0)); 
        parser = new Parser(tokens); 
        Node result = parser.parse(); 
        
        assertTrue(result instanceof MathOpNode); // Check the type of the node
        assertEquals(MathOpNode.MathOperation.DIVIDE, ((MathOpNode)result).getOperation()); // Verify operation
        assertEquals("8", ((MathOpNode)result).getLeft().toString()); // Verify left and right operands
        assertEquals("2", ((MathOpNode)result).getRight().toString()); 
    }
  
    @Test
    public void testParentheses() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.LPAREN, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "1", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUS, "+", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "2", 0, 0));
        tokens.add(new Token(Token.TokenType.RPAREN, ")", 0, 0));
        tokens.add(new Token(Token.TokenType.MULTIPLY, "*", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "3", 0, 0));
        parser = new Parser(tokens);
        Node result = parser.parse();
        
        // Check the outer operation is multiplication
        assertTrue(result instanceof MathOpNode); // Result should be an operation node
        assertEquals(MathOpNode.MathOperation.MULTIPLY, ((MathOpNode)result).getOperation()); // Operation of the root node is MULTIPLY
        // Check the left child is an addition operation
        assertTrue(((MathOpNode)result).getLeft() instanceof MathOpNode); // Left child of the root MathOpNode is itself an instance of MathOpNode
        assertEquals(MathOpNode.MathOperation.ADD, ((MathOpNode)((MathOpNode)result).getLeft()).getOperation());
    }
*/

	
	// PARSER 2 UNIT TESTS BELOW
    
    @Test
    public void testPrintStatement() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.PRINT, "print", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "311", 0, 0));
        Parser parser = new Parser(tokens);
        Node result = parser.parse();
        
        assertTrue(result instanceof StatementsNode, "Result = StatementsNode");

        StatementsNode statements = (StatementsNode) result;// Verify StatementsNode contains only 1 PrintNode
        
        assertEquals(1, statements.getStatements().size(), "Should contain 1 statement");
        assertTrue(statements.getStatements().get(0) instanceof PrintNode, "Statement should be a PrintNode");
    }

    @Test
    public void testAssignment() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.EQUALS, "=", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "1", 0, 0));

        parser = new Parser(tokens);
        Node result = parser.parse();

        assertTrue(result instanceof StatementsNode, "Result equals StatementsNode");
        assertEquals(1, ((StatementsNode)result).getStatements().size(), "Contain one statement");
        assertTrue(((StatementsNode)result).getStatements().get(0) instanceof AssignmentNode, "Statement = AssignmentNode");
    }
    
    @Test
    public void testStatements() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.PRINT, "print", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "42", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.EQUALS, "=", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));

        parser = new Parser(tokens);
        Node result = parser.parse();

        assertTrue(result instanceof StatementsNode, "Result should equal StatementsNode");
        assertEquals(2, ((StatementsNode)result).getStatements().size(), "Contains two statements");
        assertTrue(((StatementsNode)result).getStatements().get(0) instanceof PrintNode, "First statement is PrintNode");
        assertTrue(((StatementsNode)result).getStatements().get(1) instanceof AssignmentNode, "Second is AssignmentNode");
    }
    
    @Test
    public void testFactorWithNumbers() throws Exception 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.NUMBER, "3", 0, 0));

        Parser parser = new Parser(tokens);
        Method method = Parser.class.getDeclaredMethod("factor"); // Access the private method factor using reflection
        method.setAccessible(true); // Makes the method accessible
        Node result = (Node) method.invoke(parser); 

        assertTrue(result instanceof IntegerNode, "Factor IntegerNode");
        assertEquals(3, ((IntegerNode)result).getNumber(), "Value should equal 3");
    }
    
    @Test
    public void testFactor() throws Exception 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.LPAREN, "(", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "3", 0, 0));
        tokens.add(new Token(Token.TokenType.RPAREN, ")", 0, 0));

        Parser parser = new Parser(tokens);
        Method factorMethod = Parser.class.getDeclaredMethod("factor"); // Use reflection to access the private factor method
        factorMethod.setAccessible(true); // Makes method accessible
        Node result = (Node) factorMethod.invoke(parser); // Apply the factor method on parser instance

        assertTrue(result instanceof IntegerNode, "Factor with parentheses parsed as IntegerNode");
        assertEquals(3, ((IntegerNode)result).getNumber(), "Value should equal 3 inside the parentheses");
    }
    
    @Test
    public void testExpression() throws Exception 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.NUMBER, "3", 0, 0));
        tokens.add(new Token(Token.TokenType.PLUS, "+", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "4", 0, 0));
        tokens.add(new Token(Token.TokenType.MULTIPLY, "*", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "5", 0, 0));

        Parser parser = new Parser(tokens);
        Method expressionMethod = Parser.class.getDeclaredMethod("expression"); // Reflection to access the private expression method
        expressionMethod.setAccessible(true);
        Node result = (Node) expressionMethod.invoke(parser);

        assertTrue(result instanceof MathOpNode, "Expression parsed as MathOpNode");
    }
    
    // PARSER 3 UNIT TESTS BELOW
    
    @Test
    public void testReadStatement() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.READ, "read", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "x", 0, 0));
        tokens.add(new Token(Token.TokenType.COMMA, ",", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "y", 0, 0));

        parser = new Parser(tokens);
        Node result = parser.parse();

        assertTrue(result instanceof StatementsNode); // Check if instance of StatementNode
        assertEquals(1, ((StatementsNode)result).getStatements().size()); // Check that there is only 1 statement parsed
        assertTrue(((StatementsNode)result).getStatements().get(0) instanceof ReadNode); // Check the first statement is ReadNode

        ReadNode readNode = (ReadNode)((StatementsNode)result).getStatements().get(0);
        assertEquals(2, readNode.getVariables().size()); // Check the ReadNode contains correct numbers of variables
    }

    @Test
    public void testDataStatement() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.DATA, "data", 0, 0));
        tokens.add(new Token(Token.TokenType.STRING, "\"Hello\"", 0, 0));
        tokens.add(new Token(Token.TokenType.COMMA, ",", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "123", 0, 0));

        parser = new Parser(tokens);
        Node result = parser.parse();

        assertTrue(result instanceof StatementsNode);
        assertEquals(1, ((StatementsNode)result).getStatements().size());
        assertTrue(((StatementsNode)result).getStatements().get(0) instanceof DataNode);

        DataNode dataNode = (DataNode)((StatementsNode)result).getStatements().get(0);
        assertEquals(2, dataNode.getDataValues().size());
    }

    // PARSER 4 unit tests below
    
    @Test
    void testENDStatement() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.END, "END", 1, 0));

        parser = new Parser(tokens);
        Node result = parser.parse();

        assertTrue(result instanceof StatementsNode); // Ensure result is a StatementsNode
        assertEquals(1, ((StatementsNode)result).getStatements().size()); // There should be one statement
        assertTrue(((StatementsNode)result).getStatements().get(0) instanceof ENDNode); // The statement should be an ENDNode
    }
    
    @Test
    void testGOSUBStatement() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.GOSUB, null, 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "100", 0, 0));
        
        Parser parser = new Parser(tokens);
        
        Node result = parser.parse();

        assertTrue(result instanceof StatementsNode);
        assertEquals(1, ((StatementsNode)result).getStatements().size());
        assertTrue(((StatementsNode)result).getStatements().get(0) instanceof GOSUBNode);
        assertEquals("100", ((GOSUBNode)((StatementsNode)result).getStatements().get(0)).getLabel());
    }
    
    @Test
    public void testRETURNStatement() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.RETURN, "RETURN", 0, 0));
        
        Parser parser = new Parser(tokens);
        Node result = parser.parse();
        
        assertTrue(result instanceof StatementsNode);
        assertTrue(((StatementsNode) result).getStatements().get(0) instanceof RETURNNode);
    }
    
    @Test
    public void testNEXTStatement() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.NEXT, "NEXT", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "i", 0, 0));
        
        Parser parser = new Parser(tokens);
        Node result = parser.parse();
        
        assertTrue(result instanceof StatementsNode);
        assertTrue(((StatementsNode) result).getStatements().get(0) instanceof NEXTNode);
        
        NEXTNode nextNode = (NEXTNode) ((StatementsNode) result).getStatements().get(0);
        
        assertEquals("i", nextNode.getVariable());
    }
    
    @Test
    public void testIFStatement() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.IF, "IF", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "1", 0, 0));
        tokens.add(new Token(Token.TokenType.EQUALS, "=", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "1", 0, 0));
        tokens.add(new Token(Token.TokenType.THEN, "THEN", 0, 0));
        tokens.add(new Token(Token.TokenType.PRINT, "PRINT", 0, 0));
        tokens.add(new Token(Token.TokenType.STRING, "\"True\"", 0, 0));
        
        Parser parser = new Parser(tokens);
        Node result = parser.parse();
        
        assertTrue(result instanceof StatementsNode);
        assertTrue(((StatementsNode) result).getStatements().get(0) instanceof IFNode);
        
        IFNode ifNode = (IFNode) ((StatementsNode) result).getStatements().get(0);
        
        assertTrue(ifNode.getCondition() instanceof BooleanExpressionNode);
        assertTrue(ifNode.getTrueStatement() instanceof PrintNode);
    }
    
    @Test
    public void testFORStatement() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.FOR, "FOR", 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "i", 0, 0));
        tokens.add(new Token(Token.TokenType.EQUALS, "=", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "1", 0, 0));
        tokens.add(new Token(Token.TokenType.TO, "TO", 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "10", 0, 0));
        
        Parser parser = new Parser(tokens);
        Node result = parser.parse();
        
        assertTrue(result instanceof StatementsNode);
        assertTrue(((StatementsNode) result).getStatements().get(0) instanceof FORNode);
        
        FORNode forNode = (FORNode) ((StatementsNode) result).getStatements().get(0);
        
        assertEquals("i", forNode.getVariable());
    }
    
    @Test
    public void testWHILEStatement() 
    {
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.WHILE, null, 0, 0));
        tokens.add(new Token(Token.TokenType.WORD, "i", 0, 0));
        tokens.add(new Token(Token.TokenType.LESSTHAN, null, 0, 0));
        tokens.add(new Token(Token.TokenType.NUMBER, "10", 0, 0));
        tokens.add(new Token(Token.TokenType.PRINT, null, 0, 0));
        tokens.add(new Token(Token.TokenType.STRING, "\"i is less than 10\"", 0, 0));

        Parser parser = new Parser(tokens);
        Node result = parser.parse();

        assertTrue(result instanceof StatementsNode, "Parsed result should be a StatementsNode");
        StatementsNode statements = (StatementsNode) result;
        assertEquals(1, statements.getStatements().size(), "Should contain one statement");
        assertTrue(statements.getStatements().get(0) instanceof WHILENode, "The statement should be a WHILENode");

        WHILENode whileNode = (WHILENode) statements.getStatements().get(0);
        assertTrue(whileNode.getCondition() instanceof BooleanExpressionNode, "The condition should be a BooleanExpressionNode");
        assertTrue(whileNode.getBody() instanceof PrintNode, "The body should be a PrintNode");
    }
}

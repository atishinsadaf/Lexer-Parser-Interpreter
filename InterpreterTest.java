import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InterpreterTest 
{
	private Interpreter interpreter;
    
	private Field integerVariables;
    private Field floatVariables;
    private Method evaluateInt;
    private Method evaluateFloat;
    private Method printMethod;
    private Method handleForNode;


	@Test
    public void testLEFT$() 
	{
        assertEquals("Hel", Interpreter.LEFT$("Hello", 3));
        assertEquals("Hello", Interpreter.LEFT$("Hello", 10)); // More characters than the string length
    }

    @Test
    public void testRIGHT$() 
    {
        assertEquals("llo", Interpreter.RIGHT$("Hello", 3));
        assertEquals("Hello", Interpreter.RIGHT$("Hello", 5)); 
    }

    @Test
    public void testMID$() 
    {
        assertEquals("ell", Interpreter.MID$("Hello", 2, 3));
        assertEquals("lo", Interpreter.MID$("Hello", 4, 5));
    }

    @Test
    public void testNUM$() 
    {
        assertEquals("123", Interpreter.NUM$(123));
        assertEquals("45.67", Interpreter.NUM$(45.67f));
    }

    @Test
    public void testVAL() 
    {
        assertEquals(123, Interpreter.VAL("123"));
        assertEquals(0, Interpreter.VAL("abc")); // Non-numeric string
    }

    @Test
    public void testVALF() 
    {
        assertEquals(123.45f, Interpreter.VALF("123.45"), 0.001);
        assertEquals(0.0f, Interpreter.VALF("abc"), 0.001); // Non-numeric string
    }
    
    
    // Interpreter 2
    
    @BeforeEach
    public void setup() throws Exception 
    {
        interpreter = new Interpreter(null);

        integerVariables = Interpreter.class.getDeclaredField("integerVariables");
        integerVariables.setAccessible(true);  // Make accessible

        floatVariables = Interpreter.class.getDeclaredField("floatVariables");
        floatVariables.setAccessible(true);  // Make accessible

        evaluateInt = Interpreter.class.getDeclaredMethod("evaluateInt", Node.class);
        evaluateInt.setAccessible(true);
        evaluateFloat = Interpreter.class.getDeclaredMethod("evaluateFloat", Node.class);
        evaluateFloat.setAccessible(true);
        
        printMethod = Interpreter.class.getDeclaredMethod("Print", PrintNode.class);
        printMethod.setAccessible(true);
        
        handleForNode = Interpreter.class.getDeclaredMethod("handleForNode", FORNode.class);
        handleForNode.setAccessible(true);
    }
 
    
    @Test
    public void testEvaluateInt() throws Exception 
    {
        IntegerNode node = new IntegerNode(5);
        int result = (int) evaluateInt.invoke(interpreter, node);
        assertEquals(5, result);
    }
    
    @Test
    public void testEvaluateFloat() throws Exception 
    {
        FloatNode node = new FloatNode(3.14f);
        float result = (float) evaluateFloat.invoke(interpreter, node);
        assertEquals(3.14f, result, 0.001);
    }

    @Test
    public void testInterpretMethodWithNullStatement() 
    {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> interpreter.interpret(null));
        assertEquals("Null Statement", exception.getMessage());
    }
    
    
    @Test
    public void testReadNode() 
    {
        ReadNode readNode = new ReadNode(new LinkedList<>());
        assertDoesNotThrow(() -> interpreter.interpret(readNode));
    }
    
    @Test
    public void testAssign() throws Exception 
    {
        @SuppressWarnings("unchecked")
        HashMap<String, Integer> intVars = (HashMap<String, Integer>) integerVariables.get(interpreter);
        intVars.put("x", 10);  // Predefine x

        IntegerNode valueNode = new IntegerNode(20);
        VariableNode variableNode = new VariableNode("x");
        AssignmentNode assignNode = new AssignmentNode(variableNode, valueNode);

        interpreter.interpret(assignNode);

        assertEquals(20, (int) intVars.get("x"));
    }

    
    @Test
    public void testMathOperations() throws Exception 
    {
        Interpreter interpreter = new Interpreter(null);

        MathOpNode addNode = new MathOpNode(MathOpNode.MathOperation.ADD, new IntegerNode(5), new IntegerNode(3)); // Test addition
        int result = (int) evaluateInt.invoke(interpreter, addNode);
        assertEquals(8, result);

        MathOpNode subNode = new MathOpNode(MathOpNode.MathOperation.SUBTRACT, new IntegerNode(10), new IntegerNode(3)); // Test subtraction
        result = (int) evaluateInt.invoke(interpreter, subNode);
        assertEquals(7, result);
  
        MathOpNode mulNode = new MathOpNode(MathOpNode.MathOperation.MULTIPLY, new IntegerNode(5), new IntegerNode(3)); // Test multiplication
        result = (int) evaluateInt.invoke(interpreter, mulNode);
        assertEquals(15, result);
        
        MathOpNode divNode = new MathOpNode(MathOpNode.MathOperation.DIVIDE, new IntegerNode(10), new IntegerNode(2)); // Test division
        result = (int) evaluateInt.invoke(interpreter, divNode);
        assertEquals(5, result);
    }

    @Test
    public void testEvaluateBoolean() throws Exception 
    {
        Interpreter interpreter = new Interpreter(null);

        // Setup a boolean expression node for testing
        IntegerNode leftNode = new IntegerNode(5);
        IntegerNode rightNode = new IntegerNode(10);
        
        Token.TokenType operator = Token.TokenType.LESSTHAN;
        BooleanExpressionNode booleanNode = new BooleanExpressionNode(leftNode, operator, rightNode);

        // Reflectively access the evaluateBoolean method
        Method evaluateBoolean = Interpreter.class.getDeclaredMethod("evaluateBoolean", Node.class);
        evaluateBoolean.setAccessible(true);

        boolean result = (boolean) evaluateBoolean.invoke(interpreter, booleanNode);
        assertTrue(result, "Expected true for 5 < 10");
    }

    @Test
    public void testHandleForNodeLoopExecution() throws Exception 
    {
        FORNode forNode = new FORNode("i", new IntegerNode(1), new IntegerNode(2), new IntegerNode(1));

        HashMap<String, Integer> variables = new HashMap<>();
        integerVariables.set(interpreter, variables);
        
        Field currentStatementField = Interpreter.class.getDeclaredField("currentStatement");
        currentStatementField.setAccessible(true);
        currentStatementField.set(interpreter, forNode);

        handleForNode.invoke(interpreter, forNode);

        
        while ((Integer) currentStatementField.get(interpreter) != null && variables.get("i") < 2) // Manually increment to simulate the loop's progress
        {
            handleForNode.invoke(interpreter, forNode);
        }

        assertEquals(2, (int) variables.get("i"), "Var i should be incremented to 2"); // Check if the loop variable i reaches 2 

        assertNull(currentStatementField.get(interpreter), "Current statement should be null after the loop ends");
    }
    
    @Test
    public void testInterpretWithNullStatement() 
    {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> interpreter.interpret(null));
        assertEquals("Null Statement", exception.getMessage());
    }
}

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Interpreter 
{
    private HashMap<String, Integer> integerVariables = new HashMap<>(); // Integer variables storage
    private HashMap<String, Float> floatVariables = new HashMap<>(); // Float variables storage
    private HashMap<String, String> stringVariables = new HashMap<>(); // String variables storage
    
    private HashMap<String, LabeledStatementNode> labelMap = new HashMap<>();
    
    private Queue<Object> dataQueue = new LinkedList<>(); // Queue for DATA from DATA statements
    private Stack<StatementNode> callStack = new Stack<>(); // Stack for managing flow during GOSUB, RETURN, FOR, and NEXT statements
    
    private boolean loop = true; // Control for the run loop
    private StatementNode currentStatement; // Pointer to the current statement being executed
    
    // Test mode
    private boolean testMode = false;
    private List<String> testInput = new LinkedList<>();
    private List<String> testOutput = new LinkedList<>();
    
    public Interpreter(Node ast) 
    {
        preprocessData(ast);
        preprocessLabels(ast);
    }
    
    public void startTestMode(boolean test)
    {
    	testMode = test;
    }
    
    public void setTestInput(List<String> inputs)
    {
    	testInput = inputs;
    }
    
    public List<String> getTestOutput()
    {
    	return testOutput;
    }

    private void preprocessData(Node node) // Read optimization
    {
        if (node instanceof DataNode) 
        {
            DataNode dataNode = (DataNode) node;
            dataQueue.addAll(dataNode.getDataValues());
        } 
        else if (node instanceof StatementsNode) 
        {
            StatementsNode statementsNode = (StatementsNode) node;
            for (StatementNode statement : statementsNode.getStatements()) 
            {
            	preprocessData(statement); // Recursively process each statement
            }
        }
    }

    private void preprocessLabels(Node node) // Label optimization 
    {
        if (node instanceof LabeledStatementNode) 
        {
            LabeledStatementNode labeledNode = (LabeledStatementNode) node;
            labelMap.put(labeledNode.getLabel(), labeledNode);
        } 
        else if (node instanceof StatementsNode) 
        {
            StatementsNode statementsNode = (StatementsNode) node;
            for (StatementNode statement : statementsNode.getStatements()) 
            {
            	preprocessLabels(statement); // Recursively process each statement
            }
        }
    }
    
    public void buildLinkedList(StatementsNode root) // Link each StatementNode in the AST to its subsequent node 
    {
        List<StatementNode> statements = root.getStatements();
        
        for (int i = 0; i < statements.size() - 1; i++) // Iterate over all statements except last as it does not have a next statement to point to
        {
            statements.get(i).setNext(statements.get(i + 1));
        }
    }

    // BUILT IN BASIC FUNCTIONS
    public static int RANDOM() // Returns a random integer
    {
        return new Random().nextInt();
    }

    public static String LEFT$(String data, int characters) // Returns the leftmost N characters from the string
    {
        return data.substring(0, Math.min(characters, data.length()));
    }

    public static String RIGHT$(String data, int characters) // Returns the rightmost N characters from the string
    {
        return data.substring(data.length() - Math.min(characters, data.length()));
    }

    public static String MID$(String data, int start, int count) // Returns the characters of the string, starting from the 2nd argument and taking the 3rd argument as the coun
    {
        int startIndex = Math.max(start - 1, 0);
        int endIndex = Math.min(start - 1 + count, data.length());
        return data.substring(startIndex, endIndex);
    }

    public static String NUM$(Number number) // Converts a number to a string
    {
        return number.toString();
    }

    public static int VAL(String data) // Converts a string to an integer
    {
        try 
        {
            return Integer.parseInt(data.trim());
        } 
        catch (NumberFormatException e) 
        {
            return 0;
        }
    }

    public static float VALF(String data) // Converts a string to a float
    {
        try 
        {
            return Float.parseFloat(data.trim());
        } 
        catch (NumberFormatException e) 
        {
            return 0.0f;
        }
    }
   
    private int evaluateInt(Node node) 
    {
        if (node instanceof VariableNode) // Get the integer value from variable storage if node is a variable
        {
        	return integerVariables.get(((VariableNode) node).getName());
        } 
        else if (node instanceof IntegerNode) 
        {
        	return ((IntegerNode) node).getNumber();
        } 
        else if (node instanceof MathOpNode) // If node is a math operation, evaulate both operands
        {
            MathOpNode mathOp = (MathOpNode) node;
            int left = evaluateInt(mathOp.getLeft());
            int right = evaluateInt(mathOp.getRight());
            
            switch (mathOp.getOperation()) 
            {
                case ADD: return left + right;
                case SUBTRACT: return left - right;
                case MULTIPLY: return left * right;
                case DIVIDE: return left / right;
                default: throw new IllegalArgumentException("Unsupported operator: " + mathOp.getOperation());
            }
        } 
        else if (node instanceof FunctionInvocationNode) // Handle function calls that will return an integer
        {
            return RANDOM();// example, will return a random integer
        }
        throw new IllegalArgumentException("Unsupported node type for integer");
    }

    private float evaluateFloat(Node node) 
    {
        if (node instanceof VariableNode) // Get float value from variable storage if the node is a variable
        {
        	return floatVariables.get(((VariableNode) node).getName());
        } 
        else if (node instanceof FloatNode) 
        {
        	return ((FloatNode) node).getNumber();
        } 
        else if (node instanceof MathOpNode) // If node is a math operation, evaluate both operands
        {
            MathOpNode mathOp = (MathOpNode) node;
            float left = evaluateFloat(mathOp.getLeft());
            float right = evaluateFloat(mathOp.getRight());
            switch (mathOp.getOperation()) 
            {
                case ADD: return left + right;
                case SUBTRACT: return left - right;
                case MULTIPLY: return left * right;
                case DIVIDE: return left / right;
                default: throw new IllegalArgumentException("Unsupported operator: " + mathOp.getOperation());
            }
        }
        throw new IllegalArgumentException("Node type for float evaluation is not supported");
    }
    
    public void interpret(StatementNode statement) throws Exception
    {
    	if (statement == null) 
    	{
            throw new IllegalArgumentException("Null Statement");
        }
    	currentStatement = statement;
        while (loop && currentStatement != null) 
        {
            try 
            {       
            	if (currentStatement instanceof IFNode) // Check if the current statement is an IF statement
            	{ 
            	    IFNode ifNode = (IFNode) currentStatement;
            	    boolean conditionResult = evaluateBoolean(ifNode.getCondition());
            	    
            	    if (conditionResult) 
            	    {
            	        currentStatement = ifNode.getTrueStatement(); // If the condition is true execute the true statement
            	    } 
            	    else 
            	    {
            	        currentStatement = currentStatement.getNext(); // If the condition is false go to  next statement
            	    }
            	    continue; // Move on to the next iteration
            	}
                else if (currentStatement instanceof GOSUBNode) 
                {
                    GOSUBNode gosubNode = (GOSUBNode) currentStatement;
                    callStack.push(gosubNode.getNext());
                    currentStatement = labelMap.get(gosubNode.getLabel());
                    continue;
                } 
                else if (currentStatement instanceof RETURNNode) 
                {
                    if (callStack.isEmpty()) 
                    {
                        throw new RuntimeException("RETURN without GOSUB");
                    }
                    currentStatement = callStack.pop();
                    continue;
                } 
                else if (currentStatement instanceof FORNode) 
                {
                    handleForNode((FORNode) currentStatement);
                    continue;
                } 
                else if (currentStatement instanceof NEXTNode) 
                {
                    if (callStack.isEmpty()) 
                    {
                        throw new RuntimeException("NEXT without FOR");
                    }
                    currentStatement = callStack.pop();
                    continue;
                } 
                else if (currentStatement instanceof ENDNode) 
                {
                    loop = false;
                    continue;
                }
                
                // Handle basic statement types - Read, Assign, Print, Input
                if (currentStatement instanceof ReadNode) 
                {
                    Read((ReadNode) currentStatement);
                } 
                else if (currentStatement instanceof AssignmentNode) 
                {
                    Assign((AssignmentNode) currentStatement);
                } 
                else if (currentStatement instanceof PrintNode) 
                {
                    Print((PrintNode) currentStatement);
                } 
                else if (currentStatement instanceof InputNode) 
                {
                    Input((InputNode) currentStatement);
                } 
                else 
                {
                    throw new UnsupportedOperationException("Unsupported statement type: " + currentStatement.getClass().getSimpleName());
                }
                
                currentStatement = currentStatement.getNext();// Proceed to the next statement
            } 
            catch (Exception e) 
            {
                System.err.println("Error during interpretation: " + e.getMessage());
                return;
            }
        }
    }

    private void Read(ReadNode readNode) // Read and remove items from the internal queue
    {
        for (VariableNode variable : readNode.getVariables()) 
        {
            Object value = dataQueue.poll();
            if (value == null) 
            {
                throw new NoSuchElementException("Queue is empty");
            }
            if (value instanceof Integer) 
            {
                integerVariables.put(variable.getName(), (Integer) value);
            } 
            else if (value instanceof Float) 
            {
                floatVariables.put(variable.getName(), (Float) value);
            } 
            else 
            {
                throw new IllegalArgumentException("Read value is not an int or float");
            }
        }
    }

    private void Assign(AssignmentNode assignNode) 
    {
        VariableNode variable = assignNode.getVariableNode();
        Node valueNode = assignNode.assignedValue();
       
        if (integerVariables.containsKey(variable.getName())) 
        {
            integerVariables.put(variable.getName(), evaluateInt(valueNode));
        } 
        else if (floatVariables.containsKey(variable.getName())) 
        {
            floatVariables.put(variable.getName(), evaluateFloat(valueNode));
        }
        else 
        {
            throw new IllegalArgumentException("Variable not found");
        }
    }

    private void Print(PrintNode printNode) // Prints to the system output or test output based on the mode
    {
        StringBuilder output = new StringBuilder(); // Accumulate output text
        
        for (Node node : printNode.getNode()) 
        {
            if (node instanceof IntegerNode || (node instanceof VariableNode && integerVariables.containsKey(((VariableNode) node).getName()))) 
            {
                output.append(evaluateInt(node));
            } 
            else if (node instanceof FloatNode || (node instanceof VariableNode && floatVariables.containsKey(((VariableNode) node).getName()))) 
            {
                output.append(evaluateFloat(node));
            } 
            else 
            {
                throw new IllegalArgumentException("Unsupported node type in Print statement");
            }
        }
        output.append("\n"); // Append newline char after all nodes are processed
        
        if (testMode) // Output to the test output list 
        {
            testOutput.add(output.toString());
        } 
        else // Print output to console
        {
            System.out.print(output.toString());
        }
    }

    private void Input(InputNode inputNode) // Reads data and sets the variables based on prompts
    {
        if (testMode) 
        {
            for (VariableNode variable : inputNode.getVariables()) 
            {
                if (!testInput.isEmpty()) 
                {
                    String input = testInput.remove(0);
                    if (integerVariables.containsKey(variable.getName())) 
                    {
                        integerVariables.put(variable.getName(), Integer.parseInt(input));
                    } 
                    else if (floatVariables.containsKey(variable.getName())) 
                    {
                        floatVariables.put(variable.getName(), Float.parseFloat(input));
                    } 
                    else 
                    {
                        throw new IllegalArgumentException("Variable not found for input");
                    }
                } 
                else 
                {
                    throw new NoSuchElementException("Need more test inputs");
                }
            }
        }
        else 
        {
            try (Scanner scanner = new Scanner(System.in)) 
            {
                for (VariableNode variable : inputNode.getVariables()) 
                {
                    System.out.print("Enter an integer value for " + variable.getName() + ": ");
                    if (integerVariables.containsKey(variable.getName())) // Read integer from console and uodate variable
                    {
                        integerVariables.put(variable.getName(), scanner.nextInt());
                    } 
                    else if (floatVariables.containsKey(variable.getName())) // Read float from console and update variable
                    {
                        floatVariables.put(variable.getName(), scanner.nextFloat());
                    } 
                    else 
                    {
                        throw new IllegalArgumentException("Variable not found for input"); // If variable name is not recognized, throw exception
                    }
                }
            }
        }
    }
    
    private boolean evaluateBoolean(Node condition) throws Exception 
    {
        if (condition instanceof BooleanExpressionNode) 
        {
            BooleanExpressionNode booleanNode = (BooleanExpressionNode) condition;
            int leftValue = evaluateInt(booleanNode.getLeftExpression());
            int rightValue = evaluateInt(booleanNode.getRightExpression());
            
            switch (booleanNode.getOperator()) // Compare based on operator of boolean node
            {
                case LESSTHAN:
                    return leftValue < rightValue;
                case LESSTHANOREQUAL:
                    return leftValue <= rightValue;
                case GREATERTHAN:
                    return leftValue > rightValue;
                case GREATERTHANOREQUAL:
                    return leftValue >= rightValue;
                case EQUALS:
                    return leftValue == rightValue;
                case NOTEQUALS:
                    return leftValue != rightValue;
                default:
                    throw new IllegalArgumentException("Invalid boolean operator: " + booleanNode.getOperator());
            }
        } 
        else // If condition is not a BooleanExpressionNode then evaluate it as integer value
        {
            int value = evaluateInt(condition);
            return value != 0; // Return true if int valuue is != 0, false otherwise
        }
    }
    
    private void handleForNode(FORNode forNode) throws Exception 
    {
        String varName = forNode.getVariable();
        int startValue = evaluateInt(forNode.getStartExpression());
        int endValue = evaluateInt(forNode.getEndExpression());
        int stepValue = forNode.getStepExpression() != null ? evaluateInt(forNode.getStepExpression()) : 1;

        if (!integerVariables.containsKey(varName) || currentStatement == forNode) 
        {
            integerVariables.put(varName, startValue);
        }

        int currentValue = integerVariables.get(varName);
        if ((stepValue > 0 && currentValue > endValue) || (stepValue < 0 && currentValue < endValue)) 
        {
            StatementNode node = forNode.getNext();
            while (!(node instanceof NEXTNode)) 
            {
                if (node == null) 
                {
                    throw new IllegalStateException("No NEXT node for FOR loop");
                }
                node = node.getNext();
            }
            currentStatement = node.getNext(); // Move past NEXT node
        } 
        else 
        {
            currentValue += stepValue; // Increment loop variable if not at the first iteration
            integerVariables.put(varName, currentValue); // Update loop variable
            callStack.push(currentStatement); // Push current FOR node for NEXT to jump back to
            currentStatement = currentStatement.getNext(); // Move to the next node in the loop body
        }
    }
}

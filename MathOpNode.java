
public class MathOpNode extends Node
{
	public enum MathOperation
	{
		ADD, SUBTRACT, MULTIPLY, DIVIDE;
	}
	
	private final MathOperation operation;
	private final Node left;
	private final Node right;
	
	public MathOpNode(MathOperation operation, Node left, Node right)
	{
		this.operation = operation;
		this.left = left;
		this.right = right;
	}
	
	public MathOperation getOperation()
	{
		return operation;
	}
	
	public Node getLeft()
	{
		return left;
	}
	
	public Node getRight()
	{
		return right;
	}
	
	@Override
	public String toString() 
	{
	    String symbol = ""; // Initialize the operator symbol as an empty string

	    if (operation == MathOperation.ADD) 
	    {
	        symbol = "+";
	    } 
	    else if (operation == MathOperation.SUBTRACT) 
	    {
	        symbol = "-";
	    } 
	    else if (operation == MathOperation.MULTIPLY) 
	    {
	        symbol = "*";
	    } 
	    else if (operation == MathOperation.DIVIDE) 
	    {
	        symbol = "/";
	    }

	    return left.toString() + " " + symbol + " " + right.toString();
	}


}

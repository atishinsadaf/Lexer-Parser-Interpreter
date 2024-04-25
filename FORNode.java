
public class FORNode extends StatementNode
{
	private String variable;
	private Node start;
	private Node end;
	private Node step; // Increment or decrement
	
	public FORNode(String variable, Node start, Node end, Node step)
	{
		this.variable = variable;
		this.start = start;
		this.end = end;
		this.step = step;
	}
	
	public String getVariable()
	{
		return variable;
	}
	
	public Node getStartExpression()
	{
		return start;
	}
	public Node getEndExpression()
	{
		return end;
	}
	public Node getStepExpression()
	{
		return step;
	}
	
	@Override
	public String toString()
	{
		return String.format("FOR %s = %s TO %s STEP %s", variable, start, end, step); // A string in the format "FOR variable = start TO end STEP step"
	}

}


public class AssignmentNode extends StatementNode
{
	private VariableNode variableNode;
	private Node assignedValue;
	
	public AssignmentNode(VariableNode variableNode, Node assignedValue)
	{
		this.variableNode = variableNode;
		this.assignedValue = assignedValue;
	}
	
	public VariableNode getVariableNode()
	{
		return variableNode;
	}
	
	public Node assignedValue()
	{
		return assignedValue;
	}
	
	@Override
	public String toString()
	{
		return variableNode.toString() + assignedValue.toString();
	}
}

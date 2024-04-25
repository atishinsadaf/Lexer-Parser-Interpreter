import java.util.LinkedList;

public class ReadNode extends StatementNode 
{
	private LinkedList<VariableNode> variables; // Holds StringNode, IntegerNode, and FloatNode
	
	public ReadNode(LinkedList<VariableNode> variables)
	{
		this.variables = variables;
	}
	
	public LinkedList<VariableNode> getVariables()
	{
		return variables;
	}
	
	@Override
	public String toString()
	{
		String result = "READ ";
		
		for (VariableNode variable : variables)
		{
			result = result + variable.toString() + ", ";
		}
		
		if (result.endsWith(", "))
		{
			result = result.substring(0, result.length() - 2); // Removes the last comma
		}
		
		return result; 
	}
	
}

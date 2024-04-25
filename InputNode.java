import java.util.LinkedList;

public class InputNode extends StatementNode
{
	private Node parameter; // Can be a StringNode or VariableNode
	private LinkedList<VariableNode> variables;
	
	public InputNode(Node parameter, LinkedList<VariableNode> variables)
	{
		this.parameter = parameter;
		this.variables = variables;
	}
	
	public Node getParameter()
	{
		return parameter;
	}
	
	public LinkedList<VariableNode> getVariables()
	{
		return variables;
	}
	
	@Override
	public String toString()
	{
		String constString = "";
	    if (parameter != null) 
	    {
	        constString = parameter.toString() + " ";
	    }
	    
	    String variableString = "";
	    for (int i = 0; i < variables.size(); i++) 
	    {
	    	variableString += variables.get(i).toString();
	        if (i < variables.size() - 1) 
	        { 
	        	variableString += ", "; // Avoids adding a comma after the last variable
	        }
	    }
	    
	    return "INPUT " + constString + variableString;
	}

}

import java.util.LinkedList;

public class StatementsNode extends Node 
{
	private LinkedList <StatementNode> statements;
	
	public StatementsNode()
	{
		this.statements = new LinkedList<>();
	}
	
	public LinkedList<StatementNode> getStatements()
	{
		return statements;
	}
	
	public void addStatement(StatementNode statement)
	{
		statements.add(statement);
	}
	
	@Override
	public String toString() 
	{
	    StringBuilder sb = new StringBuilder();
	    for (StatementNode statement : statements) // Iterate over each statement in the statements list
	    { 
	        sb.append(statement.toString()); // Append the string representation of the statement
	        sb.append("\n"); // Newline character after each statement
	    }
	    return sb.toString();
	}


}

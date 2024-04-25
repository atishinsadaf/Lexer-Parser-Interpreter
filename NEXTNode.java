
public class NEXTNode extends StatementNode
{
	private String variable;
	
	public NEXTNode(String variable)
	{
		this.variable = variable;
	}
	
	public String getVariable()
	{
		return variable;
	}
	
	@Override
	public String toString()
	{
		return "NEXT" + variable;
	}

}

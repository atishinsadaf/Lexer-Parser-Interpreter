
public class VariableNode extends Node 
{
	private String name;
	
	public VariableNode(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

}

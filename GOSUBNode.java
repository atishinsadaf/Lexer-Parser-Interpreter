
public class GOSUBNode extends StatementNode
{
	private String label;
	
	public GOSUBNode(String label)
	{
		this.label = label;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	@Override
	public String toString()
	{
		return "GOSUB" + label;
	}

}

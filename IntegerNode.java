
public class IntegerNode extends Node
{
	private int number;
	
	public IntegerNode (int number)
	{
		this.number = number;
	}
	
	public int getNumber() 
	{
		return number;
	}
	
	@Override
	public String toString()
	{
		return Integer.toString(number);
	}
}

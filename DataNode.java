import java.util.LinkedList;

public class DataNode extends StatementNode 
{
	private LinkedList<Node> dataValues;
	
	public DataNode(LinkedList<Node> dataValues)
	{
		this.dataValues = dataValues;
	}
	
	public LinkedList<Node> getDataValues()
	{
		return dataValues;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder("DATA ");
        for (Node values : dataValues) 
        {
            builder.append(values.toString()).append(", ");
        }
        if (builder.lastIndexOf(", ") > -1) 
        {
            builder.delete(builder.lastIndexOf(", "), builder.length()); // Remove the last comma
        }
        return builder.toString(); 
    }
	
}

import java.util.LinkedList;

public class PrintNode extends StatementNode 
{
    private LinkedList<Node> nodes;  // List to hold multiple nodes

    public PrintNode(LinkedList<Node> nodes) // Constructor 
    {
        this.nodes = nodes;
    }

    public LinkedList<Node> getNodes() 
    {
        return nodes;
    }

    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("print ");
        boolean isFirst = true;  // Initialize a boolean flag to check if it's the first node in the loop
        
        for (Node node : nodes) // Iterate over each node in the nodes list
        {  
            if (!isFirst) 
            {
                sb.append(", ");  // If it's not the first node, prepend a comma and space.
            }
            else 
            {
                isFirst = false;  // After the first iteration set isFirst to false
            }
            sb.append(node.toString());  // Append the string representation of the node
        }
        return sb.toString();  // Convert the StringBuilder to a String and return it
    }
}

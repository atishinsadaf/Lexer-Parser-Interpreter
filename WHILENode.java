
public class WHILENode extends StatementNode 
{
    private Node condition; // The boolean condition to evaluate
    private StatementNode body; // The body of the loop

    public WHILENode(Node condition, StatementNode body) 
    {
        this.condition = condition;
        this.body = body;
    }

    public Node getCondition() 
    {
        return condition;
    }

    public StatementNode getBody() 
    {
        return body;
    }

    @Override
    public String toString() 
    {
        return "WHILE" + condition + " DO " + body;
    }
}


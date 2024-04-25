
public class IFNode extends StatementNode 
{
    private Node condition;
    private StatementNode trueStatement;

    public IFNode(Node condition, StatementNode trueStatement) 
    {
        this.condition = condition;
        this.trueStatement = trueStatement;
    }

    public Node getCondition() 
    {
        return condition;
    }

    public StatementNode getTrueStatement() 
    {
        return trueStatement;
    }

    @Override
    public String toString() 
    {
        return "IF" + condition + " THEN " + trueStatement;
    }
}


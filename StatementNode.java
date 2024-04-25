
public abstract class StatementNode extends Node
{
	protected StatementNode next;

    public StatementNode getNext() 
    {
        return next;
    }

    public void setNext(StatementNode next) 
    {
        this.next = next;
    }

}

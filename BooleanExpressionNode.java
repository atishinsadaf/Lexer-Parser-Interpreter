
public class BooleanExpressionNode extends Node 
{
    private Node left; // Left side of the boolean expression
    private Token.TokenType operator; // Boolean operators such as <, >, ==, !=
    private Node right; // Right side of the boolean expression

    public BooleanExpressionNode(Node left, Token.TokenType operator, Node right) 
    {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Node getLeftExpression() 
    {
        return left;
    }

    public Token.TokenType getOperator() 
    {
        return operator;
    }

    public Node getRightExpression() 
    {
        return right;
    }

    @Override
    public String toString() 
    {
        return left + " " + operator + " " + right;
    }
}


import java.util.List;

public class FunctionInvocationNode extends Node 
{
    private String functionName; // Name of function being called
    private List<Node> arguments; // List of arguments passed to function

    public FunctionInvocationNode(String functionName, List<Node> arguments) 
    {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public String getFunctionName() 
    {
        return functionName;
    }

    public List<Node> getArguments() 
    {
        return arguments;
    }

    @Override
    public String toString() 
    {
        String result = functionName + "(";
        for (int i = 0; i < arguments.size(); i++) // Loop through eACh arguments in the arguments list
        {
            result = result + arguments.get(i).toString();
            if (i < arguments.size() - 1) // If it isnt the last argument, add a comma to seperate the arguments
            {
                result += ", ";
            }
        }
        result += ")";
        return result;
    }
}


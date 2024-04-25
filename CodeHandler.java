import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class CodeHandler 
{
	private String document;
	private int index;
	
	private CodeHandler(String file)
	{
		try 
		{
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            this.document = new String(bytes);
            this.index = 0;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	char Peek(int i) // Looks "i" characters ahead, returns that character. Leave index alone
	{
		if (index + i < document.length())
		{
			return document.charAt(index + i);
		}
		else
		{
			return '\0'; // Null
		}
	}
	
	String PeekString(int i) // Peeks "i" characters ahead, returns the string, does not move index
	{
		return document.substring(index, index + i);
	}
	
	char GetChar() // Returns the next/"peeked" character and increments index
	{
		char character = Peek(0);
		index++;
		return character;
	}
	
	void Swallow(int i) // Moves index "i" positions
	{
		index = index + i;
	}
	
	boolean isDone() // Checks if index is >= to the document, returns true if we are at the end of the document
	{
		return	index >= document.length();
	}
	
	String Remainder() // Returns the rest of the document as a string
	{
		return document.substring(index);
	}
	
	static CodeHandler create(String file) // Returns a new instance of CodeHandler with initialized file content
	{
		return new CodeHandler(file);
	}
}

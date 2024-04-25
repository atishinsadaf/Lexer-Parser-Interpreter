import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CodeHandlerTest 
{
    @Test
    public void PeekFunction() 
    {
        CodeHandler codeHandler = CodeHandler.create("C:\\Users\\axsad\\eclipse-workspace\\CSI311 Lexer\\testfile1.txt");
        assertEquals('c', codeHandler.Peek(0));
        assertEquals('s', codeHandler.Peek(1));
        assertEquals('i', codeHandler.Peek(2));
        assertEquals('3', codeHandler.Peek(3));
        assertEquals('1', codeHandler.Peek(4));
        assertEquals('1', codeHandler.Peek(5));    
    }
    
    @Test
    public void GetCharFunction() 
    {
        CodeHandler codeHandler = CodeHandler.create("C:\\Users\\axsad\\eclipse-workspace\\CSI311 Lexer\\testfile2.txt");
        assertEquals('c', codeHandler.GetChar());
        assertEquals('s', codeHandler.GetChar());
        assertEquals('i', codeHandler.GetChar());
        assertEquals('3', codeHandler.GetChar());
        assertEquals('1', codeHandler.GetChar());
        assertEquals('1', codeHandler.GetChar());
        
        assertTrue(codeHandler.isDone());
    }
}

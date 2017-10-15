package tiger.scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import tiger.errors.*;
import tiger.ast.*;

public class ParserTest {
    private static void test(String filename) {
    	ErrorList e = new ErrorList(filename);
    	java_cup.runtime.Symbol out=null;
        try{
        	InputStream inp = new FileInputStream(filename);
        	
        	Lexer the_lexer = new Lexer(inp,e);
        	
            Parser the_parser = new Parser(the_lexer,e);
            out=the_parser.parse();
        }
        catch (IOException ex) {
            System.err.println("IO exception");
        }
        catch (Exception ex){
        	System.err.println("error");
        }	/* eat it */
        
        if(!e.no_error())
        	e.print_error();
        else{
        	Expr result = (Expr)(out.value);
        	Print p = new Print(System.out);
        	p.prExp(result, 0);
        }
    }

    public static void main(String[] argv) {
        for(String i: argv){
        	System.err.println("File: "+i);
        	test(i);
        }	
        //test only
        test("simple_test/test.tig");
    }

}

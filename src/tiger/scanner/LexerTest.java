package tiger.scanner;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import tiger.errors.*;

public class LexerTest {
    private static void test(String filename) {
        try{
        	InputStream inp = new FileInputStream(filename);
        	ErrorList e = new ErrorList(filename);
        	Lexer tmp = new Lexer(inp,e);
        	java_cup.runtime.Symbol x=tmp.next_token();
            while(x.sym != Sym.EOF){
            	System.out.printf("token: %-3d %-10s %-8s",x.sym,SymName.getName(x.sym),((TokenInfo)x.value).info+"");
            	System.out.println("\t@"+x.left+","+x.right);
            	x=tmp.next_token();
            }
            e.print_error();
        }
        catch (IOException e) {
            System.err.println("IO exception");
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

class SymName{
	static Map<Integer,String> mapping = new Hashtable<Integer,String>();
	static{
		try{
		for(Field y : Sym.class.getFields()){
			mapping.put((Integer)y.get(new Sym()), y.getName());
		}}
		catch(Exception e){}	/* swallow it */
	}
	static String getName(int n){
		return mapping.get(n);
	}
}
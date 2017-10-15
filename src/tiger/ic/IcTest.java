package tiger.ic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import tiger.ast.*;
import tiger.errors.ErrorList;
import tiger.scanner.Parser;
import tiger.symbol.*;
import tiger.typing.*;

public class IcTest {
	private static void test(String filename) {
    	ErrorList e = new ErrorList(filename);
    	java_cup.runtime.Symbol out=null;
        try{
        	InputStream inp = new FileInputStream(filename);
            Parser the_parser = new Parser(inp,e);
            out=the_parser.parse();
        }
        catch (IOException ex) {
            System.err.println("IO exception");
        }
        catch (Exception ex){
        	System.err.println("error");
        }	/* eat it */
        
        if(e.no_error()){
        	//parse
        	Expr result = (Expr)(out.value);
        	//Print p = new Print(System.out);
        	//p.prExp(result, 0);
        	//System.out.println();
        	//type checking
        	Type tmp = result.type_check(Table.init_t_env, Table.init_v_env, false, e);
        	if(e.no_error()){
        		System.out.println("Type is:"+tmp.actual().toString());
        		//ic
        		IcFun.ic_start_main(result);
        		IcFun.main_fun.print(0);
        	}
        }
        e.print_error();
    }

    public static void main(String[] argv) {
        /* only one file one program --- beacause ic use global(static) var */
        //test only
        test("simple_test/test.tig");
    	if(argv.length>0)
    		test(argv[0]);
    }

}


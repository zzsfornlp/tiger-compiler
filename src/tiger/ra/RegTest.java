package tiger.ra;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import tiger.ast.*;
import tiger.errors.ErrorList;
import tiger.scanner.Parser;
import tiger.symbol.*;
import tiger.typing.*;
import tiger.ic.*;
import tiger.block.*;

public class RegTest {
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
        	Type tmp = result.type_check(Table.init_t_env, Table.init_v_env, false, e);
        	if(e.no_error()){
        		System.out.println("Type is:"+tmp.actual().toString());
        		//ic
        		IcFun.ic_start_main(result);
        		//block
        		Block_utils.block_start(IcFun.main_fun);
        		//reg
        		Reg_utils.reg_start(IcFun.main_fun, 8);
        		IcFun.main_fun.print_reg(0);
        	}
        }
        e.print_error();
    }

    public static void main(String[] argv) {
        /* only one file one program --- beacause ic use global(static) var */
        //test only
    	test("simple_test/test.tig");
    	//test(argv[0]);
    }

}


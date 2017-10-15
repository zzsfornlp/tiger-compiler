package tiger.block;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import tiger.ast.*;
import tiger.errors.ErrorList;
import tiger.scanner.Parser;
import tiger.symbol.*;
import tiger.typing.*;
import tiger.ic.*;

public class BlockTest {
	private static void test(String filename,int stage) {
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
        		//IcFun.main_fun.print(0);
        		if(stage >= 1){//combine labels
        			System.out.println("-------------------------------------------");
        			Block_step1.combine_flags(IcFun.main_fun);
        			//IcFun.main_fun.print(0);
        		}
        		if(stage >= 3){//basic blocks && liveness analysis
        			System.out.println("-------------------------------------------");
        			Block_step2.get_blocks(IcFun.main_fun);
        			Block_step3.analyze_liveness(IcFun.main_fun);
        			//IcFun.main_fun.print_blocks(0);
        		}
        		if(stage >= 4){//next ref info
        			System.out.println("-------------------------------------------");
        			Block_step4.solve_nextref(IcFun.main_fun);
        			IcFun.main_fun.print_blocks(0);
        		}
        	}
        }
        e.print_error();
    }

    public static void main(String[] argv) {
        /* only one file one program --- beacause ic use global(static) var */
        //test only
        test("simple_test/test.tig",4);
    	//test(argv[0],4);
    }

}


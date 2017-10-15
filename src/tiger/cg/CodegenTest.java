package tiger.cg;

import java.io.*;

import tiger.ast.*;
import tiger.errors.ErrorList;
import tiger.scanner.Parser;
import tiger.symbol.*;
import tiger.typing.*;
import tiger.ic.*;
import tiger.block.*;
import tiger.ra.*;
import tiger.builtins.*;

public class CodegenTest {
	private static void test(String filename)throws Exception {
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
        		//System.out.println("Type is:"+tmp.actual().toString());
        		//ic
        		IcFun.ic_start_main(result);
        		//block
        		Block_utils.block_start(IcFun.main_fun);
        		//reg
        		Reg_utils.reg_start(IcFun.main_fun, 16);
        		//IcFun.main_fun.print_reg(0);
        		
        		//code
        		Codegen.gen_code();
        		
        		//File testfile = new File("/home/zzs/test/tigertest.s");
        		//FileWriter fw = new FileWriter(testfile);
        		//fw.write(Builtins.codes+IcFun.string_center+Codegen.get_code_str(IcFun.main_fun));
        		//fw.close();
        		System.out.print(Builtins.codes+IcFun.string_center+Codegen.get_code_str(IcFun.main_fun));
        		
        	}
        }
        e.print_error();
    }

    public static void main(String[] argv) throws Exception{
        /* only one file one program --- beacause ic use global(static) var */
        //test only
    	test("simple_test/test.tig");
    	//test(argv[0]);
    }
}


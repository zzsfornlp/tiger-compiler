package tiger.compiler;

import java.io.*;

import tiger.ast.Expr;
import tiger.block.Block_utils;
import tiger.builtins.Builtins;
import tiger.cg.Codegen;
import tiger.errors.ErrorList;
import tiger.ic.IcFun;
import tiger.ra.Reg_utils;
import tiger.scanner.Parser;
import tiger.symbol.Table;
import tiger.typing.Type;

public class Compiler {
	//the central controll
	public static boolean compiler_cg_verbose=true;
	//options
	static String filename_in;
	static String filename_out;
	static InputStream instream;
	//infos
	static String version1 = "Tiger ZCompiler v1.0";
	static String help1 = version1 + "\n"
			+ "Cmdline options:\n" + "\t-h,--help:\tPrint this help.\n"
			+ "\t-v,--version:\tPrint version.\n" + "\t-b,--brief:\tGenerate code without comments.\n"
			+ "\t-o filename:\tOutput filename(default is <origin_name>.s).\n";
	static String header1 = "# By Tiger ZCompiler v1.0 #\n\n";
	
	//compile
	private static String compile(){
    	ErrorList e = new ErrorList(filename_in);
    	java_cup.runtime.Symbol out=null;
        try{
            Parser the_parser = new Parser(instream,e);
            out=the_parser.parse();
        }
        catch (Exception ex){
        	//System.err.println("error");
        }	/* eat it */
        if(e.no_error()){
        	//parse
        	Expr result = (Expr)(out.value);
        	Type tmp = result.type_check(Table.init_t_env, Table.init_v_env, false, e);
        	if(e.no_error()){
        		//ic
        		IcFun.ic_start_main(result);
        		//block
        		Block_utils.block_start(IcFun.main_fun);
        		//reg
        		Reg_utils.reg_start(IcFun.main_fun, tiger.cg.Codegen_constant.MIPS_ALL_REGS);
        		//code
        		Codegen.gen_code();
        		
        		return (header1+Builtins.codes+IcFun.string_center+Codegen.get_code_str(IcFun.main_fun));
        	}
        }
        e.print_error();
        System.exit(1);
        return "";
    }

    public static void main(String[] argv){
        /* only one file one program --- beacause ic use global(static) var */
    	//deal with command line
    	int i=0;
    	while(i<argv.length){
    		if(argv[i].equals("-b") || argv[i].equals("--brief")){//brief
    			compiler_cg_verbose = false;
    			i++;
    		}
    		else if(argv[i].equals("-v") || argv[i].equals("--version")){//version
    			System.out.println(version1);
    			System.exit(0);
    		}
    		else if(argv[i].equals("-h") || argv[i].equals("--help")){//version
    			System.out.println(help1);
    			System.exit(0);
    		}
    		else if(argv[i].equals("-o")){//output file
    			i++;
    			if(i>=argv.length){
    				System.err.println("Error: no output file.");
        			System.exit(1);
    			}
    			else{
    				filename_out = argv[i++];
    			}
    		}
    		else{
    			if(filename_in == null)
    				filename_in = argv[i++];
    		}
    	}
    	//no input file
    	if(filename_in == null){
    		System.err.println("Error: no input file.");
			System.exit(1);
    	}
    	//get file input
    	File infile = new File(filename_in); 
    	try{
    		instream = new FileInputStream(infile);
    	}
    	catch (IOException ex){
    		System.err.println("Error: input file error.");
    		System.exit(1);
    	}
    	//specify filename_out
    	if(filename_out == null){
    		filename_out = infile.getName()+".s";
    	}
    	//compile
    	String code = compile();
    	//write out
    	try{
    		File testfile = new File(filename_out);
    		FileWriter fw = new FileWriter(testfile);
    		fw.write(code);
    		fw.close();
    	}catch (IOException ex){
    		System.err.println("Error: output file error.");
    		System.exit(1);
    	}
    }
}

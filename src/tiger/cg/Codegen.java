package tiger.cg;

import java.util.*;

import tiger.ic.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.Mips_addi;
import static tiger.compiler.Compiler.compiler_cg_verbose;

public class Codegen {
	/* final step : code generation */
	public static void gen_code(){
		/* step1 : rename functions to avoid name conflict */
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		Cg_check_function(IcFun.main_fun, map);
		
		/* step2 : libraries */
		//...
		
		/* step3 : constant string center */
		StringBuilder str_constant = new StringBuilder();
		if(compiler_cg_verbose)
			str_constant.append("# The String Constant #\n");
		str_constant.append(".data\n");
		//	_Sn: .asciiz "..."
		for(int i=0;i<IcFun.const_strings.size();i++)
			str_constant.append(Codegen_constant.str_prefix+i+": .asciiz "+"\""+Cg_transform_str(IcFun.const_strings.get(i))+"\"\n");
		str_constant.append("\n");
		IcFun.string_center = str_constant.toString();
		
		/* step4 : recursively generate code */
		Codegen_utils_v2.Cg_gen_mips(IcFun.main_fun);
	}
	
	public static String get_code_str(IcFun f){
		StringBuilder s = new StringBuilder();
		for(IcFun ff : f.funs){
			s = s.append(get_code_str(ff));
		}
		s.append(f.function_code);
		for(IcStmt i : f.stmts)
			s.append(i.stmt_code);
		s.append("\n");
		return s.toString();
	}
}

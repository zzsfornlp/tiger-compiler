package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;

public class IcStmt_label extends IcStmt{
	static int so_far=1;
	
	public int lnumber;
	public String l;
	
	public static IcStmt_label new_one(IcFun f,int n){
		IcStmt_label tmp = new IcStmt_label(f,n);
		tmp.lnumber = so_far++;
		tmp.l = "_L"+tmp.lnumber;
		return tmp;
	}
	public IcStmt_label(IcFun f,int n){
		super(f,n);
	}
	
	public String toString(){
		return num+":"+l+":";
	}
	
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+this.toString()+"\n");

		s.append(l+":\n");

		stmt_code = s.toString();
	}
}

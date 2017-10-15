package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;

public class IcStmt_assign extends IcStmt{
	public IcVar assigned;
	public IcOper right;
	public IcStmt_assign(IcFun f,int n,IcVar a,IcOper le){
		super(f,n);
		assigned = a;
		right = le;
	}
	
	public String toString(){
		return num+":"+assigned+" = "+right;
	}
	
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+this.toString()+"\n");
		//calculate addr
		String x = Cg_solve_oper(assigned,s,1,fun);
		String y = Cg_solve_oper(right,s,2,fun);
		s.append(Mips_move(x,y));
		//option wb
		Cg_write_var(assigned,x,s,fun);
		
		stmt_code = s.toString();
	}
}

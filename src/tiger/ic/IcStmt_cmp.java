package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;

public class IcStmt_cmp extends IcStmt{
	public static final int EQ=4, NE=5, LT=6, LE=7, GT=8, GE=9;
		    
	public static final String[] op_name = new String[]{"=","<>","<","<=",">",">="};
	
	public IcVar assigned;
	public IcOper left;
	public IcOper right;
	public int operation;
	public IcStmt_cmp(IcFun f,int n,IcVar a,IcOper le,IcOper ri,int op){
		super(f,n);
		assigned = a;
		left = le;
		right = ri;
		operation = op;
	}
	
	public String toString(){
		return num+":"+assigned+" = "+"("+left+" "+op_name[operation-4]+" "+right+")";
	}
	
	/* cmp code */
	public static final String[] op_code_name = new String[]{"seq","sne","slt","sle","sgt","sge"};
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+this.toString()+"\n");
		//calculate addr
		String x = Cg_solve_oper(left,s,1,fun);
		String y = Cg_solve_oper(right,s,2,fun);
		String a = Cg_solve_oper(assigned,s,1,fun);
		s.append(Mips_cmp(op_code_name[operation-4],a,x,y));
		//option wb
		Cg_write_var(assigned,a,s,fun);
		
		stmt_code = s.toString();
	}
}
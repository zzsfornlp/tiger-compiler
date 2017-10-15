package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;

public class IcStmt_record2 extends IcStmt{
	/* y = x.i; */
	public IcOper rec;
	public IcConst_int index;	//the num of the field
	public IcVar assigned;
	public IcStmt_record2(IcFun f,int n,IcOper a,IcConst_int i,IcVar v){
		super(f,n);
		rec = a;
		index = i;
		assigned = v;
	}
	
	public String toString(){
		return num+":"+""+assigned+" = "+rec+"."+index;
	}
	
	/* code generation */
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+this.toString()+"\n");
		//calculate addr
		String x = Cg_solve_oper(rec,s,1,fun);
		String y = Cg_solve_oper(assigned,s,2,fun);
		s.append(Mips_lw(y,x,index.num*4));

		//option wb
		Cg_write_var(assigned,y,s,fun);
		
		stmt_code = s.toString();
	}
}

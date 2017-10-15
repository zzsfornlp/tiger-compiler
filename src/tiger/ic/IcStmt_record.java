package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;

public class IcStmt_record extends IcStmt{
	/* x.i = y; */
	public IcOper rec;
	public IcConst_int index;	//the num of the field
	public IcOper value;
	public IcStmt_record(IcFun f,int n,IcOper a,IcConst_int i,IcOper v){
		super(f,n);
		rec = a;
		index = i;
		value = v;
	}
	
	public String toString(){
		return num+":"+""+rec+"."+index+" = "+value;
	}
	
	/* code generation */
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+this.toString()+"\n");
		//calculate addr
		String x = Cg_solve_oper(rec,s,1,fun);
		String y = Cg_solve_oper(value,s,2,fun);
		s.append(Mips_sw(y,x,index.num*4));

		stmt_code = s.toString();
	}
}

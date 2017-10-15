package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;

public class IcStmt_array extends IcStmt{
	/* x[i] = y; */
	public IcOper arr;
	public IcOper index;
	public IcOper value;
	public IcStmt_array(IcFun f,int n,IcOper a,IcOper i,IcOper v){
		super(f,n);
		arr = a;
		index = i;
		value = v;
	}
	
	//just print basic info
	public String toString(){
		return num+":"+""+arr+"["+index+"] = "+value;
	}
	
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+this.toString()+"\n");
		//arr must be IcVar
		if(!(arr instanceof IcVar))
			throw new CgError("Fatal fault: array not variable " + arr);
		String a = Cg_solve_oper((IcVar)arr,s,1,fun);
		String i = Cg_solve_oper(index,s,2,fun);
		s.append(Mips_op("mul",MIPS_T2,i,"4"));
		s.append(Mips_op("add",MIPS_T1,MIPS_T2,a));
		
		String y = Cg_solve_oper(value,s,2,fun);
		s.append(Mips_sw(y,MIPS_T1,0));
		stmt_code = s.toString();
	}
}

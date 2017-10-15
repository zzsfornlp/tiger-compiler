package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;

public class IcStmt_new extends IcStmt{
	public IcOper size;
	public IcVar result;
	public IcStmt_new(IcFun f,int n,IcOper i,IcVar v){
		super(f,n);
		size=i;
		result=v;
	}

	public String toString(){
		return num+":"+"new: size of "+size+" to "+result;
	}
	
	/* code generation */
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+this.toString()+"\n");
		//calculate addr
		String x = Cg_solve_oper(size,s,1,fun);
		s.append(Mips_move("$a0",x));
		s.append(Mips_op("mul","$a0","$a0","4"));
		s.append(Mips_li("$v0",9));
		s.append("syscall"+"\n");
		String y = Cg_solve_oper(result,s,1,fun);
		s.append(Mips_move(y,"$v0"));
		//option wb
		Cg_write_var(result,y,s,fun);
		
		stmt_code = s.toString();
	}
}

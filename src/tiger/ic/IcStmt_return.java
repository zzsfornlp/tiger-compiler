package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;

public class IcStmt_return extends IcStmt{
	public IcOper return_v;
	public IcStmt_return(IcFun f,int n,IcOper v){
		super(f,n);
		return_v = v;
	}
	
	public String toString(){
		return num+":"+"return "+return_v;
	}
	
	/* code generation */
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+this.toString()+"\n");
		
		/* 1.return address */
		if(return_v==null)
			s.append(Mips_move("$v0","$zero"));
		else{
			String x = Cg_solve_oper(return_v,s,1,fun);
			s.append(Mips_move("$v0",x));
		}
		/* 2.restore callee-save-regs */
		int count=0;
		for(int i=0;i<MIPS_CALLE_SAVE_REG;i++){
			if(fun.reg_used[i]==true){
				s.append(Mips_lw(MIPS_REGS[i],"$fp",-4*(count+1)));
				count++;
			}
		}
		/* 3.the frame */
		s.append(Mips_addi("$sp","$fp",-4));
		s.append(Mips_jr("$ra"));
		
		stmt_code = s.toString();
	}
}

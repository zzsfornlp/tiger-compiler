package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;

public class IcStmt_goto extends IcStmt{
	public IcStmt_label label;
	public IcStmt_goto(IcFun f,int n,IcStmt_label la){
		super(f,n);
		label = la;
	}

	public String toString(){
		return num+":"+"goto "+label.l;
	}
	
	//for combining labels in block_step1
	public void replace_label(IcStmt_label origin,IcStmt_label target){
		if(label==origin)
			label = target;
	}
	
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+this.toString()+"\n");

		s.append(Mips_jump(label.l));

		stmt_code = s.toString();
	}
}

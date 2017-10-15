package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;

public class IcStmt_branch extends IcStmt{
	/* if x then goto L */
	public static final int EQ=4, NE=5, LT=6, LE=7, GT=8, GE=9;
	public static final String[] op_name = new String[]{"=","<>","<","<=",">",">="};
	
	public IcOper pred1;
	public IcOper pred2;
	public IcStmt_label label;
	public int operation;
	public IcStmt_branch(IcFun f,int n,IcOper p1,IcOper p2,IcStmt_label la,int op){
		super(f,n);
		pred1 = p1;
		pred2 = p2;
		label = la;
		operation = op;
	}

	public String toString(){
		return num+":"+"brach to "+label.l+" if "+"("+pred1+" "+op_name[operation-4]+" "+pred2+")";
	}
	
	//for combining labels in block_step1
	public void replace_label(IcStmt_label origin,IcStmt_label target){
		if(label==origin)
			label = target;
	}
	
	/* branch code */
	public static final String[] op_code_name = new String[]{"beq","bne","blt","ble","bgt","bge"};
	
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+this.toString()+"\n");
		//calculate addr
		String x = Cg_solve_oper(pred1,s,1,fun);
		String y = Cg_solve_oper(pred2,s,2,fun);
		s.append(Mips_branch(op_code_name[operation-4],x,y,label.l));
		stmt_code = s.toString();
	}
}

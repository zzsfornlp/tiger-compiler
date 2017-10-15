package tiger.ic;

import static tiger.ic.IcFun.print_head;
import tiger.block.*;

import java.util.*;

abstract public class IcStmt {
	public IcFun fun;
	public int num;
	public IcStmt(IcFun f,int n){
		fun = f;
		num = n;
	}
	
	//print-funstions
	public void print_it(int n){
		print_head(n);
		IcFun.print_out(this.toString());
	}
	public void print_basic(int n){
		print_it(n);
		IcFun.print_out("\n");
	}
	public void print_nextref(int n){
		print_it(n);
		IcFun.print_out("  next_ref:"+next_ref_l.toString());
		IcFun.print_out("\n");
	}
	public void print_reg(int n){
		print_it(n);
		IcFun.print_out(" | regs-alloc:");
		for(IcVar i : def_var)
			IcFun.print_out("<"+i+":$"+this.fun.all_vars_reg.get(i)+">");
		for(IcVar i : use_var)
			IcFun.print_out("<"+i+":$"+this.fun.all_vars_reg.get(i)+">");
		IcFun.print_out("\n");
	}
	
	//block info
	/* const */
	public static final int OUTSIDE=0,NOPE=-1;
	/* step 2 : blocking */
	public Block block_belonged;
	/* step3 : liveness --- figure out the IcVars */
	public ArrayList<IcVar> def_var = new ArrayList<IcVar>();
	public ArrayList<IcVar> use_var = new ArrayList<IcVar>();
	/* step 4 : next ref 
	 * the first one must be the one being assigned if there is one
	 */
	public LinkedHashMap<IcVar,Integer> next_ref_l = new LinkedHashMap<IcVar,Integer>();
	
	//register allocation
	public HashSet<IcVar> live_vars = new HashSet<IcVar>();
	
	//code gen
	public String stmt_code;
	abstract public void gen_mips();
	
	@Deprecated
	public void gen_code(){}
}

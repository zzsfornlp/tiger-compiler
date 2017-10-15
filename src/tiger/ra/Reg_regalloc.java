package tiger.ra;

import java.util.*;

import tiger.block.Block;
import tiger.ic.*;

public class Reg_regalloc {
	static void alloc_reg(IcFun f,int reg_n){
		/*
		 * in fact, the allocation is somewhat spwcial,
		 * because IcVar_param and IcVar_local always have copy in the memory.
		 */
		/* 0.recursive*/
		for(IcFun ff : f.funs)
			alloc_reg(ff,reg_n);
		/* 1.interference coloring */
		//all vars
		List<IcVar> vars = new ArrayList<IcVar>();
		vars.addAll(f.all_vars);
		HashMap<IcVar,Integer> ordering = new HashMap<IcVar,Integer>();
		for(int i=0;i<vars.size();i++)
			ordering.put(vars.get(i), i);
		//construct graph
		Graph g = new Graph(vars.size());
		/* conflict1: same place living set */
		for(IcStmt i : f.stmts){
			List<IcVar> tmp_vars = new LinkedList<IcVar>();
			tmp_vars.addAll(i.live_vars);
			for(IcVar v : i.live_vars){
				tmp_vars.remove(v);
				for(IcVar vv : tmp_vars){
					g.add_edge(ordering.get(v), ordering.get(vv));
					g.add_edge(ordering.get(vv), ordering.get(v));
				}
			}
		}
		for(Block b : f.block_l){
			List<IcVar> tmp_vars = new LinkedList<IcVar>();
			tmp_vars.addAll(b.out);
			for(IcVar v : b.out){
				tmp_vars.remove(v);
				for(IcVar vv : tmp_vars){
					g.add_edge(ordering.get(v), ordering.get(vv));
					g.add_edge(ordering.get(vv), ordering.get(v));
				}
			}
		}
		/* conflict2: define one and next livings */
		for(IcStmt i : f.stmts){
			//first next living
			Block b = i.block_belonged;
			int index = b.stmts.indexOf(i);
			HashSet<IcVar> live = null;
			if(index == b.stmts.size()-1)	//last one
				live = b.out;
			else	//next one
				live = b.stmts.get(index+1).live_vars;
			//for define-living conflicts
			for(IcVar v : i.def_var){
				for(IcVar vv : live){
					if(v != vv){
						g.add_edge(ordering.get(v), ordering.get(vv));
						g.add_edge(ordering.get(vv), ordering.get(v));
					}
				}
			}
		}
		/* debug1 */
		//System.err.println(vars);
		//g.debug_print();
		
		//coloring
		List<Integer> regs_alloc = g.coloring(reg_n);
		
		//store info
		f.reg_used = new boolean[reg_n];
		Arrays.fill(f.reg_used, false);
		for(int i: regs_alloc){
			if(i>0)
				f.reg_used[i-1] = true;
		}
		for(IcVar i : ordering.keySet()){
			int place = regs_alloc.get(ordering.get(i));
			f.all_vars_reg.put(i,place);
			//tmp spill
			if(i instanceof IcVar_tmp && place==0)
				f.tmp_spill_l.add((IcVar_tmp)i);
			//outside var
			if(i.fun != f){
				f.var_outside.add(i);
			}
		}
	}
}

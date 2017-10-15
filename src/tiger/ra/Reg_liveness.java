package tiger.ra;

import java.util.*;

import tiger.block.*;
import tiger.ic.*;

public class Reg_liveness {
	/* analyze liveness info for register coloring */
	static void analyze_live(IcFun f){
		/* 0.recursive*/
		for(IcFun ff : f.funs)
			analyze_live(ff);
		/* for every block */
		for(Block b : f.block_l){
			HashSet<IcVar> live = new HashSet<IcVar>();
			int size = b.stmts.size();
			//init with the outside liveness
			live.addAll(b.out);	
			for(int i=size-1;i>=0;i--){
				//first figure the next ref
				IcStmt inst = b.stmts.get(i);
				for(IcVar v : inst.def_var){
					f.all_vars.add(v);	//add to function's all-var
					live.remove(v);
				}
				for(IcVar v : inst.use_var){
					f.all_vars.add(v);
					live.add(v);
				}
				inst.live_vars.addAll(live);
			}
		}
	}
}

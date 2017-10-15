package tiger.block;

import tiger.ic.*;
import java.util.*;

public class Block_step4 {
	/* step4: next reference in one block --- use addr num */
	static void solve_nextref(IcFun f){
		/* 0.recursive*/
		for(IcFun ff : f.funs)
			solve_nextref(ff);
		/* for every block */
		for(Block b : f.block_l){
			/* reverse resolve ref */
			/* 1.init with the all hashmap ---  */
			HashMap<IcVar,Integer> m_all = new HashMap<IcVar,Integer>();
			for(IcVar i : b.use)
				m_all.put(i, IcStmt.NOPE);
			for(IcVar i : b.def)
				m_all.put(i, IcStmt.NOPE);
			for(IcVar i : b.out)
				m_all.put(i, IcStmt.OUTSIDE);
			/* 2.reverse */
			int size = b.stmts.size();
			for(int i=size-1;i>=0;i--){
				//first figure the next ref
				IcStmt inst = b.stmts.get(i);
				for(IcVar v : inst.def_var)
					inst.next_ref_l.put(v, m_all.get(v));
				for(IcVar v : inst.use_var)
					inst.next_ref_l.put(v, m_all.get(v));
				//update the ref info -- use first
				for(IcVar v : inst.use_var)	//update ref
					m_all.put(v, inst.num);
				for(IcVar v : inst.def_var)	//get killed
					m_all.put(v, IcStmt.NOPE);
				
			}
		}
	}
}

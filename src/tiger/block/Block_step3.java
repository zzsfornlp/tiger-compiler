package tiger.block;

import tiger.ic.*;

import java.util.*;

public class Block_step3 {
	/* step3: liveness analysis */
	/*
	 * IN[B] = USEb U (OUT[B] - DEFb)
	 * OUT[B] = U IN[s]
	 */
	static void analyze_liveness(IcFun f){
		/* 0.recursive*/
		for(IcFun ff : f.funs)
			analyze_liveness(ff);
		/* 1.use and def */
		add_use_def(f);
		/* 2.solve the data flow */
		boolean flag = true;
		int size = f.block_l.size();
		while(flag){
			flag = false;
			for(int i=size-1;i>=0;i--){
				Block b = f.block_l.get(i);
				// a copy of in
				HashSet<IcVar> in_clone = (HashSet<IcVar>)b.in.clone();
				// data flow analysis
				for(Block succ : b.gotos){
					b.out.addAll(succ.in);
				}
				HashSet<IcVar> tmp = (HashSet<IcVar>)b.out.clone();
				tmp.removeAll(b.def);
				b.in.addAll(b.use);
				b.in.addAll(tmp);
				
				if(!b.in.equals(in_clone))
					flag = true;
			}
		}
	}
	
	/* help functions */
	private static void add_use(Block b,IcOper i,IcStmt inst){
		if(i instanceof IcVar){
			//add to block's info
			if(!b.def.contains(i))
				b.use.add((IcVar)i);
			//add to inst's info
			inst.use_var.add((IcVar)i);
		}
	}
	private static void add_def(Block b,IcOper i,IcStmt inst){
		if(i instanceof IcVar){
			//add to block's info
			b.def.add((IcVar)i);
			//add to inst's info
			inst.def_var.add((IcVar)i);
		}
	}
	
	private static void add_use_def(IcFun f){
		for(Block b : f.block_l){
			for(IcStmt i: b.stmts){
				if(i instanceof IcStmt_array){
					IcStmt_array inst = (IcStmt_array)i;
					add_use(b,inst.arr,inst);
					add_use(b,inst.index,inst);
					add_use(b,inst.value,inst);
				}
				else if(i instanceof IcStmt_array2){
					IcStmt_array2 inst = (IcStmt_array2)i;
					add_use(b,inst.arr,inst);
					add_use(b,inst.index,inst);
					add_def(b,inst.assigned,inst);
				}
				else if(i instanceof IcStmt_assign){
					IcStmt_assign inst = (IcStmt_assign)i;
					add_use(b,inst.right,inst);
					add_def(b,inst.assigned,inst);
				}
				else if(i instanceof IcStmt_branch){
					IcStmt_branch inst = (IcStmt_branch)i;
					add_use(b,inst.pred1,inst);
					add_use(b,inst.pred2,inst);
				}
				else if(i instanceof IcStmt_call){
					IcStmt_call inst = (IcStmt_call)i;
					for(IcOper o : inst.args)
						add_use(b,o,inst);
				}
				else if(i instanceof IcStmt_callback){
					IcStmt_callback inst = (IcStmt_callback)i;
					for(IcOper o : inst.args)
						add_use(b,o,inst);
					add_def(b,inst.result,inst);
				}
				else if(i instanceof IcStmt_cmp){
					IcStmt_cmp inst = (IcStmt_cmp)i;
					add_use(b,inst.left,inst);
					add_use(b,inst.right,inst);
					add_def(b,inst.assigned,inst);
				}
				else if(i instanceof IcStmt_new){
					IcStmt_new inst = (IcStmt_new)i;
					add_use(b,inst.size,inst);
					add_def(b,inst.result,inst);
				}
				else if(i instanceof IcStmt_op){
					IcStmt_op inst = (IcStmt_op)i;
					add_use(b,inst.left,inst);
					add_use(b,inst.right,inst);
					add_def(b,inst.assigned,inst);
				}
				else if(i instanceof IcStmt_record){
					IcStmt_record inst = (IcStmt_record)i;
					add_use(b,inst.rec,inst);
					add_use(b,inst.value,inst);
				}
				else if(i instanceof IcStmt_record2){
					IcStmt_record2 inst = (IcStmt_record2)i;
					add_use(b,inst.rec,inst);
					add_def(b,inst.assigned,inst);
				}
				else if(i instanceof IcStmt_return){
					IcStmt_return inst = (IcStmt_return)i;
					add_use(b,inst.return_v,inst);
				}
			}
		}
	}
}

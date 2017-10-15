package tiger.block;

import tiger.ic.*;

public class Block_step2 {
	/* step2: apart the blocks */
	static void get_blocks(IcFun f){
		/* 0.recursive*/
		for(IcFun ff : f.funs)
			get_blocks(ff);
		/* 1.go through the stms only update goto_tmp*/
		// for speed
		IcStmt []s = f.stmts.toArray(new IcStmt[0]);
		// go through
		int count = 1;
		Block b = new Block(f,count++);
		f.block_l.add(b);
		for(int i=0;i<s.length;i++){
			IcStmt now = s[i];
			if(b.stmts.size()!=0 && now instanceof IcStmt_label){
				//store link info
				b.goto_addrs.add(now.num);
				//new one
				b = new Block(f,count++);
				f.block_l.add(b);
				b.stmts.add(now);
				now.block_belonged = b;
			}
			else if(now instanceof IcStmt_goto){
				IcStmt_goto tmp = (IcStmt_goto)now;
				b.stmts.add(now);
				now.block_belonged = b;
				//goto
				b.goto_addrs.add(tmp.label.num);
				//then new one
				b = new Block(f,count++);
				f.block_l.add(b);
			}
			else if(now instanceof IcStmt_branch){
				IcStmt_branch tmp = (IcStmt_branch)now;
				b.stmts.add(now);
				now.block_belonged = b;
				//branch
				b.goto_addrs.add(now.num+1);
				b.goto_addrs.add(tmp.label.num);
				//then new one
				b = new Block(f,count++);
				f.block_l.add(b);
			}
			else{
				b.stmts.add(now);
				now.block_belonged = b;
			}
		}
		/* 2.resolve the links */
		for(Block one : f.block_l){
			for(Integer x : one.goto_addrs){
				one.gotos.add(s[x-1].block_belonged);
				s[x-1].block_belonged.comes.add(one);
			}
		}
	}
}

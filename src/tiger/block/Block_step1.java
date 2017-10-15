package tiger.block;

import tiger.ic.*;
import java.util.*;

public class Block_step1 {
	/* step1: combine the nearing flags(labels) */
	static void combine_flags(IcFun f){
		/* 0.combine the sub funs' flags(not related)*/
		for(IcFun ff : f.funs)
			combine_flags(ff);
		/* 1.find nearing flags --- only preserve the first */
		IcStmt_label []la = f.labels.toArray(new IcStmt_label[0]);
		Arrays.sort(la, new Comparator<IcStmt_label>(){
			public int compare(IcStmt_label t1,IcStmt_label t2){
				return (t1.num==t2.num)?0:((t1.num>t2.num)?1:-1);
			}
		});
		int size = f.labels.size();
		int i=0,head=0;
		while(true){
			if(head>=size)
				break;
			i = head + 1;
			//search ahead
			while(true){
				if(i<size && la[i].num==la[i-1].num+1){
					i++;
				}
				else
					break;
			}
			i--;
			//clear nearing one
			for(int t=head+1;t<=i;t++){
				//replace for goto and branch
				for(IcStmt s:f.stmts){
					if(s instanceof IcStmt_goto)
						((IcStmt_goto)s).replace_label(la[t], la[head]);
					else if(s instanceof IcStmt_branch)
						((IcStmt_branch)s).replace_label(la[t], la[head]);
				}
				//delete in the function
				f.labels.remove(la[t]);
				f.stmts.remove(la[t]);
			}
			//rewind
			head = i+1;
		}
		/* 2.recalculate the num for each stmt */
		int num=0;
		for(IcStmt stmt:f.stmts)
			stmt.num = ++num;
	}
}

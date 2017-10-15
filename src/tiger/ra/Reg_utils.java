package tiger.ra;

import tiger.ic.IcFun;

public class Reg_utils {
	/* regster allocation 2 steps */
	static public void reg_start(IcFun main,int max){
		Reg_liveness.analyze_live(main);
		Reg_regalloc.alloc_reg(main, max);
	}
}

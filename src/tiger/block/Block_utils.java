package tiger.block;

import tiger.ic.*;

public class Block_utils {
	/* from the stage of block and so on
	 * use static function instead of class method
	 * which maybe not good design but maybe clear
	 */
	
	static public void block_start(IcFun main){
		Block_step1.combine_flags(main);
		Block_step2.get_blocks(main);
		Block_step3.analyze_liveness(main);
		Block_step4.solve_nextref(main);
	}
}

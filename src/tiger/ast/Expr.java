package tiger.ast;

import tiger.typing.*;
import tiger.errors.*;
import tiger.symbol.*;
import tiger.ic.*;

abstract public class Expr extends Zposition{
	public Type the_type;
	abstract public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e);
	
	/* ic --- return a tuple of two objects(stmt_list,result) --- (LinkedList<IcStmt>,IcOper) 
	 * stmt_list can not be null, but result can be null(if typecheck ok,there'll be no error)
	 * really rely on type check
	 */
	abstract public Object[] ic_trans(IcFun f,IcStmt_label break_to);
}

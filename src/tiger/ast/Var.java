package tiger.ast;

import tiger.errors.ErrorList;
import tiger.ic.IcFun;
import tiger.ic.IcStmt_label;
import tiger.symbol.*;
import tiger.typing.Type;

abstract public class Var extends Zposition{
	public Type the_type;
	abstract public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e);
	
	abstract public Object[] ic_trans(IcFun f,IcStmt_label break_to);
}

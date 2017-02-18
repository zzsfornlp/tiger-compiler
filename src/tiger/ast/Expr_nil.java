package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.ic.IcConst_int;
import tiger.ic.IcFun;
import tiger.ic.IcStmt;
import tiger.ic.IcStmt_label;
import tiger.symbol.Table;
import tiger.typing.Type;

public class Expr_nil extends Expr{
	public Expr_nil(int l,int c){
		line = l;
		column = c;
	}
	
	/* the type checking */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		the_type = Type.THE_NIL;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		return new Object[]{new LinkedList<IcStmt>(),new IcConst_int(0)};
	}
}

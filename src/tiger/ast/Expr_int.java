package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.symbol.Table;
import tiger.typing.Type;
import tiger.ic.*;

public class Expr_int  extends Expr{
	public Integer value;
	public Expr_int(int l,int c,Integer v){
		line = l;
		column = c;
		value = v;
	}
	
	/* the type checking */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		the_type = Type.THE_INT;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		return new Object[]{new LinkedList<IcStmt>(),new IcConst_int(value)};
	}
}

package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.symbol.Table;
import tiger.typing.*;
import tiger.ic.*;

public class Expr_str extends Expr{
	public String value;
	public Expr_str(int l,int c,String v){
		line = l;
		column = c;
		value = v;
	}
	
	/* the type checking */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		the_type = Type.THE_STRING;
		return the_type;
	}
	
	/* lc */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		return new Object[]{new LinkedList<IcStmt>(),new IcConst_str(value)};
	}
}

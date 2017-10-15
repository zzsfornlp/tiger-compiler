package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.ic.IcConst_str;
import tiger.ic.IcFun;
import tiger.ic.IcStmt;
import tiger.ic.IcStmt_label;
import tiger.symbol.*;
import tiger.typing.*;

public class Expr_var extends Expr{
	public Var value;
	public Expr_var(int l,int c,Var v){
		line = l;
		column = c;
		value = v;
	}
	
	/* type checking & whether in loop */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		the_type = value.type_check(t_env, v_env, false, e);
		return the_type;
	}
	
	/* lc */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		return value.ic_trans(f, break_to);
	}
}

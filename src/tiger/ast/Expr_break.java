package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.ic.*;
import tiger.symbol.Table;
import tiger.typing.Type;

public class Expr_break extends Expr{
	public Expr_break(int l,int c){
		line = l;
		column = c;
	}
	
	/* type checking & whether in loop */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		if(!in_loop)
			e.add_error("Break expr not in a loop", line, column, "typing");
		the_type = Type.THE_VOID;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		codes.add(new IcStmt_goto(f,0,break_to));
		return new Object[]{codes,null};
	}
}

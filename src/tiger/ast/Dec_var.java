package tiger.ast;

import tiger.errors.ErrorList;
import tiger.symbol.*;
import tiger.typing.*;
import tiger.ic.*;

public class Dec_var extends Dec{
	public Zsymbol id;
	public Zsymbol tid;		/* optional, when null ... */
	public Expr expr;
	public Dec_var(int l,int c,Zsymbol s,Expr e,Zsymbol ss){
		line = l;
		column = c;
		id = s;
		expr = e;
		tid = ss;
	}
		
	/* type checking */
	public Type the_type;
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		/* 0.get expr type */
		Type tmp = expr.type_check(t_env, v_env, false, e);
		/* 1.get declaration type */
		if(tid==null){//no type dec
			if(tmp instanceof NIL)
				e.add_error("Can't assign nil to var with no type declaration", line, column, "typing");
			the_type = tmp;
			return the_type;
		}
		else{
			Type tid_type = t_env.get(tid);
			if(tid_type==null){
				e.add_error("Undeclared type name "+tid, line, column, "typing");
				the_type = tmp;
				return the_type;
			}
			else if(!tid_type.coerceTo(tmp)){
				e.add_error("Conflict type when declarting var "+id, line, column, "typing");
				the_type = tmp;
				return the_type;
			}
			else{
				the_type = tid_type;
				return the_type;
			}
		}
	}
	
	/* ic */
	public tiger.ic.IcVar ic_var;
}

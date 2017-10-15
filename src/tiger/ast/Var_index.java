package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.ic.*;
import tiger.symbol.*;
import tiger.typing.*;

public class Var_index extends Var{
	public Var var;
	public Expr expr;
	public Var_index(int l,int c,Var v,Expr e){
		line = l;
		column = c;
		var = v;
		expr = e;
	}
	
	/* type checking & whether in loop */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		Type t = var.type_check(t_env, v_env, false, e);
		Type t_int = expr.type_check(t_env, v_env, false, e);
		if(!(t_int.actual() instanceof INT))
			e.add_error("Type of for var_array index not integer", expr.line, expr.column, "typing");
		
		if(!(t.actual() instanceof ARRAY)){
			e.add_error("Type of the variable not array", line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
			return the_type;
		}
		else{
			the_type = ((ARRAY)(t.actual())).element;
			return the_type;
		}	
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		
		// arr index
		Object[] result2 = expr.ic_trans(f, null);
		codes.addAll((LinkedList<IcStmt>)result2[0]);
		IcOper the_index = (IcOper)result2[1];
		// arr itself
		Object[] result3 = var.ic_trans(f, null);
		codes.addAll((LinkedList<IcStmt>)result3[0]);
		IcOper the_arr = (IcOper)result3[1];
		// tmp
		IcVar_tmp the_t = IcVar_tmp.new_one(f);
		codes.add(new IcStmt_array2(f,0,the_arr,the_index,the_t));
		
		return new Object[]{codes,the_t};
	}
}

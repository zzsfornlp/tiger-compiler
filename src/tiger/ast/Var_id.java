package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.ic.*;
import tiger.symbol.*;
import tiger.typing.*;

public class Var_id extends Var{
	public Zsymbol sym;
	public Var_id(int l,int c,Zsymbol s){
		line = l;
		column = c;
		sym = s;
	}
	
	/* type checking & whether in loop */
	public Dec_var the_dec;
	
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		Dec d = v_env.get(sym);
		if(d==null){
			e.add_error("Var "+sym+" not declared", line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
			return the_type;
		}
		else if(!(d instanceof Dec_var)){
			e.add_error("Var "+sym+" should be variable", line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
			return the_type;
		}
		else{
			the_dec = (Dec_var)d;
			Type tmp = ((Dec_var)d).the_type;
			the_type = tmp;
			return the_type;
		}
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		return new Object[]{new LinkedList<IcStmt>(),the_dec.ic_var};
	}
}

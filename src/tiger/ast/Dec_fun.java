package tiger.ast;

import tiger.errors.ErrorList;
import tiger.symbol.Table;
import tiger.symbol.Zsymbol;
import tiger.typing.*;
import tiger.ast.Dec_var;

import java.util.*;

public class Dec_fun extends Dec{
	public Zsymbol id;
	public Expr expr;
	public Listty_field fields;	/* optional */
	public Zsymbol tid;			/* optional */
	
	public boolean is_builtin=false;
	public Dec_fun(Zsymbol i,Type r,List<Type> l){
		id = i;
		is_builtin = true;
		return_type = r;
		arg_type = l;
	}
	public Dec_fun(int l,int c,Zsymbol s,Expr e,Listty_field f,Zsymbol ss){
		line = l;
		column = c;
		id = s;
		expr = e;
		fields = f;
		tid = ss;
	}
	
	/* type checking */
	public Type return_type;
	public List<Type> arg_type=new LinkedList<Type>();
	public List<Dec_var> arg_dec=new LinkedList<Dec_var>();
	
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		v_env = v_env.beginScope();
		Iterator<Type> iter = arg_type.iterator();
		if(fields!=null){
			for(Field_ty k : fields.list){
				Dec_var dv = new Dec_var(k.line,k.column,k.id,null,null);
				dv.the_type = iter.next();
				arg_dec.add(dv);
				v_env.put(k.id, dv);
			}
		}
		Type tmp = expr.type_check(t_env, v_env, false, e);
		if(!tmp.coerceTo(return_type)){
			e.add_error("Type mismatch for return type of function "+id, expr.line, expr.column, "typing");
		}
		v_env = v_env.endScope();
		return return_type;
	}
	public Type type_get(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		/* return type */
		if(tid==null)
			return_type = Type.THE_VOID;
		else{
			Type tmp = t_env.get(tid);
			if(tmp==null){
				e.add_error("Undeclared return type "+tid+" in function "+id, line, column, "typing");
				return_type = Type.THE_ERROR_STATE;
			}
			else
				return_type = tmp;
		}
		/* args */
		if(fields!=null){
			for(Field_ty k : fields.list){
				Type tmp = t_env.get(k.tid);
				if(tmp==null){
					e.add_error("Undeclared arg type "+k.tid+" in function "+id+" 's arg"+k.id, k.line, k.column, "typing");
					arg_type.add(Type.THE_ERROR_STATE);
				}
				else
					arg_type.add(tmp);
			}
		}
		return return_type;
	}
	
	/* ic */
	public tiger.ic.IcFun ic_fun;
}

package tiger.ast;

import java.util.*;

import tiger.errors.ErrorList;
import tiger.ic.*;
import tiger.symbol.*;
import tiger.typing.*;


public class Expr_app extends Expr{
	public Zsymbol function;
	public List_expr list;	/* optional */
	public Expr_app(int l,int c,Zsymbol f,List_expr li){
		line = l;
		column = c;
		function = f;
		list = li;
	}
	
	/* type checking */
	public Dec_fun the_dec;
	
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		/* 1.first check the function name */
		Dec f = v_env.get(function);
		if(f==null){//no such function name
			e.add_error("Undeclared function name "+function, line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
			return the_type;
		}
		else if(!(f instanceof Dec_fun)){//not a function name
			e.add_error("Wrong function name "+function, line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
			return the_type;
		}
		/* 2.match the args list */
		Dec_fun ff = (Dec_fun)f;
		the_dec = ff;
		List<Type> should_be = ff.arg_type;
		List<Expr> real_is = (list==null)?(new ArrayList<Expr>()):list.list;	//args is optional
		if(should_be.size() != real_is.size()){
			e.add_error("Wrong args num in the call of "+function, line, column, "typing");
			the_type = ff.return_type;
			return the_type;
		}
		else{
			Iterator<Type> iter_t = should_be.iterator();
			Iterator<Expr> iter_e = real_is.iterator();
			int count=0;
			while(iter_e.hasNext()){
				count++;
				Expr tmp = iter_e.next();
				Type t_tmp = iter_t.next();
				Type real = tmp.type_check(t_env, v_env, false, e);
				if(!t_tmp.coerceTo(real)){
					e.add_error("Arg type mismatch in the call of "+function+":arg "+count+" type should be "+t_tmp+" but is "+real, 
							tmp.line, tmp.column, "typing");
				}
			}
			the_type = ff.return_type;
			return the_type;
		}
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		IcOper r = null;
		/* 1. the args */
		LinkedList<IcOper> arg_list = new LinkedList<IcOper>();
		if(list != null){
			for(Expr e: list.list){
				Object[] result = e.ic_trans(f, break_to);
				codes.addAll((LinkedList<IcStmt>)result[0]);
				arg_list.add((IcOper)result[1]);
			}
		}
		/* 2. the call and return */
		if(the_dec.return_type.coerceTo(Type.THE_VOID)){
			IcStmt_call call = new IcStmt_call(f,0,the_dec.ic_fun);
			call.args.addAll(arg_list);
			codes.add(call);
		}
		else{
			IcVar_tmp tmp = IcVar_tmp.new_one(f);
			IcStmt_callback call = new IcStmt_callback(f,0,the_dec.ic_fun,tmp);
			call.args.addAll(arg_list);
			codes.add(call);
			r = tmp;
		}
		return new Object[]{codes,r};
	}
}

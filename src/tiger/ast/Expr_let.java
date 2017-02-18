package tiger.ast;

import java.util.*;

import tiger.errors.ErrorList;
import tiger.ic.IcFun;
import tiger.ic.IcStmt_label;
import tiger.symbol.*;
import tiger.typing.*;
import tiger.ic.*;


public class Expr_let extends Expr{
	List_dec dec;
	Listseq_expr seq;	/* optional */
	public Expr_let(int l,int c,List_dec d,Listseq_expr s){
		line = l;
		column = c;
		dec = d;
		seq = s;
	}
	
	/* the type checking */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		/* 0.new env */
		t_env = t_env.beginScope();
		v_env = v_env.beginScope();
		/* 1.check the dec */
		List<Dec> l = dec.list;
		Dec now;
		int index=0;
		int lenght = l.size();
		while(index<lenght){
			now = l.get(index++);
			if(now instanceof Dec_var){
				/* deal with Dec_var */
				((Dec_var)now).type_check(t_env, v_env, false, e);
				v_env.put(((Dec_var) now).id, now);
			}
			else if(now instanceof Dec_fun){
				List<Dec_fun> seq = new LinkedList<Dec_fun>();
				/* get Dec_fun seq */
				seq.add((Dec_fun)now);
				while(index<lenght && l.get(index) instanceof Dec_fun){
					seq.add((Dec_fun)l.get(index++));
				}
				/* deal with them in 2 parts */
				Set<Zsymbol> name_sets = new HashSet<Zsymbol>();
				List<Dec_fun> to_delete = new LinkedList<Dec_fun>();
				/* step1: get interface type */
				for(Dec_fun d : seq){
					if(name_sets.contains(d.id)){
						/* conflict function name in the same seq -- just abandon */
						e.add_error("Conflict function declaration in the same seq", d.line, d.column, "typing");
						to_delete.add(d);
					}
					else{
						name_sets.add(d.id);
						d.type_get(t_env, v_env, false, e);
						v_env.put(((Dec_fun)d).id, d);
					}
				}
				seq.removeAll(to_delete);
				/* step2: check body type */
				for(Dec_fun d : seq){
					d.type_check(t_env, v_env, false, e);
				}
			}
			else{
				List<Dec_type> seq = new LinkedList<Dec_type>();
				/* get Dec_type seq */
				seq.add((Dec_type)now);
				while(index<lenght && l.get(index) instanceof Dec_type){
					seq.add((Dec_type)l.get(index++));
				}
				/* deal with them */
				Set<Zsymbol> name_sets = new HashSet<Zsymbol>();
				List<Dec_type> to_delete = new LinkedList<Dec_type>();
				/* step1: put type TO_FILL for each dec with no name binding */
				for(Dec_type d : seq){
					if(name_sets.contains(d.id)){
						/* conflict function name in the same seq -- just abandon */
						e.add_error("Conflict type declaration in the same seq", d.line, d.column, "typing");
						to_delete.add(d);
					}
					else{
						name_sets.add(d.id);
						t_env.put(d.id, new TO_FILL(d.id));
					}
				}
				seq.removeAll(to_delete);
				/* step2: get everyone's type */
				for(Dec_type d : seq){
					t_env.put(d.id,d.type_check(t_env, v_env, false, e));
				}
				/* step3: fill in types */
				for(Dec_type d : seq)
					fill_in(t_env.get(d.id),t_env);
				/* step4: check type loops */
				for(Dec_type d : seq)
					check_loop(d,t_env,e);
			}
		}
		/* 2.go to the expr list */
		if(seq==null){
			the_type = Type.THE_VOID;
		}
		else{
			Type tmp=null;
			for(Expr ee:seq.list)
				tmp = ee.type_check(t_env, v_env, in_loop, e);
			if(tmp==null){
				the_type = Type.THE_VOID;
			}
			else{
				the_type = tmp;
				
			}
		}
		/* 3.close env */
		t_env = t_env.endScope();
		v_env = v_env.endScope();
		return the_type;
	}
	
	/* help functions */
	static private void fill_in(Type t,Table<Type> t_env){
		//fill in the unbinding NMAE type
		NAME n=null;
		if(t instanceof ARRAY){
			ARRAY tmp = (ARRAY)t;
			if(tmp.element instanceof NAME){
				n = (NAME)tmp.element;
				if(n.binding instanceof TO_FILL)
					n.binding = t_env.get(n.name);
			}
		}
		else if(t instanceof RECORD){
			RECORD tmp = (RECORD)t;
			Map<Zsymbol,Type> m = tmp.fie;
			for(Zsymbol s : m.keySet()){
				if(m.get(s) instanceof NAME){
					n = (NAME)m.get(s);
					if(n.binding instanceof TO_FILL)
						n.binding = t_env.get(n.name);
				}
			}
		}
		else if(t instanceof NAME){
			NAME tmp = (NAME)t;
			if(tmp.binding instanceof TO_FILL){
				tmp.binding = t_env.get(tmp.name);
			}
		}
	}
	static private void check_loop(Dec_type d,Table<Type> t_env,ErrorList e){
		Type tmp = t_env.get(d.id);
		if(tmp instanceof NAME){
			if(((NAME)tmp).isLoop()){
				e.add_error("Loop type declaration", d.line, d.column, "typing");
			}
		}
	}
	
	/* lc */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		IcOper r = null;
		/* 1.check the dec list, first add into the IcFun's list */
		for(Dec d : dec.list){
			if(d instanceof Dec_var){
				Dec_var dd = (Dec_var)d;
				//new IcVar
				IcVar_local t = new IcVar_local(f,dd.id.toString(),f.var_l.size(),dd);	
				//add to IcFun's list
				f.var_l.add(t);		
				//add to Dec's field
				dd.ic_var = t;
			}
			else if(d instanceof Dec_fun){
				Dec_fun dd = (Dec_fun)d;
				//new IcFun
				IcFun t = new IcFun();
				t.name = dd.id.toString();
				t.belonged_fun = f;
				t.level = f.level + 1;
				t.dec = dd;
				for(Dec_var i : dd.arg_dec){
					IcVar_param p = new IcVar_param(t,i.id.toString(),t.var_p.size(),i);
					i.ic_var = p;
					t.var_p.add(p);
				}
				//add to list
				f.funs.add(t);
				//Dec_fun's field
				dd.ic_fun = t;
			}
		}
		/* 2.figure out the dec list --- get codes */
		for(Dec d : dec.list){
			if(d instanceof Dec_var){
				Dec_var dd = (Dec_var)d;
				Object[] result = dd.expr.ic_trans(f, null);
				codes.addAll((LinkedList<IcStmt>)result[0]);
				codes.add(new IcStmt_assign(f,0,dd.ic_var,(IcOper)result[1]));
			}
			else if(d instanceof Dec_fun){
				//for function,add to the referred IcFun
				Dec_fun dd = (Dec_fun)d;
				Object[] result = dd.expr.ic_trans(dd.ic_fun, null);
				dd.ic_fun.stmts.addAll((LinkedList<IcStmt>)result[0]);
				dd.ic_fun.stmts.add(new IcStmt_return(dd.ic_fun,0,(IcOper)result[1]));
				//order function's statement
				int num=0;
				for(IcStmt i:dd.ic_fun.stmts)
					i.num = ++num;
			}
		}
		/* 3.calculate body */
		Object[] result=new Object[]{null,null};
		if(seq!=null){
			for(Expr ee:seq.list){
				result = ee.ic_trans(f, break_to);
				codes.addAll((LinkedList<IcStmt>)result[0]);
			}
		}
		//result is the last one
		r = (IcOper)result[1];
		return new Object[]{codes,r};
	}
}

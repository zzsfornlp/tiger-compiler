package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.ic.*;
import tiger.symbol.*;
import tiger.typing.*;

public class Expr_for extends Expr{
	Zsymbol sym;
	Expr e1,e2,e3;
	public Expr_for(int l,int c,Zsymbol s,Expr ex1,Expr ex2,Expr ex3){
		line = l;
		column = c;
		sym = s;
		e1 = ex1;
		e2 = ex2;
		e3 = ex3;
	}
	
	/* type checking & whether in loop */
	Dec_var the_index;
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		/* 1.add a new scope for sym */
		Dec_var dv = new Dec_var(line,column,sym,e1,null);
		the_index = dv;
		dv.the_type = Type.THE_INT;
		
		v_env = v_env.beginScope();
		v_env.put(sym, dv);
		/* 2.check the range expr,which should be int */
		Type num_type = e1.type_check(t_env, v_env, false, e);
		if(!(num_type.actual() instanceof INT)){
			e.add_error("Type of for range should be integer", e1.line, e1.column, "typing");
		}
		num_type = e2.type_check(t_env, v_env, false, e);
		if(!(num_type.actual() instanceof INT)){
			e.add_error("Type of for range should be integer", e2.line, e2.column, "typing");
		}
		/* 3.check loop body,which must be void */
		Type loop_type = e3.type_check(t_env, v_env, true, e);
		if(!(loop_type.actual() instanceof VOID)){
			e.add_error("Type of for body should be void", e3.line, e3.column, "typing");
		}
		/* 4.close env */
		v_env = v_env.endScope();
		
		the_type = Type.THE_VOID;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		/* i = expr1;
		 * L1: branch L2 if i>expr2
		 *  	expr3
		 *  	i++
		 *  	goto L1
		 *  L2 
		 */
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		
		/* 1.add the dec of index */
		//new IcVar
		IcVar_local t = new IcVar_local(f,the_index.id.toString(),f.var_l.size(),the_index);	
		//add to IcFun's list
		f.var_l.add(t);		
		//add to Dec's field
		the_index.ic_var = t;
		/* 2.i = expr1 */
		Object[] result = e1.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result[0]);
		codes.add(new IcStmt_assign(f,0,t,(IcOper)result[1]));
		/* 3.L1 & L2(not add now) */
		IcStmt_label the_l1 = IcStmt_label.new_one(f, 0);
		codes.add(the_l1);
		f.labels.add(the_l1);
		IcStmt_label the_l2 = IcStmt_label.new_one(f, 0);
		f.labels.add(the_l2);
		/* 4.expr2 */
		Object[] result2 = e2.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result2[0]);
		IcOper the_end = (IcOper)result2[1];
		/* 5.branch */
		codes.add(new IcStmt_branch(f,0,t,the_end,the_l2,IcStmt_branch.GT));
		/* 6.expr3 */
		Object[] result3 = e3.ic_trans(f, the_l2);	//change break_to
		codes.addAll((LinkedList<IcStmt>)result3[0]);
		/* 7.i++ */
		codes.add(new IcStmt_op(f,0,t,t,new IcConst_int(1),IcStmt_op.PLUS));
		/* 8.goto L1 */
		codes.add(new IcStmt_goto(f,0,the_l1));
		/* 9.L2 */
		codes.add(the_l2);
		
		return new Object[]{codes,null};
		
	}
}

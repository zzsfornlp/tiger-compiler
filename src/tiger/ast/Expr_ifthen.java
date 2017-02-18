package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.symbol.Table;
import tiger.typing.*;
import tiger.ic.*;

public class Expr_ifthen extends Expr{
	Expr e1,e2;
	public Expr_ifthen(int l,int c,Expr ex1,Expr ex2){
		line = l;
		column = c;
		e1 = ex1;
		e2 = ex2;
	}
	
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		/* 1.check the predicate expr, which must be integer */
		Type num_type = e1.type_check(t_env, v_env, false, e);
		if(!(num_type.actual() instanceof INT)){
			e.add_error("Type of if predicate should be integer", e1.line, e1.column, "typing");
		}
		/* 2.check the rest two */
		Type t2 = e2.type_check(t_env, v_env, in_loop, e);
		if(!(t2.actual() instanceof VOID)){
			e.add_error("Type of if_then result is not void", line, column, "typing");
		}
		the_type = Type.THE_VOID;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		/* branch L1 if expr1==0
		 * expr2
		 * L1
		 */
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		
		/* 1.L1(not now) */
		IcStmt_label the_l1 = IcStmt_label.new_one(f, 0);
		f.labels.add(the_l1);
		/* 2.expr1 */
		Object[] result = e1.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result[0]);
		IcOper pred = (IcOper)result[1];
		/* 3.branch */
		codes.add(new IcStmt_branch(f,0,pred,new IcConst_int(0),the_l1,IcStmt_branch.EQ));
		/* 4.expr2 --- must be void */
		Object[] result2 = e2.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result2[0]);
		/* 5.L1 */
		codes.add(the_l1);
		
		return new Object[]{codes,null};
	}
}

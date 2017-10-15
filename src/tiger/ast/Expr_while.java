package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.symbol.Table;
import tiger.typing.INT;
import tiger.typing.Type;
import tiger.typing.VOID;
import tiger.ic.*;

public class Expr_while extends Expr{
	Expr e1,e2;
	public Expr_while(int l,int c,Expr ex1,Expr ex2){
		line = l;
		column = c;
		e1 = ex1;
		e2 = ex2;
	}
	
	/* type check */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		/* 1.check the predicate expr, which must be integer */
		Type num_type = e1.type_check(t_env, v_env, false, e);
		if(!(num_type.actual() instanceof INT)){
			e.add_error("Type of while predicate should be integer", e1.line, e1.column, "typing");
		}
		/* 2.check the rest */
		Type t2 = e2.type_check(t_env, v_env, true, e);
		if(!(t2.actual() instanceof VOID)){
			e.add_error("Type of while result is not void", line, column, "typing");
		}
		the_type = Type.THE_VOID;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		/* L1:
		 * branch L2 if expr1==0
		 * expr2
		 * goto L1
		 * L2
		 */
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		
		/* 1.labels */
		IcStmt_label the_l1 = IcStmt_label.new_one(f, 0);
		codes.add(the_l1);
		f.labels.add(the_l1);
		IcStmt_label the_l2 = IcStmt_label.new_one(f, 0);
		f.labels.add(the_l2);
		/* 2.expr1 and branch */
		Object[] result = e1.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result[0]);
		codes.add(new IcStmt_branch(f,0,(IcOper)result[1],new IcConst_int(0),the_l2,IcStmt_branch.EQ));
		/* 3.the expr2 */
		Object[] result2 = e2.ic_trans(f, the_l2);	//change break_to
		codes.addAll((LinkedList<IcStmt>)result2[0]);
		/* 4.the rest */
		codes.add(new IcStmt_goto(f,0,the_l1));
		codes.add(the_l2);
		
		return new Object[]{codes,null};
	}
}
	
	
	

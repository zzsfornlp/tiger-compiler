package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.symbol.Table;
import tiger.typing.INT;
import tiger.typing.Type;
import tiger.ic.*;

public class Expr_ifelse extends Expr{
	Expr e1,e2,e3;
	public Expr_ifelse(int l,int c,Expr ex1,Expr ex2,Expr ex3){
		line = l;
		column = c;
		e1 = ex1;
		e2 = ex2;
		e3 = ex3;
	}
	
	/* type checking & whether in loop */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		/* 1.check the predicate expr, which must be integer */
		Type num_type = e1.type_check(t_env, v_env, false, e);
		if(!(num_type.actual() instanceof INT)){
			e.add_error("Type of if predicate should be integer", e1.line, e1.column, "typing");
		}
		/* 2.check the rest two */
		Type t2 = e2.type_check(t_env, v_env, false, e);
		Type t3 = e3.type_check(t_env, v_env, false, e);
		if(!t2.coerceTo(t3)){
			e.add_error("Type of branches of then/else mismatch", line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
		}
		else{
			the_type = t2;
		}
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		/* branch L1 if expr1==0
		 * expr2
		 * goto L2
		 * L1
		 * expr3
		 * L2
		 */
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();

		IcVar_tmp x = null;
		boolean is_void = the_type.coerceTo(Type.THE_VOID);
		if(!is_void)
			x = IcVar_tmp.new_one(f);
		
		/* 1.L1(not now) & L2(not add now) */
		IcStmt_label the_l1 = IcStmt_label.new_one(f, 0);
		f.labels.add(the_l1);
		IcStmt_label the_l2 = IcStmt_label.new_one(f, 0);
		f.labels.add(the_l2);
		/* 2.expr1 */
		Object[] result = e1.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result[0]);
		IcOper pred = (IcOper)result[1];
		/* 3.branch */
		codes.add(new IcStmt_branch(f,0,pred,new IcConst_int(0),the_l1,IcStmt_branch.EQ));
		/* 4.expr2 */
		Object[] result2 = e2.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result2[0]);
		IcOper r2 = (IcOper)result2[1];
		if(!is_void)
			codes.add(new IcStmt_assign(f,0,x,r2));
		/* 5.goto L2 */
		codes.add(new IcStmt_goto(f,0,the_l2));
		/* 6.L1 */
		codes.add(the_l1);
		/* 7.expr3 */
		Object[] result3 = e3.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result3[0]);
		IcOper r3 = (IcOper)result3[1];
		if(!is_void)
			codes.add(new IcStmt_assign(f,0,x,r3));
		/* 8.L2 */
		codes.add(the_l2);
		
		return new Object[]{codes,x};
	}
}


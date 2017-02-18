package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.symbol.Table;
import tiger.typing.INT;
import tiger.typing.Type;
import tiger.typing.VOID;
import tiger.ic.*;

public class Expr_neg extends Expr{
	public Expr expr;
	public Expr_neg(int l,int c,Expr e){
		line = l;
		column = c;
		expr = e;
	}
	
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		Type tmp = expr.type_check(t_env, v_env, false, e);
		if(!(tmp.actual() instanceof INT))
			e.add_error("Type of for unary-minus expr should be integer", line, column, "typing");
		the_type = Type.THE_INT;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		
		/* 1.expr */
		Object[] result = expr.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result[0]);
		IcOper r = (IcOper)result[1];
		/* 2.neg */
		IcVar_tmp t = IcVar_tmp.new_one(f);
		codes.add(new IcStmt_op(f,0,t,new IcConst_int(0),r,IcStmt_op.MINUS));
		
		return new Object[]{codes,t};
	}
}

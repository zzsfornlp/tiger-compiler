package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.ic.*;
import tiger.symbol.Table;
import tiger.typing.*;

public class Expr_assign extends Expr{
	public Var var;
	public Expr expr;
	public Expr_assign(int l,int c,Var v,Expr e){
		line = l;
		column = c;
		var = v;
		expr = e;
	}
	
	/* type checking */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		Type l_type = var.type_check(t_env, v_env, false, e);
		Type r_type = expr.type_check(t_env, v_env, false, e);
		if(!l_type.coerceTo(r_type)){
			e.add_error("Type mismatch in assignment", line, column, "typing");
		}
		the_type = Type.THE_VOID;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		
		/* 1.figure out the expr */
		Object[] result = expr.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result[0]);
		IcOper the_value = (IcOper)result[1];
		
		/* 2.here must figure out var */
		if(var instanceof Var_id){
			Var_id vv = (Var_id)var;
			codes.add(new IcStmt_assign(f,0,vv.the_dec.ic_var,the_value));
		}
		else if(var instanceof Var_index){
			Var_index vv = (Var_index)var;
			// arr index
			Object[] result2 = vv.expr.ic_trans(f, break_to);
			codes.addAll((LinkedList<IcStmt>)result2[0]);
			IcOper the_index = (IcOper)result2[1];
			// arr itself
			Object[] result3 = vv.var.ic_trans(f, break_to);
			codes.addAll((LinkedList<IcStmt>)result3[0]);
			IcOper the_arr = (IcOper)result3[1];
			//assign
			codes.add(new IcStmt_array(f,0,the_arr,the_index,the_value));
		}
		else{
			Var_field vv = (Var_field)var;
			// rec index
			int the_index = vv.the_record.order.indexOf(vv.sym);
			// rec itself
			Object[] result3 = vv.var.ic_trans(f, break_to);
			codes.addAll((LinkedList<IcStmt>)result3[0]);
			IcOper the_rec = (IcOper)result3[1];
			//assign
			codes.add(new IcStmt_record(f,0,the_rec,new IcConst_int(the_index),the_value));
		}
		
		return new Object[]{codes,null};
	}
}

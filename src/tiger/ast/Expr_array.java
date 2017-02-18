package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.ic.*;
import tiger.symbol.*;
import tiger.typing.*;

public class Expr_array extends Expr{
	public Zsymbol id;	
	public Expr num,value;
	public Expr_array(int l,int c,Zsymbol s,Expr n,Expr v){
		line = l;
		column = c;
		id = s;
		num = n;
		value = v;
	}
	
	/* type checking */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		/* 1.first check the type_id's name */
		Type a_type = t_env.get(id);
		if(a_type==null){//no such tid name
			e.add_error("Undeclared type name "+id, line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
			return the_type;
		}
		else if(!(a_type.actual() instanceof ARRAY)){
			e.add_error("Wrong type "+id+" which is not array type", line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
			return the_type;
		}
		/* 2.check expr--num, which should be int */
		Type num_type = num.type_check(t_env, v_env, false, e);
		if(!(num_type.actual() instanceof INT)){
			e.add_error("Wrong num type in array construct which is not integer", num.line, num.column, "typing");
		}
		/* 3.check expr--value, which should be the same as array_type's element */
		Type v_type = value.type_check(t_env, v_env, false, e);
		Type should_be = ((ARRAY)a_type.actual()).element;
		if(!v_type.coerceTo(should_be)){
			e.add_error("Wrong element type in array construct which should be "+should_be, num.line, num.column, "typing");
		}
		
		the_type = a_type;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		// if num <= 0, error, but ignore that
		/* t = num;
		 * i = 0;
		 * a = new[num];
		 * L: a[i] = value;
		 * 	  branch L if i < num
		 */
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		IcOper r = null;
		
		/* 1.first get num */
		Object[] result = num.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result[0]);
		IcOper the_num = (IcOper)result[1];
		/* 2.tmp t */
		IcVar_tmp the_t = IcVar_tmp.new_one(f);
		codes.add(new IcStmt_assign(f,0,the_t,new IcConst_int(0)));
		/* 3.new arr */
		IcVar_tmp the_t_a = IcVar_tmp.new_one(f);
		codes.add(new IcStmt_new(f,0,the_num,the_t_a));
		/* 4.new label */
		IcStmt_label the_l = IcStmt_label.new_one(f, 0);
		codes.add(the_l);
		f.labels.add(the_l);
		/* 5.the loop --- value */
		Object[] result2 = value.ic_trans(f, break_to);
		codes.addAll((LinkedList<IcStmt>)result2[0]);
		IcOper the_value = (IcOper)result2[1];
		/* 6.the loop --- assign */
		codes.add(new IcStmt_array(f,0,the_t_a,the_t,the_value));
		/* 7.the loop --- plus */
		codes.add(new IcStmt_op(f,0,the_t,the_t,new IcConst_int(1),IcStmt_op.PLUS));
		/* 8.the loop --- branch */
		codes.add(new IcStmt_branch(f,0,the_t,the_num,the_l,IcStmt_branch.LT));
		
		return new Object[]{codes,the_t_a};
	}
}

package tiger.ast;

import java.util.LinkedList;
import java.util.Map;

import tiger.errors.ErrorList;
import tiger.symbol.*;
import tiger.typing.*;
import tiger.ic.*;

public class Expr_struct extends Expr{
	// maybe the order does not matter
	public Zsymbol id;
	public List_field list;	/* optional */
	public Expr_struct(int l,int c,Zsymbol s,List_field li){
		line = l;
		column = c;
		id = s;
		list = li;
	}
	
	/* type checking & whether in loop */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		/* 1.first check the type_id's name */
		Type a_type = t_env.get(id);
		if(a_type==null){//no such tid name
			e.add_error("Undeclared type name "+id, line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
			return the_type;
		}
		else if(!(a_type.actual() instanceof RECORD)){
			e.add_error("Wrong type "+id+" which is not record type", line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
			return the_type;
		}
		/* 2.check the fields */
		RECORD r = (RECORD)(a_type.actual());
		Map<Zsymbol,Type> m = r.fie;
		for(Field f : list.list){
			Type tmp = m.get(f.sym);
			if(tmp==null)
				e.add_error("No such field "+f.sym+" in struct "+id, f.line, f.column, "typing");
			else if(!f.expr.type_check(t_env, v_env, false, e).coerceTo(tmp))
				e.add_error("Type mismatch in field "+f.sym+" in struct "+id, f.line, f.column, "typing");
		}
		the_type = r;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		/* t = new(size of field)
		 * then init...
		 */
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		IcOper r = null;
		
		/* 1.first get num and new*/
		RECORD re = (RECORD)the_type;
		int num = re.order.size();
		IcVar_tmp t = IcVar_tmp.new_one(f);
		codes.add(new IcStmt_new(f,0,new IcConst_int(num),t));
		/* 2.initialize the fields */
		for(Field ff : list.list){
			int place = re.order.indexOf(ff.sym);	//from 0
			Object[] result1 = ff.expr.ic_trans(f, break_to);
			codes.addAll((LinkedList<IcStmt>)result1[0]);
			IcOper the_value = (IcOper)result1[1];
			codes.add(new IcStmt_record(f,0,t,new IcConst_int(place),the_value));
		}
		
		return new Object[]{codes,t};
	}
}

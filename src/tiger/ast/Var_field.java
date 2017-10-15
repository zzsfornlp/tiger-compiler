package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.ic.*;
import tiger.symbol.Table;
import tiger.symbol.Zsymbol;
import tiger.typing.*;

public class Var_field extends Var{
	public Var var;
	public Zsymbol sym;
	public Var_field(int l,int c,Var v,Zsymbol s){
		line = l;
		column = c;
		sym = s;
		var = v;
	}
	
	public RECORD the_record;
	/* type checking & whether in loop */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		Type t = var.type_check(t_env, v_env, false, e);
		
		if(!(t.actual() instanceof RECORD)){
			e.add_error("Type of the variable not record", line, column, "typing");
			the_type = Type.THE_ERROR_STATE;
			return the_type;
		}
		else{
			RECORD tt = (RECORD)(t.actual());
			the_record = tt;
			Type ft = tt.fie.get(sym);
			if(ft == null){
				e.add_error("No such field "+sym+" in record", line, column, "typing");
				the_type = Type.THE_ERROR_STATE;
				return the_type;
			}
			else{
				the_type = ft;
				return the_type;
			}
		}
			
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		
		// rec index
		int the_index = the_record.order.indexOf(sym);
		// rec itself
		Object[] result3 = var.ic_trans(f, null);
		codes.addAll((LinkedList<IcStmt>)result3[0]);
		IcOper the_rec = (IcOper)result3[1];
		// tmp
		IcVar_tmp the_t = IcVar_tmp.new_one(f);
		codes.add(new IcStmt_record2(f,0,the_rec,new IcConst_int(the_index),the_t));
				
		return new Object[]{codes,the_t};
	}
}

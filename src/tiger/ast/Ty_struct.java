package tiger.ast;

import tiger.errors.ErrorList;
import tiger.symbol.Table;
import tiger.symbol.Zsymbol;
import tiger.typing.*;

import java.util.*;

public class Ty_struct extends Ty{
	public Listty_field fields; 	/* optional */
	public Ty_struct(int l,int c,Listty_field f){
		line = l;
		column = c;
		fields = f;
	}
	
	/* type check */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		LinkedHashMap<Zsymbol,Type> m = new LinkedHashMap<Zsymbol,Type>();
		RECORD s = new RECORD(m);
		for(Field_ty k : fields.list){
			Type tmp = t_env.get(k.tid);
			if(tmp==null){
				e.add_error("Undeclared field type "+k.tid+" in record's field "+k.id, k.line, k.column, "typing");
				m.put(k.id,(Type.THE_ERROR_STATE));
			}
			else{
				NAME n = new NAME(k.tid);
				n.bind(tmp);
				m.put(k.id, n);
			}
		}
		//add the order of RECORD
		s.order.addAll(s.fie.keySet());
		return s;
	}
}

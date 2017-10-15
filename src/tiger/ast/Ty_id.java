package tiger.ast;

import tiger.errors.ErrorList;
import tiger.symbol.*;
import tiger.typing.ARRAY;
import tiger.typing.NAME;
import tiger.typing.Type;

public class Ty_id extends Ty{
	public Zsymbol sym;
	public Ty_id(int l,int c,Zsymbol s){
		line = l;
		column = c;
		sym = s;
	}
	
	/* type check */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		Type tmp = t_env.get(sym);
		if(tmp==null){
			e.add_error("Undeclared type "+sym+"in id type declaration", line, column, "typing");
			return Type.THE_ERROR_STATE;
		}
		else{
			NAME n = new NAME(sym);
			n.bind(tmp);
			return n;
		}
	}
}

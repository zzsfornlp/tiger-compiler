package tiger.ast;

import tiger.errors.ErrorList;
import tiger.symbol.Table;
import tiger.symbol.Zsymbol;
import tiger.typing.Type;

public class Dec_type extends Dec{
	public Zsymbol id;
	public Ty type;
	public Dec_type(int l,int c,Zsymbol s,Ty t){
		line = l;
		column = c;
		id = s;
		type = t;
	}
	
	/* type check */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		return type.type_check(t_env, v_env, false, e);
	}
}

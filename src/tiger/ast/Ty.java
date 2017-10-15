package tiger.ast;

import tiger.errors.ErrorList;
import tiger.symbol.Table;
import tiger.typing.Type;

abstract public class Ty extends Zposition{
	abstract public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e);
}

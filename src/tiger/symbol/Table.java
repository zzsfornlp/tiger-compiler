package tiger.symbol;

import java.util.*;
import tiger.ast.*;
import tiger.typing.*;
import tiger.symbol.*;

public class Table<T>{
	static public Table<Dec> init_v_env = new Table<Dec>();
	static public Table<Type> init_t_env = new Table<Type>();
	static{
		init_t_env.put(Zsymbol.symbol("int"), Type.THE_INT);
		init_t_env.put(Zsymbol.symbol("string"), Type.THE_STRING);
		tiger.builtins.Builtins.add_stdfunctions_dec(init_v_env);
	}
	
	
  private Map<Zsymbol, T> dict = new HashMap<Zsymbol, T>();
  private Table<T> old;

  public Table(){}
  public Table(Table<T> x){
	  old = x;
  }

  public T get(Zsymbol key) {
	T tmp = dict.get(key);
	if(tmp==null){
		if(old==null)
			return null;	//no dec symbol
		else
			return old.get(key);
	}
	else
		return tmp;
  }	
  public void put(Zsymbol key, T value) {
	dict.put(key,value);
  }
 /**
  * Remembers the current state of the Table.
  */
  public Table<T> beginScope(){
	  return new Table<T>(this);
  }
 /** 
  * Restores the table to what it was at the most recent beginScope
  *	that has not already been ended.
  */
  public Table<T> endScope() {
	  return old;
  }
}


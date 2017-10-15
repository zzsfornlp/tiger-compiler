package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Not {
	static IcFun ic_not = new IcFun();
	
	/* type checking */
	static Dec_fun not;
	static{
		List<Type> l = new LinkedList<Type>();
		l.add(Type.THE_INT);
		not = new Dec_fun(Zsymbol.symbol("not"),Type.THE_INT,l);
		not.ic_fun = Not.ic_not;
	}
	
	/* ic */
	
	static{
		ic_not.name = "_not";
		ic_not.is_builtin = true;
		ic_not.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".text\n"+
			"_not:\n"+
			"lw $a0,12($fp)\n"+
			"seq $v0,$a0,0\n"+
			"j $ra\n"+
			"\n";
}

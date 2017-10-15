package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Ord {
	static IcFun ic_ord = new IcFun();
	
	/* type checking */
	static Dec_fun ord;
	static{
		List<Type> l = new LinkedList<Type>();
		l.add(Type.THE_STRING);
		ord = new Dec_fun(Zsymbol.symbol("ord"),Type.THE_INT,l);
		ord.ic_fun = Ord.ic_ord;
	}
	
	/* ic */
	
	static{
		ic_ord.name = "_ord";
		ic_ord.is_builtin = true;
		ic_ord.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".text\n"+
			"_ord:\n"+
			"lw $a0,12($fp)\n"+
			"lbu $v0,($a0)\n"+
			"j $ra\n"+
			"\n";
}

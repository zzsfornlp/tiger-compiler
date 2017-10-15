package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Print {
	static IcFun ic_print = new IcFun();
	
	/* type checking */
	static Dec_fun print;
	static{
		List<Type> l = new LinkedList<Type>();
		l.add(Type.THE_STRING);
		print = new Dec_fun(Zsymbol.symbol("print"),Type.THE_VOID,l);
		print.ic_fun = Print.ic_print;
	}
	
	/* ic */
	
	static{
		ic_print.name = "_print";
		ic_print.is_builtin = true;
		ic_print.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".text\n"+
			"_print:\n"+
			"lw $a0,12($fp)\n"+
			"li $v0,4\n"+
			"syscall\n"+
			"j $ra\n"+
			"\n";
}

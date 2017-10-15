package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Printi {
	static IcFun ic_printi = new IcFun();
	
	/* type checking */
	static Dec_fun printi;
	static{
		List<Type> l = new LinkedList<Type>();
		l.add(Type.THE_INT);
		printi = new Dec_fun(Zsymbol.symbol("printi"),Type.THE_VOID,l);
		printi.ic_fun = Printi.ic_printi;
	}
	
	/* ic */
	
	static{
		ic_printi.name = "_printi";
		ic_printi.is_builtin = true;
		ic_printi.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".text\n"+
			"_printi:\n"+
			"lw $a0,12($fp)\n"+
			"li $v0,1\n"+
			"syscall\n"+
			"j $ra\n"+
			"\n";
}
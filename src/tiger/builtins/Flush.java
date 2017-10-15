package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Flush {
	static IcFun ic_flush = new IcFun();
	
	/* type checking */
	static Dec_fun flush;
	static{
		List<Type> l = new LinkedList<Type>();
		flush = new Dec_fun(Zsymbol.symbol("flush"),Type.THE_VOID,l);
		flush.ic_fun = Flush.ic_flush;
	}
	
	/* ic */
	
	static{
		ic_flush.name = "_flush";
		ic_flush.is_builtin = true;
		ic_flush.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".text\n"+
			"_flush:\n"+
			"j $ra\n"+
			"\n";
}
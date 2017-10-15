package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Getchar {
	static IcFun ic_getchar = new IcFun();
	
	/* type checking */
	static Dec_fun getchar;
	static{
		List<Type> l = new LinkedList<Type>();
		getchar = new Dec_fun(Zsymbol.symbol("getchar"),Type.THE_STRING,l);
		getchar.ic_fun = Getchar.ic_getchar;
	}
	
	/* ic */
	
	static{
		ic_getchar.name = "_getchar";
		ic_getchar.is_builtin = true;
		ic_getchar.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".text\n"+
			"_getchar:\n"+
			"li $a0,2\n"+
			"li $v0,9\n"+
			"syscall\n"+
			"sb $zero,1($v0)\n"+
			//getchar
			"move $t8,$v0\n"+
			"move $a0,$v0\n"+
			"li $a1,2\n"+
			"li $v0,8\n"+
			"syscall\n"+
			"move $v0,$t8\n"+
			"j $ra\n"+
			"\n";
}

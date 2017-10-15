package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Exit {
	public static IcFun ic_exit = new IcFun();
	
	/* type checking */
	static Dec_fun exit;
	static{
		List<Type> l = new LinkedList<Type>();
		l.add(Type.THE_INT);
		exit = new Dec_fun(Zsymbol.symbol("exit"),Type.THE_VOID,l);
		exit.ic_fun = Exit.ic_exit;
	}
	
	/* ic */
	//exit is special because it's definately used with the end of _main
	
	static{
		ic_exit.name = "_exit";
		ic_exit.is_builtin = true;
		ic_exit.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".data\n"+
			"_exstr: .asciiz \"Exit with \"\n"+
			"_exstr2: .asciiz \"\\n\"\n"+
			".text\n"+
			"_exit:\n"+
			"li $v0,4\n"+
			"la $a0,_exstr2\n"+
			"syscall\n"+
			"li $v0,4\n"+
			"la $a0,_exstr\n"+
			"syscall\n"+
			"lw $a0,12($fp)\n"+
			"li $v0,1\n"+
			"syscall\n"+
			"li $v0,4\n"+
			"la $a0,_exstr2\n"+
			"syscall\n"+
			
			"li $v0,10\n"+
			"syscall\n"+
			"\n";
}

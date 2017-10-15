package tiger.builtins;

import tiger.ast.*;
import tiger.typing.*;
import tiger.symbol.*;
import tiger.ic.*;

import java.util.*;

public class Chr {
	static IcFun ic_chr = new IcFun();
	
	/* type checking */
	static Dec_fun chr;
	static{
		List<Type> l = new LinkedList<Type>();
		l.add(Type.THE_INT);
		chr = new Dec_fun(Zsymbol.symbol("chr"),Type.THE_STRING,l);
		chr.ic_fun = Chr.ic_chr;
	}
	
	/* ic */
	
	static{
		ic_chr.name = "_chr";
		ic_chr.is_builtin = true;
		ic_chr.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".data\n"+
			".text\n"+
			"_chr:\n"+
			"lw $a0,12($fp)\n"+
			//trucate it with 8 bits
			"andi $a1,$a0,255\n"+
			"li $a0,2\n"+
			"li $v0,9\n"+
			"syscall\n"+
			"sb $a1,($v0)\n"+
			"sb $zero,1($v0)\n"+
			"j $ra\n"+
			"\n";
}

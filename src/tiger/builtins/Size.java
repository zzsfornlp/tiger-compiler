package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Size {
	static IcFun ic_size = new IcFun();
	
	/* type checking */
	static Dec_fun size;
	static{
		List<Type> l = new LinkedList<Type>();
		l.add(Type.THE_STRING);
		size = new Dec_fun(Zsymbol.symbol("size"),Type.THE_INT,l);
		size.ic_fun = Size.ic_size;
	}
	
	/* ic */
	
	static{
		ic_size.name = "_size";
		ic_size.is_builtin = true;
		ic_size.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".text\n"+
			"_size:\n"+
			"lw $a0,12($fp)\n"+
			//calculate size
			"li $v0,0\n"+
			"_Lsize_size1:\n"+
			"lb $t9,($a0)\n"+
			"beq $t9,$zero,_Lsize_size1_out\n"+
			"addi $v0,$v0,1\n"+
			"addi $a0,$a0,1\n"+
			"j _Lsize_size1\n"+
			"_Lsize_size1_out:\n"+
			"j $ra\n"+
			"\n";
}
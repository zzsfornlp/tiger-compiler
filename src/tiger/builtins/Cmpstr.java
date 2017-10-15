package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Cmpstr {
	//the builtin's builtin function (not visible outside)
	public static IcFun ic_cmpstr = new IcFun();
	
	/* type checking */
	static Dec_fun cmpstr;
	static{
		List<Type> l = new LinkedList<Type>();
		l.add(Type.THE_STRING);
		l.add(Type.THE_STRING);
		cmpstr = new Dec_fun(Zsymbol.symbol("cmpstr"),Type.THE_INT,l);
		cmpstr.ic_fun = Cmpstr.ic_cmpstr;
	}
	
	/* ic */
		
	static{
		ic_cmpstr.name = "_cmpstr";
		ic_cmpstr.is_builtin = true;
		ic_cmpstr.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".text\n"+
			"_cmpstr:\n"+
			"lw $a0,12($fp)\n"+
			"lw $a1,16($fp)\n"+
			
			"_Lcmpstr_loop:\n"+
			"lb $t8,($a0)\n"+
			"lb $t9,($a1)\n"+
			"bne $t8,$t9,_Lcmpstr_ne\n"+
			"beq $t8,$zero,_Lcmpstr_0\n"+
			"add $a0,$a0,1\n"+
			"add $a1,$a1,1\n"+
			"j _Lcmpstr_loop\n"+
			
			"_Lcmpstr_ne:\n"+
			"blt $t8,$t9,_Lcmpstr_n1\n"+
			"j _Lcmpstr_p1\n"+
			
			"_Lcmpstr_0:\n"+
			"li $v0,0\n"+
			"j $ra\n"+
			"_Lcmpstr_n1:\n"+
			"li $v0,-1\n"+
			"j $ra\n"+
			"_Lcmpstr_p1:\n"+
			"li $v0,1\n"+
			"j $ra\n"+
			"\n";
}

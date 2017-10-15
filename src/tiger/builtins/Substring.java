package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Substring {
	static IcFun ic_substring = new IcFun();
	
	/* type checking */
	static Dec_fun substring;
	static{
		List<Type> l = new LinkedList<Type>();
		l.add(Type.THE_STRING);
		l.add(Type.THE_INT);
		l.add(Type.THE_INT);
		substring = new Dec_fun(Zsymbol.symbol("substring"),Type.THE_STRING,l);
		substring.ic_fun = Substring.ic_substring;
	}
	
	/* ic */
	
	static{
		ic_substring.name = "_substring";
		ic_substring.is_builtin = true;
		ic_substring.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".text\n"+
			"_substring:\n"+
			//load num
			"lw $a1,16($fp)\n"+
			"lw $a0,20($fp)\n"+
			//calculate size
			"addi $a0,$a0,1\n"+
			"li $v0,9\n"+
			"syscall\n"+
			"move $a3,$v0\n"+
			//load string addr
			"addi $a2,$a0,-1\n"+
			"lw $a0,12($fp)\n"+
			"add $a0,$a0,$a1\n"+
			//copy
			"li $t8,0\n"+
			"_substring_loop:\n"+
			"bge $t8,$a2,_substring_out\n"+
			"lbu $t9,($a0)\n"+
				//get out if end of string
			"beq $t9,$zero,_substring_out\n"+
			"addi $a0,$a0,1\n"+
			"addi $t8,$t8,1\n"+
			"sb $t9,($a3)\n"+
			"addi $a3,$a3,1\n"+
			"j _substring_loop\n"+
			//out
			"_substring_out:\n"+
			"sb $zero,($a3)\n"+
			"j $ra\n"+
			"\n";
			
			
			

}
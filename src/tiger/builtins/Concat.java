package tiger.builtins;

import tiger.ast.*;
import tiger.ic.IcFun;
import tiger.typing.*;
import tiger.symbol.*;

import java.util.*;

public class Concat {
	static IcFun ic_concat = new IcFun();
	
	/* type checking */
	static Dec_fun concat;
	static{
		List<Type> l = new LinkedList<Type>();
		l.add(Type.THE_STRING);
		l.add(Type.THE_STRING);
		concat = new Dec_fun(Zsymbol.symbol("concat"),Type.THE_STRING,l);
		concat.ic_fun = Concat.ic_concat;
	}
	
	/* ic */
	
	static{
		ic_concat.name = "_concat";
		ic_concat.is_builtin = true;
		ic_concat.belonged_fun = IcFun.global_fun;
	}
	
	public static String code =
			".text\n"+
			"_concat:\n"+
			"lw $a0,12($fp)\n"+
			"lw $a1,16($fp)\n"+
			//calculate size
			"li $t8,0\n"+
			"_Lconcat_size1:\n"+
			"lb $t9,($a0)\n"+
			"beq $t9,$zero,_Lconcat_size1_out\n"+
			"addi $t8,$t8,1\n"+
			"addi $a0,$a0,1\n"+
			"j _Lconcat_size1\n"+
			"_Lconcat_size1_out:\n"+
			
			"_Lconcat_size2:\n"+
			"lb $t9,($a1)\n"+
			"beq $t9,$zero,_Lconcat_size2_out\n"+
			"addi $t8,$t8,1\n"+
			"addi $a1,$a1,1\n"+
			"j _Lconcat_size2\n"+
			"_Lconcat_size2_out:\n"+
			"addi $t8,$t8,1\n"+
			//new one
			"move $a0,$t8\n"+
			"li $v0,9\n"+
			"syscall\n"+
			"move $v1,$v0\n"+
			//copy strings
			"lw $a0,12($fp)\n"+
			"lw $a1,16($fp)\n"+
			
			"_Lconcat_copy1:\n"+
			"lb $t9,($a0)\n"+
			"beq $t9,$zero,_Lconcat_copy1_out\n"+
			"sb $t9,($v1)\n"+
			"addi $a0,$a0,1\n"+
			"addi $v1,$v1,1\n"+
			"j _Lconcat_copy1\n"+
			"_Lconcat_copy1_out:\n"+
			
			"_Lconcat_copy2:\n"+
			"lb $t9,($a1)\n"+
			"beq $t9,$zero,_Lconcat_copy2_out\n"+
			"sb $t9,($v1)\n"+
			"addi $a1,$a1,1\n"+
			"addi $v1,$v1,1\n"+
			"j _Lconcat_copy2\n"+
			"_Lconcat_copy2_out:\n"+
			
			//over
			"sb $zero,($v1)\n"+
			"j $ra\n"+
			"\n";
			
			
			
			
}

package tiger.builtins;

import tiger.ast.*;
import tiger.typing.*;
import tiger.symbol.*;
import tiger.ic.*;
import java.util.*;

public class Builtins {
	/* type checking -- library declarations */
	private static void put(Table<Dec> v,String s,Dec d){
		v.put(Zsymbol.symbol(s), d);
	}
	public static void add_stdfunctions_dec(Table<Dec> v){
		put(v,"print",Print.print);
		put(v,"printi",Printi.printi);
		put(v,"flush",Flush.flush);
		put(v,"getchar",Getchar.getchar);
		put(v,"ord",Ord.ord);
		put(v,"chr",Chr.chr);
		put(v,"size",Size.size);
		put(v,"substring",Substring.substring);
		put(v,"concat",Concat.concat);
		put(v,"not",Not.not);
		put(v,"exit",Exit.exit);
	}
	/* ic */
	public static LinkedList<IcFun> std_IcFun_list = new LinkedList<IcFun>();
	static{
		std_IcFun_list.addAll(Arrays.asList(
				Print.ic_print,Printi.ic_printi,Flush.ic_flush,Getchar.ic_getchar,
				Ord.ic_ord,Chr.ic_chr,Size.ic_size,Substring.ic_substring,Concat.ic_concat,
				Not.ic_not,Exit.ic_exit));
		std_IcFun_list.add(Cmpstr.ic_cmpstr);
	}
	
	public static String codes = Print.code+Printi.code+Flush.code+Getchar.code+
			Ord.code+Chr.code+Size.code+Substring.code+Concat.code+
			Not.code+Exit.code+Cmpstr.code;
	static{
		if(tiger.compiler.Compiler.compiler_cg_verbose)
			codes = "# The Standard libraries #\n"+codes;
	}
}
package tiger.ic;

import java.util.*;

import tiger.builtins.*;
import tiger.errors.WarningList;
import tiger.ast.*;
import tiger.block.*;

public class IcFun {
	/* part1:ic-----------------------------------------------------------------*/
	/* fields */
	//basic info(adding)
	public LinkedList<IcVar_local> var_l = new LinkedList<IcVar_local>();
	public LinkedList<IcFun> funs = new LinkedList<IcFun>();
	public LinkedList<IcStmt> stmts = new LinkedList<IcStmt>();
	public LinkedList<IcStmt_label> labels = new LinkedList<IcStmt_label>();
	//basic info(creating)
	public String name;
	public LinkedList<IcVar_param> var_p = new LinkedList<IcVar_param>();
	public IcFun belonged_fun;		//in which function being declared		
	public boolean is_builtin = false;
	public int level = 0;
	public Dec_fun dec;
	
	/* static */
	//print
	public static void print_head(int n){
		for(int i=0;i<n;i++)
			System.out.print(" ");
	}
	public static void print_out(String x){
		System.out.print(x);
	}
	// the global string pool
	public static ArrayList<String> const_strings = new ArrayList<String>();
	public static String string_center;
	static public int add_str(String x){
		int k=const_strings.indexOf(x);
		if(k==-1){
			const_strings.add(x);
			return const_strings.size()-1;
		}
		else
			return k;
	}
	// the global one
	static public IcFun global_fun;
	static public IcFun main_fun;
	static{
		/* the global level */
		global_fun = new IcFun();
		global_fun.name = "_global";
		global_fun.funs.addAll(Builtins.std_IcFun_list);
		global_fun.level = -1;
		/* the main function */
		main_fun = new IcFun();
		main_fun.name = "main";
		global_fun.funs.add(main_fun);
		main_fun.belonged_fun = global_fun;
	}
	static public void ic_start_main(Expr program){
		//translate the program
		Object[] result = program.ic_trans(main_fun, null);
		//add a call to exit
		main_fun.stmts.addAll((LinkedList<IcStmt>)result[0]);
		IcStmt_call last = new IcStmt_call(main_fun,0,tiger.builtins.Exit.ic_exit);
		IcOper x = (IcOper)result[1];
		if(x==null)
			last.args.add(new IcConst_int(0));
		else
			last.args.add(x);
		main_fun.stmts.add(last);
		//order function's statement
		int num=0;
		for(IcStmt i:main_fun.stmts)
			i.num = ++num;
	}
	
	/* ic-print */
	public void print(int n){
		//function name
		print_head(n);
		print_out("Function:"+name+"{"+"\n");
		//parameters
		print_head(n+1);
		print_out("parameters:");
		for(IcVar_param i : var_p){
			print_out(i.name+",");
		}
		print_out("\n");
		//functions
		print_head(n+1);
		print_out("functions:"+"\n");
		for(IcFun i : funs){
			i.print(n+2);
		}
		//stmts
		print_head(n+1);
		print_out("statements:"+"\n");
		for(IcStmt i: stmts){
			i.print_basic(n+2);
		}
		//close
		print_head(n+1);
		print_out("}"+"\n");
	}
	
	/* part2:block-----------------------------------------------------------------*/
	public ArrayList<Block> block_l = new ArrayList<Block>();
	public void print_blocks(int n){
		//name
		print_head(n);
		print_out("Function:"+name+"{"+"\n");
		//parameters
		print_head(n+1);
		print_out("parameters:");
		for(IcVar_param i : var_p){
			print_out(i.name+",");
		}
		print_out("\n");
		//functions
		print_head(n+1);
		print_out("functions:"+"\n");
		for(IcFun i : funs){
			i.print_blocks(n+2);
		}
		//blocks
		print_head(n+1);
		print_out("blocks:"+"\n");
		for(Block i: block_l){
			i.print(n+2);
		}
		//close
		print_head(n+1);
		print_out("}"+"\n");		
	}
	
	/* part3: register allocation-----------------------------------------------------------------*/
	//all used variables
	public HashSet<IcVar> all_vars = new LinkedHashSet<IcVar>();
	public HashMap<IcVar,Integer> all_vars_reg = new HashMap<IcVar,Integer>();
	//the spilled temp var
	public ArrayList<IcVar_tmp> tmp_spill_l = new ArrayList<IcVar_tmp>();
	//the outside var local/param
	public ArrayList<IcVar> var_outside = new ArrayList<IcVar>();
	//the outside var local/param spilled
	//---no use //public ArrayList<IcVar> var_outside_spill = new ArrayList<IcVar>();
	//regs usage --- not initialed
	public boolean []reg_used;
	//public int reg_caller_save;//caluculate everytime calling
	public int reg_callee_save;
	
	public void print_reg(int n){
		//function name
		print_head(n);
		print_out("Function:"+name+"{"+"\n");
		//parameters
		print_head(n+1);
		print_out("parameters:");
		for(IcVar_param i : var_p){
			print_out(i.name+",");
		}
		print_out("\n");
		//functions
		print_head(n+1);
		print_out("functions:"+"\n");
		for(IcFun i : funs){
			i.print_reg(n+2);
		}
		//used reg
		print_head(n+1);
		print_out("used reg:"+Arrays.toString(reg_used)+"\n");
		//spilled tmp_var
		print_head(n+1);
		print_out("spilled tmp:"+tmp_spill_l+"\n");
		//outside var
		print_head(n+1);
		print_out("outside var:"+var_outside+"\n");
		//blocks
		print_head(n+1);
		print_out("blocks:"+"\n");
		for(Block i: block_l){
			i.print_reg(n+2);
		}
		//close
		print_head(n+1);
		print_out("}"+"\n");
	}
	
	/* part4:code generation-----------------------------------------------------------------*/
	public String function_code;	//function's code head, other in the stmts
	
	//all positive number
	//code generation info --- according to $fp
	public int pos_param=-3;	//reverse
	public int pos_local;
	public int pos_outside;
	public int pos_tmp_spilled;
	public int pos_sp;
}

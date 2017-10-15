package tiger.cg;

/* full of bugs, discard, see Codegen_utils_v2 */

import java.util.*;

import tiger.ic.*;
import tiger.errors.*;
import static tiger.cg.Codegen_constant.*;

@Deprecated
public class Codegen_utils {
	/* for s1 */
	static void Cg_check_function(IcFun f,HashMap<String,Integer> map){
		//check same name
		if(map.containsKey(f.name)){
			int k = map.get(f.name);
			map.put(f.name, k+1);
			f.name = f.name + (k+1);
			map.put(f.name,0);
		}
		else{
			map.put(f.name, 0);
		}
		//recursive
		for(IcFun i : f.funs)
			Cg_check_function(i,map);
	}
	/* for s3 */
	static String Cg_transform_str(String x){
		//state out \n \t \r
		StringBuilder tmp = new StringBuilder();
		for(int i=0;i<x.length();i++){
			char t = x.charAt(i);
			switch(t){
				case '\n': tmp.append("\\n"); break;
				case '\r': tmp.append("\\r"); break;
				case '\t': tmp.append("\\t"); break;
				default: tmp.append(t); break;
			}
		}
		return tmp.toString();
	}
	
	/* for s4 */
	/*
	@Deprecated
	static void Cg_gen_code(IcFun f){
		Cg_fcode(f);
		for(IcStmt i : f.stmts)
			Cg_scode(i);
		for(IcFun i : f.funs)
			Cg_gen_code(i);
	}
	@Deprecated
	static void Cg_scode(IcStmt i){
		i.gen_code();
	}
	*/
	
	/*
	@Deprecated
	static void Cg_fcode(IcFun f){
		//generate the fun's code's head and calculate sone info of function
		StringBuilder s = new StringBuilder();
		
		s.append(".text:\n");
		s.append(f.name+":\n");
		
		int count=0;
		for(int i=0;i<MIPS_CALLE_SAVE_REG;i++){
			if(f.reg_used[i]==true){
				//sw $tn $sp; $sp = $sp - MIPS_WORD_LENGTH
				s.append("sw "+MIPS_REGS[i]+",$sp\n");
				s.append("addi $sp,$sp,-4\n");
				count++;
			}
		}
		
		for(int i=0;i<f.var_p.size();i++){
			IcVar_param v = f.var_p.get(i);
			if(v.var_place>0){
				int off = v.fun.pos_param-i;
				s.append("lw "+MIPS_REGS[v.var_place-1]+","+off*4+"($fp)\n");
			}
		}
		
		f.pos_local = count+1;	//offset of the first local
		
		f.pos_outside = f.pos_local+f.var_l.size();
		//store the outside addr
		for(int i=0;i<f.var_outside.size();i++){
			IcVar v = f.var_outside.get(i);
			int level_up=f.level-v.fun.level;
			if(level_up<=0)
				throw new CgError("Fatal fault for variable" + v + "'s level");
			if(v instanceof IcVar_local){
				IcVar_local vv = (IcVar_local)v;
				int place = vv.fun.var_l.indexOf(vv);
				if(place==-1)
					throw new CgError("Fatal fault can't find local variable" + v);
				s.append("lw "+MIPS_T1+","+MIPS_ACCESS_LINK*4+"($fp)\n");
				while(level_up-->1){
					s.append("lw "+MIPS_T1+","+MIPS_ACCESS_LINK*4+"("+MIPS_T1+")\n");
				}
				int off=place+vv.fun.pos_local;
				s.append("addi "+MIPS_T1+","+MIPS_T1+","+off*4+"\n");
				int store_off=i+f.pos_outside;
				s.append("sw "+MIPS_T1+","+store_off*4+"($fp)\n");
				//store into reg if have one
				if(vv.var_place>0){
					s.append("lw "+MIPS_REGS[vv.var_place-1]+","+MIPS_T1+"\n");
				}
			}
			else if(v instanceof IcVar_param){
				IcVar_param vv = (IcVar_param)v;
				int place = vv.fun.var_p.indexOf(vv);
				if(place==-1)
					throw new CgError("Fatal fault can't find param variable " + v);
				s.append("lw "+MIPS_T1+","+MIPS_ACCESS_LINK*4+"($fp)\n");
				while(level_up-->1){
					s.append("lw "+MIPS_T1+","+MIPS_ACCESS_LINK*4+"("+MIPS_T1+")\n");
				}
				int off=-1*place+vv.fun.pos_param;
				s.append("addi "+MIPS_T1+","+MIPS_T1+","+off*4+"\n");
				int store_off=i+f.pos_outside;
				s.append("sw "+MIPS_T1+","+store_off*4+"($fp)\n");
				//store into reg if have one
				if(vv.var_place>0){
					s.append("lw "+MIPS_REGS[vv.var_place-1]+","+MIPS_T1+"\n");
				}
			}
			else
				throw new CgError("Fatal fault tmp can't be outside" + v);
		}
		
		//no use f.pos_out_spilled = f.pos_outside + f.var_outside.size();
		
		f.pos_tmp_spilled = f.pos_outside + f.var_outside.size();
		
		s.append("addi $sp,$sp,-"+(f.var_outside.size()+f.var_l.size()+f.tmp_spill_l.size())*4+"\n");
		
		f.function_code = s.toString();
	}*/
	
	/* mips-t1 = mips-t1 op mips-t2 */
	/*
	@Deprecated
	public static String Cg_solve_oper(IcOper oper,StringBuilder s,int place){
		if(oper instanceof IcConst_int){
			String result = (place==1)?MIPS_T1:MIPS_T2;
			s.append("li "+result+","+((IcConst_int)oper).num+"\n");
			return result;
		}
		else if(oper instanceof IcConst_str){
			String result = (place==1)?MIPS_T1:MIPS_T2;
			s.append("la "+result+","+str_prefix+((IcConst_str)oper).num+"\n");
			return result;
		}
		
		//IcVar
		IcVar v = (IcVar)oper;
		if(v.var_place > 0)
			return MIPS_REGS[v.var_place-1];
		else{
			//spilled tmp
			int i;
			if(v instanceof IcVar_tmp){
				if((i=v.fun.tmp_spill_l.indexOf(v))>=0){
					String result = (place==1)?MIPS_T1:MIPS_T2;
					int off = v.fun.pos_tmp_spilled+i;
					s.append("lw "+result+","+off*4+"($fp)\n");
					return result;
				}
				else
					throw new CgError("Fatal fault can't find spilled tmp" + v);
			}
			//local var
			else if((i=v.fun.var_l.indexOf(v))>=0){
				String result = (place==1)?MIPS_T1:MIPS_T2;
				int off = v.fun.pos_local+i;
				s.append("lw "+result+","+off*4+"($fp)\n");
				return result;
			}
			//param
			else if((i=v.fun.var_p.indexOf(v))>=0){
				String result = (place==1)?MIPS_T1:MIPS_T2;
				int off = v.fun.pos_param-i;
				s.append("lw "+result+","+off*4+"($fp)\n");
				return result;
			}
			//outside var
			else if((i=v.fun.var_outside.indexOf(v))>=0){
				String result = (place==1)?MIPS_T1:MIPS_T2;
				int off = v.fun.pos_outside+i;
				s.append("lw "+MIPS_T1+","+off*4+"($fp)\n");
				s.append("lw "+MIPS_T1+","+"("+MIPS_T1+")\n");
				return result;
			}
			else
				throw new CgError("Fatal fault can't find outside var" + v);
		}
	}
	
	@Deprecated
	public static void Cg_writeback(IcVar v,String reg,StringBuilder s){
		//need to write back if is local or param
		int i;
		if(v instanceof IcVar_tmp){
			if(v.var_place==0){
				i=v.fun.tmp_spill_l.indexOf(v);
				int off = v.fun.pos_tmp_spilled+i;
				s.append("lw "+reg+","+off*4+"($fp)\n");
			}
		}
		else{
			//outside var
			if((i=v.fun.var_outside.indexOf(v))>=0){
				int off = v.fun.pos_outside+i;
				s.append("lw "+MIPS_T1+","+off*4+"($fp)\n");
				s.append("sw "+reg+","+"("+MIPS_T1+")\n");
			}
			//param
			else if((i=v.fun.var_p.indexOf(v))>=0){
				int off = v.fun.pos_param-i;
				s.append("sw "+reg+","+off*4+"($fp)\n");
			}
			//local
			else if((i=v.fun.var_l.indexOf(v))>=0){
				int off = v.fun.pos_local+i;
				s.append("sw "+reg+","+off*4+"($fp)\n");
			}
			else
				throw new CgError("Fatal fault don't know where to store var" + v);
		}
	}
	*/
}




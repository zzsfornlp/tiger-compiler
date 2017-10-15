package tiger.cg;

import java.util.*;

import tiger.ic.*;
import tiger.errors.*;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;

public class Codegen_utils_v2 {
	//forbidden name
	static HashSet<String> forbidden_name = new HashSet<String>();
	static{
		forbidden_name.add("b");
	}
	/* for s1 */
	static void Cg_check_function(IcFun f,HashMap<String,Integer> map){
		//check for forbiden name
		while(forbidden_name.contains(f.name))
			f.name = f.name+"z";
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
	static void Cg_gen_mips(IcFun f){
		Cg_fcode_mips(f);
		for(IcStmt i : f.stmts)
			Cg_scode_mips(i);
		for(IcFun i : f.funs)
			Cg_gen_mips(i);
	}
	static void Cg_scode_mips(IcStmt i){
		/* dispatch to the detailed statement */
		i.gen_mips();
	}
	static void Cg_fcode_mips(IcFun f){
		//generate the fun's code's head and calculate sone info of function
		StringBuilder s = new StringBuilder();
		/* 0.start */
		if(f==IcFun.main_fun){
			if(compiler_cg_verbose)
				s.append("# The Main Function #\n");
		}
		/* 1.function name */
		s.append(".text\n");
		s.append(f.name+":\n");
		/* 1.5 if main fun, adjust the $fp */
		if(f==IcFun.main_fun){
			if(compiler_cg_verbose)
				s.append("##adjust $fp for main function\n");
			s.append(Mips_addi("$fp","$sp",4));
		}
		
		/* 2.calculate the $sp position */
		//callee save regs
		if(f==IcFun.main_fun){
			f.reg_callee_save = 0;
		}
		else{
			f.reg_callee_save = 0;
			for(int i=0;i<MIPS_CALLE_SAVE_REG;i++){
				if(f.reg_used[i]==true){
					f.reg_callee_save++;
				}
			}
		}
		//local variables
		f.pos_local = 1 + f.reg_callee_save;
		//outside var addr
		f.pos_outside = f.pos_local + f.var_l.size();
		//tmp var addr
		f.pos_tmp_spilled = f.pos_outside + f.var_outside.size();
		//position of $sp
		f.pos_sp = f.pos_tmp_spilled + f.tmp_spill_l.size();
		//adjust $sp
		if(compiler_cg_verbose)
			s.append("##adjust $sp for the function\n");
		s.append(Mips_addi("$sp","$fp",-4*(f.pos_sp)));
		
		/* 3.callee save regs */
		if(compiler_cg_verbose)
			s.append("##callee-save for the function, num is "+f.reg_callee_save+"\n");
		if(f != IcFun.main_fun){
			int count=0;
			for(int i=0;i<MIPS_CALLE_SAVE_REG;i++){
				if(f.reg_used[i]==true){
					s.append(Mips_sw(MIPS_REGS[i],"$fp",-4*(count+1)));
					count++;
				}
			}
		}
		
		/* 4.load the parameters */
		if(compiler_cg_verbose)
			s.append("##load parameters which have get allocated register\n");
		for(int i=0;i<f.var_p.size();i++){
			IcVar_param v = f.var_p.get(i);
			if(f.all_vars_reg.containsKey(v) && f.all_vars_reg.get(v)>0){
				int off = -1*(v.fun.pos_param-i);
				s.append(Mips_lw(MIPS_REGS[f.all_vars_reg.get(v)-1],"$fp",off*4));
			}
		}
		
		/* 5.store the outside addr and load them if get reg */
		if(compiler_cg_verbose)
			s.append("##get outside var's addr and load those have get allocated register\n");
		for(int i=0;i<f.var_outside.size();i++){
			IcVar v = f.var_outside.get(i);
			int level_up=f.level-v.fun.level;
			if(level_up<=0)
				throw new CgError("Fatal fault for variable" + v + "'s level");
			if(compiler_cg_verbose)
				s.append("#for outside var "+v+"\n");
			
			//search access link
			s.append(Mips_lw(MIPS_T1,"$fp",-4*MIPS_ACCESS_LINK));
			while(level_up-->1){
				s.append(Mips_lw(MIPS_T1,MIPS_T1,-4*MIPS_ACCESS_LINK));
			}
			//get the place
			if(v instanceof IcVar_local){
				IcVar_local vv = (IcVar_local)v;
				int place = vv.fun.var_l.indexOf(vv);
				if(place==-1)
					throw new CgError("Fatal fault can't find local variable" + v);
				int off=place+vv.fun.pos_local;
				s.append(Mips_addi(MIPS_T1,MIPS_T1,off*-4));
				int store_off=i+f.pos_outside;
				s.append(Mips_sw(MIPS_T1,"$fp",store_off*-4));
				//store into reg if have one
				if(f.all_vars_reg.get(vv)>0){
					s.append(Mips_lw(MIPS_REGS[f.all_vars_reg.get(vv)-1],MIPS_T1,0));
				}
			}
			else if(v instanceof IcVar_param){
				IcVar_param vv = (IcVar_param)v;
				int place = vv.fun.var_p.indexOf(vv);
				if(place==-1)
					throw new CgError("Fatal fault can't find param variable " + v);
				int off=-1*place+vv.fun.pos_param;
				s.append(Mips_addi(MIPS_T1,MIPS_T1,off*-4));
				int store_off=i+f.pos_outside;
				s.append(Mips_sw(MIPS_T1,"$fp",store_off*-4));
				//store into reg if have one
				if(f.all_vars_reg.get(vv)>0){
					s.append(Mips_lw(MIPS_REGS[f.all_vars_reg.get(vv)-1],MIPS_T1,0));
				}
			}
			else
				throw new CgError("Fatal fault tmp can't be outside" + v);
		}
		
		/* 6.finish */
		if(compiler_cg_verbose){
			s.append("##finish function header,the summury\n");
			s.append("#callee-reg:"+f.reg_callee_save+",local_var:"+f.var_l.size()
					+",outside_var:"+f.var_outside.size()
					+",tmp_spilled:"+f.tmp_spill_l.size()+"\n");
			s.append("##start stmts...\n");
		}
		f.function_code = s.toString();
	}
	
	//maybe not efficient, but easy to get...
	public static String Cg_solve_oper(IcOper oper,StringBuilder s,int place,IcFun f){
		if(oper instanceof IcConst_int){
			String result = (place==1)?MIPS_T1:MIPS_T2;
			s.append(Mips_li(result,((IcConst_int)oper).num));
			return result;
		}
		else if(oper instanceof IcConst_str){
			String result = (place==1)?MIPS_T1:MIPS_T2;
			s.append(Mips_la(result,str_prefix+((IcConst_str)oper).num));
			return result;
		}
		
		IcVar var = (IcVar)oper;
		if(f.all_vars_reg.get(var) > 0)
			return MIPS_REGS[f.all_vars_reg.get(var)-1];
		else{
			//spilled tmp
			int i;
			if(var instanceof IcVar_tmp){
				if((i=f.tmp_spill_l.indexOf(var))>=0){
					String result = (place==1)?MIPS_T1:MIPS_T2;
					int off = f.pos_tmp_spilled+i;
					s.append(Mips_lw(result,"$fp",-4*off));
					return result;
				}
				else
					throw new CgError("Fatal fault can't find spilled tmp" + var);
			}
			//local var
			else if((i=f.var_l.indexOf(var))>=0){
				String result = (place==1)?MIPS_T1:MIPS_T2;
				int off = f.pos_local+i;
				s.append(Mips_lw(result,"$fp",-4*off));
				return result;
			}
			//param
			else if((i=f.var_p.indexOf(var))>=0){
				String result = (place==1)?MIPS_T1:MIPS_T2;
				int off = f.pos_param-i;
				s.append(Mips_lw(result,"$fp",-4*off));
				return result;
			}
			//outside var
			else if((i=f.var_outside.indexOf(var))>=0){
				String result = (place==1)?MIPS_T1:MIPS_T2;
				int off = f.pos_outside+i;
				s.append(Mips_lw(result,"$fp",-4*off));
				s.append(Mips_lw(result,result,0));
				return result;
			}
			else
				throw new CgError("Fatal fault can't find outside var" + var);
		}
	}
	
	public static void Cg_write_var(IcVar v,String reg,StringBuilder s,IcFun f){
		//need to write back if is local or param or outside or spilled_tmp
		int i;
		if(v instanceof IcVar_tmp){
			if(f.all_vars_reg.get(v)==0){
				i=v.fun.tmp_spill_l.indexOf(v);
				if(i<0)
					throw new CgError("Fatal fault can't find spilled tmp " + v);
				int off = f.pos_tmp_spilled+i;
				s.append(Mips_sw(reg,"$fp",-4*off));
			}
		}
		else{
			//outside var
			if((i=f.var_outside.indexOf(v))>=0){
				int off = f.pos_outside+i;
				s.append(Mips_lw(MIPS_T1,"$fp",-4*off));
				s.append(Mips_sw(reg,MIPS_T1,0));
			}
			//param
			else if((i=f.var_p.indexOf(v))>=0){
				int off = -1*(f.pos_param-i);
				s.append(Mips_sw(reg,"$fp",-4*off));
			}
			//local
			else if((i=f.var_l.indexOf(v))>=0){
				int off = f.pos_local+i;
				s.append(Mips_sw(reg,"$fp",-4*off));
			}
			else
				throw new CgError("Fatal fault don't know where to store var" + v);
		}
	}
}

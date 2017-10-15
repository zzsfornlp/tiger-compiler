package tiger.ic;

import static tiger.ic.IcFun.print_head;
import static tiger.ic.IcFun.print_out;
import static tiger.cg.Codegen_constant.*;
import static tiger.cg.Codegen_utils_v2.*;
import static tiger.cg.Mips.*;
import static tiger.compiler.Compiler.compiler_cg_verbose;
import tiger.errors.*;
import tiger.block.*;

import java.util.*;
public class IcStmt_call extends IcStmt{
	public IcFun called;
	public ArrayList<IcOper> args = new ArrayList<IcOper>();
	public IcStmt_call(IcFun f,int n,IcFun fc){
		super(f,n);
		called = fc;
	}
	
	public String toString(){
		StringBuilder tmp = new StringBuilder();
		tmp.append(num+":"+"call function "+called.name+" with:");
		int size = args.size()-1;
		for(int i=0;i<size;i++){
			tmp.append(args.get(i).toString());
			tmp.append((i==size-1)?" ":",");
		}
		return tmp.toString();
	}
	
	/* code generation */
	public void gen_mips(){
		StringBuilder s = new StringBuilder();
		if(compiler_cg_verbose)
			s.append("#"+"call function "+called.name+"with "+args.size()+" args\n");
		/* 1.save regs --- caller save <skip for builtins> */
		if(compiler_cg_verbose)
			s.append("#save caller-save regs\n");
		int count=0;
		if(!called.is_builtin){
			for(int i=MIPS_CALLE_SAVE_REG;i<MIPS_ALL_REGS;i++){
				if(this.fun.reg_used[i] && called.reg_used[i]){
					//both use caller-save-reg
					s.append(Mips_sw(MIPS_REGS[i],"$sp",-4*count));
					count++;
				}
			}
		}
		/* 2.store parameters */
		if(compiler_cg_verbose)
			s.append("#save parameters(num is "+args.size()+")\n");
		int where=0;
		for(int i=args.size()-1;i>=0;i--){
			String x = Cg_solve_oper(args.get(i),s,1,fun);
			//save args reverse
			s.append(Mips_sw(x,"$sp",-4*(count+where)));
			where++;
		}
		/* 3.store return address  */
		if(compiler_cg_verbose)
			s.append("#save return address\n");
		int the_ra_off = count + args.size();
		s.append(Mips_sw("$ra","$sp",-4*the_ra_off));
		/* 4. acess link <skip for builtins> */
		if(!called.is_builtin){
			if(compiler_cg_verbose)
				s.append("#save access link\n");
			int level_up = fun.level-called.level;
			if(level_up<-1)
				throw new CgError("Fatal fault for function-calling " + called + "'s level");
			s.append(Mips_move(MIPS_T1,"$fp"));
			while(level_up-->-1)
				s.append(Mips_lw(MIPS_T1,MIPS_T1,MIPS_ACCESS_LINK*-4));
			s.append(Mips_sw(MIPS_T1,"$sp",-4*(the_ra_off+1)));
		}
		/* 5.fp */
		if(compiler_cg_verbose)
			s.append("#save $fp and move $sp and call\n");
		s.append(Mips_sw("$fp","$sp",-4*(the_ra_off+2)));
		s.append(Mips_addi("$sp","$sp",-4*(the_ra_off+3)));
		s.append(Mips_addi("$fp","$sp",4));
		/* 6.call */
		s.append(Mips_jal(called.name));
		/* 7.clean up --- $sp point to saved $fp-4 */
		if(compiler_cg_verbose)
			s.append("#return and clean up\n");
		s.append(Mips_lw("$ra","$sp",3*4));
		s.append(Mips_lw("$fp","$sp",4));
		s.append(Mips_addi("$sp","$sp",4*(the_ra_off+3)));
		count=0;
		if(!called.is_builtin){
			for(int i=MIPS_CALLE_SAVE_REG;i<MIPS_ALL_REGS;i++){
				if(this.fun.reg_used[i] && called.reg_used[i]){
					//both use caller-save-reg
					s.append(Mips_lw(MIPS_REGS[i],"$sp",-4*count));
					count++;
				}
			}
		}
		/* 7.5: reload vars --- after call <skip for builtins> */
		if(!called.is_builtin){
			if(compiler_cg_verbose)
				s.append("#reload living variables if any\n");
			Block b = this.block_belonged;
			int index = b.stmts.indexOf(this);
			HashSet<IcVar> live = null;
			if(index == b.stmts.size()-1)	//last one
				live = b.out;
			else	//next one
				live = b.stmts.get(index+1).live_vars;
			//reload
			for(IcVar v : live){
				int reg=0;
				if((reg=fun.all_vars_reg.get(v))==0)//no regs allocated
					continue;
				int i = 0;
				//outside --- always load
				if((i=fun.var_outside.indexOf(v))>=0){
					int off = fun.pos_outside+i;
					s.append(Mips_lw(MIPS_T1,"$fp",-4*off));
					s.append(Mips_lw(MIPS_REGS[reg-1],MIPS_T1,0));
				}
				else if(called.level > this.fun.level){
					//param --- depends
					if((i=fun.var_p.indexOf(v))>=0){
						int off = fun.pos_param-i;
						s.append(Mips_lw(MIPS_REGS[reg-1],"$fp",-4*off));
					}
					//local --- depends
					else if((i=fun.var_l.indexOf(v))>=0){
						int off = fun.pos_local+i;
						s.append(Mips_lw(MIPS_REGS[reg-1],"$fp",-4*off));
					}
				}
				//skip others
			}
		}
		
		stmt_code = s.toString();
	}
}

package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.symbol.*;
import tiger.typing.*;
import tiger.ic.*;
import tiger.builtins.*;

/* use oprator's position */
public class Expr_op extends Expr{
	public final static int PLUS=0, MINUS=1, MUL=2, DIV=3,
		    EQ=4, NE=5, LT=6, LE=7, GT=8, GE=9, AND=10, OR=11;
	public Expr left,right;
	public int op;
	public Expr_op(int li,int c,int o,Expr l,Expr r){
		line = li;
		column = c;
		op = o;
		left = l;
		right = r;
	}
	public boolean is_lazy(){
		return (op==AND)||(op==OR);
	}
	
	/* the type checking */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		Type l = left.type_check(t_env, v_env, false, e);
		Type r = right.type_check(t_env, v_env, false, e);
		switch(op){
		case PLUS: case MINUS: case MUL: case DIV: case AND: case OR:
			if(!(l.actual() instanceof INT)){
				e.add_error("Type of left-side for binary-op should be integer", left.line, left.column, "typing");
			}
			if(!(r.actual() instanceof INT)){
				e.add_error("Type of right-side for binary-op should be integer", right.line, right.column, "typing");
			}
			break;
		case LT: case LE: case GT: case GE:
			if(!(l.actual() instanceof INT) && !(l.actual() instanceof STRING)){
				e.add_error("Type of left-side for binary-op should be integer or string", left.line, left.column, "typing");
			}
			if(!(r.actual() instanceof INT) && !(r.actual() instanceof STRING)){
				e.add_error("Type of right-side for binary-op should be integer or string", right.line, right.column, "typing");
			}
			if(!(l.coerceTo(r))){
				e.add_error("Type of sides for binary-op do not match", line, column, "typing");
			}
			break;
		default:
			if((l.actual() instanceof NIL)&&(r.actual() instanceof NIL)){
				e.add_error("Type of sides for binary-op can't be both nil", line, column, "typing");
			}
			else if(!(l.coerceTo(r))){
				e.add_error("Type of sides for binary-op do not match", line, column, "typing");
			}
			break;
		}
		the_type = Type.THE_INT;
		return the_type;
	}
	
	/* ic */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		
		/* 1.calculate the left,right */
		Object[] result1 = left.ic_trans(f, break_to);
		LinkedList<IcStmt> code1 = (LinkedList<IcStmt>)result1[0];
		IcOper r1 = (IcOper)result1[1];
		Object[] result2 = right.ic_trans(f, break_to);
		LinkedList<IcStmt> code2 = (LinkedList<IcStmt>)result2[0];
		IcOper r2 = (IcOper)result2[1];
		/* 2.tmp */
		IcVar_tmp t = IcVar_tmp.new_one(f);
		/* 3.dispatch */
		switch(op){
		case PLUS: case MINUS: case MUL: case DIV:
			/* t = r1 op r2 */
			codes.addAll(code1);
			codes.addAll(code2);
			codes.add(new IcStmt_op(f,0,t,r1,r2,op));
			break;
		case LT: case LE: case GT: case GE: case EQ: case NE:
			if(left.the_type.coerceTo(Type.THE_STRING)){
				/* t' = cmpstr(s1,s2); //-1,0,1
				 * t = t' ...
				 */
				/* 3.2.1 cmpstr */
				IcVar_tmp t_cmp = IcVar_tmp.new_one(f);
				LinkedList<IcOper> arg_list = new LinkedList<IcOper>();
				arg_list.add(r1);
				arg_list.add(r2);
				IcStmt_callback call = new IcStmt_callback(f,0,Cmpstr.ic_cmpstr,t_cmp);
				call.args.addAll(arg_list);
				codes.add(call);
				/* 3.2.2 check */
				switch(op){
				case LT:	codes.add(new IcStmt_cmp(f,0,t,t_cmp,new IcConst_int(-1),IcStmt_cmp.EQ)); break;
				case LE:	codes.add(new IcStmt_cmp(f,0,t,t_cmp,new IcConst_int(1),IcStmt_cmp.NE)); break;
				case GT:	codes.add(new IcStmt_cmp(f,0,t,t_cmp,new IcConst_int(1),IcStmt_cmp.EQ)); break;
				case GE:	codes.add(new IcStmt_cmp(f,0,t,t_cmp,new IcConst_int(-1),IcStmt_cmp.NE)); break;
				case EQ:	codes.add(new IcStmt_cmp(f,0,t,t_cmp,new IcConst_int(0),IcStmt_cmp.EQ)); break;
				case NE:	codes.add(new IcStmt_cmp(f,0,t,t_cmp,new IcConst_int(0),IcStmt_cmp.NE)); break;
				default:	throw new RuntimeException("Not valid op in string cmp --- typecheck fail"+op);
				}
			}
			else{
				/* t = r1 op r2 */
				codes.addAll(code1);
				codes.addAll(code2);
				codes.add(new IcStmt_cmp(f,0,t,r1,r2,op));
			}
			break;
		//and or --- short-cut
		case AND: 
			/* branch L1 if s1==0
			 * s2
			 * t = (s2<>0)
			 * goto L2
			 * L1
			 * t = 0
			 * L2
			 */
			/* 1.L1(not now) & L2(not add now) */
			IcStmt_label the_l1 = IcStmt_label.new_one(f, 0);
			f.labels.add(the_l1);
			IcStmt_label the_l2 = IcStmt_label.new_one(f, 0);
			f.labels.add(the_l2);
			/* 2.s1,branch */
			codes.addAll(code1);
			codes.add(new IcStmt_branch(f,0,r1,new IcConst_int(0),the_l1,IcStmt_branch.EQ));
			/* 3.s2... */
			codes.addAll(code2);
			codes.add(new IcStmt_cmp(f,0,t,r2,new IcConst_int(0),IcStmt_cmp.NE));
			codes.add(new IcStmt_goto(f,0,the_l2));
			/* 4.labels */
			codes.add(the_l1);
			codes.add(new IcStmt_assign(f,0,t,new IcConst_int(0)));
			codes.add(the_l2);
			break;
		case OR:
			/* branch L1 if s1<>0
			 * s2
			 * t = (s2<>0)
			 * goto L2
			 * L1
			 * t = 1
			 * L2
			 */
			/* 1.L1(not now) & L2(not add now) */
			IcStmt_label the_ll1 = IcStmt_label.new_one(f, 0);
			f.labels.add(the_ll1);
			IcStmt_label the_ll2 = IcStmt_label.new_one(f, 0);
			f.labels.add(the_ll2);
			/* 2.s1,branch */
			codes.addAll(code1);
			codes.add(new IcStmt_branch(f,0,r1,new IcConst_int(0),the_ll1,IcStmt_branch.NE));
			/* 3.s2... */
			codes.addAll(code2);
			codes.add(new IcStmt_cmp(f,0,t,r2,new IcConst_int(0),IcStmt_cmp.NE));
			codes.add(new IcStmt_goto(f,0,the_ll2));
			/* 4.labels */
			codes.add(the_ll1);
			codes.add(new IcStmt_assign(f,0,t,new IcConst_int(1)));
			codes.add(the_ll2);
			break;
		default:
			throw new RuntimeException("Not valid op"+op);
		}
		
		return new Object[]{codes,t};
	}
}

	
	
	
package tiger.ast;

import java.util.LinkedList;

import tiger.errors.ErrorList;
import tiger.ic.IcFun;
import tiger.ic.IcOper;
import tiger.ic.IcStmt;
import tiger.ic.IcStmt_label;
import tiger.symbol.*;
import tiger.typing.*;

public class Expr_seq extends Expr{
	public Listseq_expr list;	/* optional */
	public Expr_seq(int l,int c,Listseq_expr li){
		line = l;
		column = c;
		list = li;
	}
	
	/* type checking & whether in loop */
	public Type type_check(Table<Type> t_env,Table<Dec> v_env,boolean in_loop,ErrorList e){
		if(list==null){
			the_type = Type.THE_VOID;
			return the_type;
		}
		else{
			Type tmp=null;
			for(Expr ee:list.list)
				tmp = ee.type_check(t_env, v_env, in_loop, e);
			if(tmp==null){
				the_type = Type.THE_VOID;
				return the_type;
			}
			else{
				the_type = tmp;
				return the_type;
			}
		}
	}
	
	/* lc */
	public Object[] ic_trans(IcFun f,IcStmt_label break_to){
		LinkedList<IcStmt> codes = new LinkedList<IcStmt>();
		IcOper r = null;
		
		/* calculate body */
		Object[] result=new Object[]{null,null};
		if(list!=null){
			for(Expr ee:list.list){
				result = ee.ic_trans(f, break_to);
				codes.addAll((LinkedList<IcStmt>)result[0]);
			}
		}
		//result is the last one
		r = (IcOper)result[1];
		return new Object[]{codes,r};
	}
}

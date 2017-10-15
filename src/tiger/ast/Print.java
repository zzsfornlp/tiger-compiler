package tiger.ast;

public class Print {

  java.io.PrintStream out;

  public Print(java.io.PrintStream o) {out=o;}

  void indent(int d) {
      for(int i=0; i<d; i++) 
            out.print(' ');
  }

  void say(String s) {
            out.print(s);
  }

  void say(int i) {
    Integer local = new Integer(i);
    out.print(local.toString());
  }

  void say(boolean b) {
    Boolean local = new Boolean(b);
    out.print(local.toString());
  }

  void sayln(String s) {
	say(s); say("\n");
  }

  void prVar(Var_id v, int d) {
    say("SimpleVar("); say(v.sym.toString()); say(")");
  }
  
  void prVar(Var_field v, int d) {
    sayln("FieldVar(");
    prVar(v.var, d+1); sayln(",");
    indent(d+1); say(v.sym.toString()); say(")");
  }

  void prVar(Var_index v, int d) {
    sayln("SubscriptVar(");
    prVar(v.var, d+1); sayln(",");
    prExp(v.expr, d+1); say(")");
  }

  /* Print A_var types. Indent d spaces. */
  void prVar(Var v, int d) {
    indent(d);
    if (v instanceof Var_id) prVar((Var_id) v, d);
    else if (v instanceof Var_field) prVar((Var_field) v, d);
    else if (v instanceof Var_index) prVar((Var_index) v, d);
    else throw new Error("Print.prVar");
  }
  
  void prExp(Expr_op e, int d) {
    sayln("OpExp(");
    indent(d+1); 
    switch(e.op) {
    case Expr_op.PLUS: say("PLUS"); break;
    case Expr_op.MINUS: say("MINUS"); break;
    case Expr_op.MUL: say("MUL"); break;
    case Expr_op.DIV: say("DIV"); break;
    case Expr_op.EQ: say("EQ"); break;
    case Expr_op.NE: say("NE"); break;
    case Expr_op.LT: say("LT"); break;
    case Expr_op.LE: say("LE"); break;
    case Expr_op.GT: say("GT"); break;
    case Expr_op.GE: say("GE"); break;
    case Expr_op.AND: say("AND"); break;
    case Expr_op.OR: say("OR"); break;
    default:
      throw new Error("Print.prExp.OpExp");
    }
    sayln(",");
    prExp(e.left, d+1); sayln(",");
    prExp(e.right, d+1); say(")");
  }

  void prExp(Expr_var e, int d) {
    sayln("varExp("); prVar(e.value, d+1);
    say(")");
  }
  
  void prExp(Expr_neg e, int d) {
	    sayln("negExp("); indent(d+1); prExp(e.expr, d+1);
	    say(")");
	  }

  void prExp(Expr_nil e, int d) {
    say("NilExp()");
  }

  void prExp(Expr_int e, int d) {
    say("IntExp("); say(e.value); say(")");
  }

  void prExp(Expr_str e, int d) {
    say("StringExp("); say(e.value); say(")");
  }

  void prExp(Expr_app e, int d) {
    say("CallExp("); say(e.function.toString()); sayln(",");
    prExplist(e.list, d+1); say(")");
  }

  void prExp(Expr_struct e, int d) {
    say("RecordExp("); say(e.id.toString());  sayln(",");
    prFieldExpList(e.list, d+1); say(")");
  }

  void prExp(Expr_seq e, int d) {
    sayln("SeqExp(");
    prExpSeq(e.list, d+1); say(")");
  }

  void prExp(Expr_assign e, int d) {
    sayln("AssignExp(");
    prVar(e.var, d+1); sayln(",");
    prExp(e.expr, d+1); say(")");
  }
  
  void prExp(Expr_ifelse e, int d) {
    sayln("IfElseExp(");
    prExp(e.e1, d+1); sayln(",");
    prExp(e.e2, d+1);
    sayln(",");
    prExp(e.e3, d+1);
    say(")");
  }
  
  void prExp(Expr_ifthen e, int d) {
	    sayln("IfThenExp(");
	    prExp(e.e1, d+1); sayln(",");
	    prExp(e.e2, d+1);
	    
	    say(")");
	  }

  void prExp(Expr_while e, int d) {
    sayln("WhileExp(");
    prExp(e.e1, d+1); sayln(",");
    prExp(e.e2, d+1); sayln(")");
  }

  void prExp(Expr_for e, int d) {
    sayln("ForExp("); 
    indent(d+1); say(e.sym.toString()); sayln(",");
    prExp(e.e1, d+1); sayln(",");
    prExp(e.e2, d+1); sayln(",");
    prExp(e.e3, d+1); say(")");
  }

  void prExp(Expr_break e, int d) {
    say("BreakExp()");
  }

  void prExp(Expr_let e, int d) {
    say("LetExp("); sayln("");
    prDecList(e.dec, d+1); sayln(",");
    prExpSeq(e.seq, d+1); say(")");
  }

  void prExp(Expr_array e, int d) {
    say("ArrayExp("); say(e.id.toString()); sayln(",");
    prExp(e.num, d+1); sayln(",");
    prExp(e.value, d+1); say(")");
  }

  /* Print Exp class types. Indent d spaces. */
  public void prExp(Expr e, int d) {
    indent(d);
    if (e instanceof Expr_op) prExp((Expr_op)e, d);
    else if (e instanceof Expr_var) prExp((Expr_var) e, d);
    else if (e instanceof Expr_nil) prExp((Expr_nil) e, d);
    else if (e instanceof Expr_int) prExp((Expr_int) e, d);
    else if (e instanceof Expr_str) prExp((Expr_str) e, d);
    else if (e instanceof Expr_app) prExp((Expr_app) e, d);
    else if (e instanceof Expr_struct) prExp((Expr_struct) e, d);
    else if (e instanceof Expr_seq) prExp((Expr_seq) e, d);
    else if (e instanceof Expr_assign) prExp((Expr_assign) e, d);
    else if (e instanceof Expr_ifthen) prExp((Expr_ifthen) e, d);
    else if (e instanceof Expr_ifelse) prExp((Expr_ifelse) e, d);
    else if (e instanceof Expr_while) prExp((Expr_while) e, d);
    else if (e instanceof Expr_for) prExp((Expr_for) e, d);
    else if (e instanceof Expr_break) prExp((Expr_break) e, d);
    else if (e instanceof Expr_let) prExp((Expr_let) e, d);
    else if (e instanceof Expr_array) prExp((Expr_array) e, d);
    else if (e instanceof Expr_neg) prExp((Expr_neg)e, d);
    else throw new Error("Print.prExp");
  }

  void prDec(Dec_fun d, int i) {
    say("FunctionDec(");
    if (d!=null) {
      sayln(d.id.toString());
      prFieldlist(d.fields, i+1); sayln(",");
      if (d.tid!=null) {
	indent(i+1); sayln(d.tid.toString());
      }
      prExp(d.expr, i+1);
    }
    say(")");
  }

  void prDec(Dec_var d, int i) {
    say("VarDec("); say(d.id.toString()); sayln(",");
    if (d.tid!=null) {
      indent(i+1); say(d.tid.toString());  sayln(",");
    }
    prExp(d.expr, i+1); 
    //sayln(",");
    //indent(i+1); say(d.escape);
    say(")"); 
  }

  void prDec(Dec_type d, int i) {
    if (d!=null) {
      say("TypeDec("); say(d.id.toString()); sayln(",");
      prTy(d.type, i+1); 
      /*if (d.next!=null) {
	say(","); prDec(d.next, i+1); 
      }*/
      say(")");
    }
  }

  void prDec(Dec d, int i) {
    indent(i);
    if (d instanceof Dec_fun) prDec((Dec_fun) d, i);
    else if (d instanceof Dec_var) prDec((Dec_var) d, i);
    else if (d instanceof Dec_type) prDec((Dec_type) d, i);
    else throw new Error("Print.prDec");
  }

  void prTy(Ty_id t, int i) {
    say("NameTy("); say(t.sym.toString()); say(")");
  }

  void prTy(Ty_struct t, int i) {
    sayln("RecordTy(");
    prFieldlist(t.fields, i+1); say(")");
  }

  void prTy(Ty_array t, int i) {
    say("ArrayTy("); say(t.sym.toString()); say(")");
  }

  void prTy(Ty t, int i) {
    if (t!=null) {
      indent(i);
      if (t instanceof Ty_id) prTy((Ty_id) t, i);
      else if (t instanceof Ty_struct) prTy((Ty_struct) t, i);
      else if (t instanceof Ty_array) prTy((Ty_array) t, i);
      else throw new Error("Print.prTy");
    }
  }

  void prFieldlist(Listty_field f, int d) {
	indent(d);
    say("Fieldlist("); 
    if (f!=null) {
    	for(Field_ty i:f.list){
    		sayln("");
    		indent(d);
      		say(i.id.toString()+":"+i.tid.toString());
      		sayln(",");
      		//prFieldlist(f.tail, d+1);
    	}
    }
    say(")");
  }

  void prExplist(List_expr e, int d) {
    indent(d);
    say("ExpList("); 
    if (e!=null) {
    	for(Expr i:e.list){
    		sayln("");
    		prExp(i, d+1); 
    		sayln(",");
    	}
      }
    say(")");
  }

  void prDecList(List_dec v, int d) {
    indent(d);
    say("DecList("); 
    if (v!=null) {
    	for(Dec i:v.list){
    		sayln("");
    		prDec(i, d+1); 
    		sayln(",");
    	}
    }
    say(")");
  }

  void prFieldExpList(List_field f, int d) {
    indent(d);
    say("FieldExpList("); 
    if (f!=null) {
    	for(Field i : f.list){
    		sayln("");
    		indent(d);
    		say(i.sym.toString()); sayln(" = ");
    		prExp(i.expr, d+1); sayln(",");
    	}
    }
    say(")");
  }
  
  void prExpSeq(Listseq_expr f, int d) {
	    indent(d);
	    say("ExpList("); 
	    if (f!=null) {
	    	for(Expr i:f.list){
	    		sayln("");
	    		indent(d);
	    		prExp(i, d+1); 
	    		sayln(";");
	    	}
	      }
	    say(")");
  }
}




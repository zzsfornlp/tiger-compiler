package tiger.ast;

import java.util.*;

public class Listseq_expr extends Zposition{
	public List<Expr> list;
	
	// only one expr
	public Listseq_expr(Expr e){
		list = new ArrayList<Expr>();
		line = e.line;
		column = e.column;
		list.add(e);
	}
	//combine lists
	public Listseq_expr(Expr e,Listseq_expr li){
		line = li.line;
		column = li.column;
		list = li.list;
		list.add(e);
	}
}

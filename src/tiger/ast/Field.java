package tiger.ast;

import tiger.symbol.*;
public class Field extends Zposition{
	public Zsymbol sym;
	public Expr expr;
	public Field(int l,int c,Zsymbol s,Expr e){
		line = l;
		column = c;
		sym = s;
		expr = e;
	}
}

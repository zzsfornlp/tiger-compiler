package tiger.ast;

import tiger.symbol.*;
import tiger.typing.*;

public class Field_ty extends Zposition{
	public Zsymbol id;
	public Zsymbol tid;
	public Field_ty(int l,int c,Zsymbol i1,Zsymbol i2){
		line = l;
		column = c;
		id = i1;
		tid = i2;
	}
}
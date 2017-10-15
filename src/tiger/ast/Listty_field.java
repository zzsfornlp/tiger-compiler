package tiger.ast;

import java.util.ArrayList;
import java.util.List;

public class Listty_field extends Zposition{
	public List<Field_ty> list;
	
	// only one expr
	public Listty_field(Field_ty e){
		list = new ArrayList<Field_ty>();
		line = e.line;
		column = e.column;
		list.add(e);
	}
	//combine lists
	public Listty_field(Field_ty e,Listty_field li){
		line = li.line;
		column = li.column;
		list = li.list;
		list.add(e);
	}
}

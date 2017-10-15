package tiger.ast;

import java.util.*;

public class List_field extends Zposition{
	public List<Field> list;
	
	// only one expr
	public List_field(Field e){
		list = new ArrayList<Field>();
		line = e.line;
		column = e.column;
		list.add(e);
	}
	//combine lists
	public List_field(Field e,List_field li){
		line = li.line;
		column = li.column;
		list = li.list;
		list.add(e);
	}
}
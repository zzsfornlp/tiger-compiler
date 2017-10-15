package tiger.ast;

import java.util.*;
public class List_dec extends Zposition{
	public List<Dec> list;
	// only one expr
	public List_dec(Dec e){
		list = new ArrayList<Dec>();
		line = e.line;
		column = e.column;
		list.add(e);
	}
	//combine lists
	public List_dec(Dec e,List_dec li){
		line = li.line;
		column = li.column;
		list = li.list;
		list.add(e);
	}
}

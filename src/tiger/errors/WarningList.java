package tiger.errors;

import java.util.ArrayList;

public class WarningList {
	private ArrayList<CompilerWarning> l=new ArrayList<CompilerWarning>();
	private String filename;
	
	public WarningList(String f){
		filename = f;
	}
	public void add_error(CompilerWarning x){
		l.add(x);
	}
	public void add_error(String m, int li, int c){
		l.add(new CompilerWarning(m,filename,li,c));
	}
	public boolean no_error(){
		return l.isEmpty();
	}
	public void print_error(){
		for(CompilerWarning x : l){
			System.err.println(x.get_error());
		}
	}
}

package tiger.errors;

import java.util.*;

public class ErrorList {
	private ArrayList<CompilerError> l=new ArrayList<CompilerError>();
	private String filename;
	
	public ErrorList(String f){
		filename = f;
	}
	public void add_error(CompilerError x){
		l.add(x);
	}
	public void add_error(String m, int li, int c,String s){
		l.add(new CompilerError(m,filename,li,c,s));
	}
	public boolean no_error(){
		return l.isEmpty();
	}
	public void print_error(){
		for(CompilerError x : l){
			System.err.println(x.get_error());
		}
	}
}

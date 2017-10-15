package tiger.errors;

public class CompilerWarning{
	protected int line,column;
	protected String message;
	protected String filename;
	
	public CompilerWarning(String m, String f, int l, int c) {
        line = l;
        column = c;
        message = m;
        filename = f;
    }
	public String get_error(){
    	return "File "+filename+": "+" warning "+(line+1)+","+(column+1)+": "+message;
    }
}

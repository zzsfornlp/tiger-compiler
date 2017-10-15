package tiger.errors;

public class CompilerError extends RuntimeException{
	protected int line,column;
	protected String message;
	protected String filename;
	protected String stage;
	
	public CompilerError(String m, String f, int l, int c,String s) {
        super(m);
        line = l;
        column = c;
        message = m;
        filename = f;
        stage = s;
    }
	public String get_error(){
    	return "File "+filename+": "+stage+" error "+(line+1)+","+(column+1)+": "+message;
    }
}

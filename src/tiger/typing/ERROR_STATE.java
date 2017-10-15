package tiger.typing;

public class ERROR_STATE extends Type{
	public Type actual() {return this;}
	
	/* in the error state, ignore the type mismatch */
	public boolean coerceTo(Type t) {return true;}
	
	
}

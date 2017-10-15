package tiger.ic;

import java.util.LinkedList;

import tiger.typing.*;
import tiger.ast.*;

abstract public class IcVar extends IcOper{
	public IcFun fun;
	public String name;
	public Dec_var dec;
	
	public IcVar(IcFun f,String n){
		fun = f;
		name = n;
	}
	
	public String toString(){
		return name;
	}
}

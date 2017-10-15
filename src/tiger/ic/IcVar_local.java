package tiger.ic;

import java.util.LinkedList;
import tiger.ast.*;
import tiger.typing.Type;

public class IcVar_local extends IcVar{
	public int num;	//num in the local_list of fun
	public IcVar_local(IcFun f,String n,int nu,Dec_var d){
		super(f,n);
		dec = d;
		num = nu;
	}
}

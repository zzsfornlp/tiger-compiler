package tiger.ic;

import tiger.ast.Dec_var;
import tiger.typing.Type;

public class IcVar_param extends IcVar{
	public int num;	//num in the param_list of fun
	public IcVar_param(IcFun f,String n,int nu,Dec_var d){
		super(f,n);
		dec = d;
		num = nu;
	}
}

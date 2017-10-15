package tiger.ic;

import java.util.LinkedList;

import tiger.typing.Type;

public class IcVar_tmp extends IcVar{
	public IcVar_tmp(IcFun f,String n){
		super(f,n);
	}
	static int so_far=1;
	public int tmp_num;
	public static IcVar_tmp new_one(IcFun f){
		IcVar_tmp x = new IcVar_tmp(f,"_t"+so_far);
		x.tmp_num = so_far++;
		return x;
	}
}

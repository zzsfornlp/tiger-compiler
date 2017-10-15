package tiger.ic;

public class IcConst_str extends IcConst{
	public String str;
	public int num;	//place in the global const pool
	public IcConst_str(String a){
		str = a;
		num = IcFun.add_str(a);
	}
	public String toString(){
		return str;
	}
}

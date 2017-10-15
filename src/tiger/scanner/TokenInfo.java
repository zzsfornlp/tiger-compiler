package tiger.scanner;

public class TokenInfo {
	public int tag,line,column;
	public Object info;
	public TokenInfo(int t,int l,int c,Object x){
		tag = t;
		line = l;
		column = c;
		info = x;
	}
}

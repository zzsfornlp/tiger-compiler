package tiger.block;

import java.util.*;
import tiger.ic.*;
import static tiger.ic.IcFun.*;

public class Block {
	public ArrayList<IcStmt> stmts = new ArrayList<IcStmt>();
	public IcFun belonged;
	public int block_num;
	
	/* step2: parts the basic blocks */
	//links
	public ArrayList<Block> gotos = new ArrayList<Block>();
	public ArrayList<Block> comes = new ArrayList<Block>();
	//temp info
	public ArrayList<Integer> goto_addrs = new ArrayList<Integer>();
	
	/* step3: liveness */
	public HashSet<IcVar> in = new HashSet<IcVar>();
	public HashSet<IcVar> out = new HashSet<IcVar>();
	public HashSet<IcVar> def = new HashSet<IcVar>();
	public HashSet<IcVar> use = new HashSet<IcVar>();
	
	/* step4: next ref */
	//......
	
	public Block(IcFun f,int n){
		belonged = f;
		block_num = n;
	}
	//toString
	public String toString(){
		return "Block"+block_num;
	}
	//print
	public void print(int n){
		print_head(n);
		print_out(this.toString()+":comes"+comes+",gotos"+gotos+"\n");
		//liveness info
		print_head(n+1);
		print_out("IN:"+in+"\n");
		print_head(n+1);
		print_out("OUT:"+out+"\n");
		print_head(n+1);
		print_out("DEF:"+def+"\n");
		print_head(n+1);
		print_out("USE:"+use+"\n");
		//stmts
		print_head(n);
		print_out("[\n");
		for(IcStmt i : stmts){
			i.print_nextref(n+1);
		}
		print_head(n);
		print_out("]\n");
	}
	
	public void print_reg(int n){
		print_head(n);
		print_out(this.toString()+":comes"+comes+",gotos"+gotos+"\n");
		//liveness info
		print_head(n+1);
		print_out("IN:"+in+"\n");
		print_head(n+1);
		print_out("OUT:"+out+"\n");
		print_head(n+1);
		print_out("DEF:"+def+"\n");
		print_head(n+1);
		print_out("USE:"+use+"\n");
		//stmts
		print_head(n);
		print_out("[\n");
		for(IcStmt i : stmts){
			i.print_reg(n+1);
		}
		print_head(n);
		print_out("]\n");
	}
}

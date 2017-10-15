package tiger.cg;

public class Mips {
	//return strings of mips command
	
	/* move rd,rs */
	public static String Mips_move(String rd,String rs){
		return "move "+rd+","+rs+"\n";
	}
	
	/* addi rd,rs,imm */
	public static String Mips_addi(String rd,String rs,int imm){
		return "addi "+rd+","+rs+","+imm+"\n";
	}
	
	/* op rd,rs1,rs2(rs2 can be imm) */
	public static String Mips_op(String op,String rd,String rs1,String rs2){
		return op+" "+rd+","+rs1+","+rs2+"\n";
	}
	
	/* li rd,imm */
	public static String Mips_li(String rd,int imm){
		return "li "+rd+","+imm+"\n";
	}
	
	/* cmp rd,rs1,rs2(rs2 can be imm) */
	public static String Mips_cmp(String op,String rd,String rs1,String rs2){
		return op+" "+rd+","+rs1+","+rs2+"\n";
	}
	
	/* la rd,addr */
	public static String Mips_la(String rd,String addr){
		return "la "+rd+","+addr+"\n";
	}
	
	/* lw rd,imm(rs_addr) */
	public static String Mips_lw(String rd,String rs_addr,int index){
		if(index==0)
			return "lw "+rd+",("+rs_addr+")\n";
		else
			return "lw "+rd+","+index+"("+rs_addr+")\n";
	}
	
	/* sw rs1,imm(rs_addr) */
	public static String Mips_sw(String rs,String rs_addr,int index){
		if(index==0)
			return "sw "+rs+",("+rs_addr+")\n";
		else
			return "sw "+rs+","+index+"("+rs_addr+")\n";
	}
	
	/* b** rs1,rs2,l(rs2 can be imm) */
	public static String Mips_branch(String op,String rs1,String rs2,String l){
		return op+" "+rs1+","+rs2+","+l+"\n";
	}
	
	/* j l */
	public static String Mips_jump(String l){
		return "j "+l+"\n";
	}
	
	/* jal l */
	public static String Mips_jal(String l){
		return "jal "+l+"\n";
	}
	
	/* jr rs */
	public static String Mips_jr(String rs){
		return "jr "+rs+"\n";
	}
	
}

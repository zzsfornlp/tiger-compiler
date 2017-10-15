package tiger.cg;

public class Codegen_constant {
	public static final String str_prefix = "_S";
	public static final String label_prefix = "_l";
	
	/* Mips */
	//frame layout --- away from $fp
	public static final String[] MIPS_REGS={"$s0","$s1","$s2","$s3","$s4","$s5","$s6","$s7",
		"$t0","$t1","$t2","$t3","$t4","$t5","$t6","$t7"};
	public static final String MIPS_T1="$t8",MIPS_T2="$t9";
	public static final int MIPS_ALL_REGS=16;
	public static final int MIPS_ACCESS_LINK=-1,MIPS_CALLE_SAVE_REG=8;
}

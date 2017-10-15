package tiger.typing;

public abstract class Type {
   public Type actual() {return this;}
         
   public boolean coerceTo(Type t) {return false;}
   
   public static final ERROR_STATE THE_ERROR_STATE=new ERROR_STATE();
   public static final NIL THE_NIL=new NIL();
   public static final INT THE_INT=new INT();
   public static final STRING THE_STRING=new STRING();
   public static final VOID THE_VOID=new VOID();
}


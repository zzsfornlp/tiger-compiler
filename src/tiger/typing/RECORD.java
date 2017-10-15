package tiger.typing;

import tiger.symbol.*;
import java.util.*;

public class RECORD extends Type {
   public LinkedHashMap<Zsymbol,Type> fie;
   public ArrayList<Zsymbol> order;
   public RECORD(LinkedHashMap<Zsymbol,Type> m) {
	   fie = m;
	   order = new ArrayList<Zsymbol>();
   }
   
   public boolean coerceTo(Type t) {
	   
	return this==t.actual()||t instanceof NIL;
   }
}
   


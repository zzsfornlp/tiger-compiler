package tiger.typing;

import tiger.symbol.*;
public class NAME extends Type {
   public Zsymbol name;
   public Type binding;
   public NAME(Zsymbol n) {name=n;}
   public boolean isLoop() {
      Type b = binding; 
      boolean any;
      binding=null;
      if (b==null) any=true;
      else if (b instanceof NAME)
            any=((NAME)b).isLoop();
      else any=false;
      binding=b;
      return any;
     }
     
   public Type actual() {return binding.actual();}

   public boolean coerceTo(Type t) {
	return this.actual().coerceTo(t);
   }
   public void bind(Type t) {binding = t;}
   
   public String toString(){
	   return binding.toString();
   }
}

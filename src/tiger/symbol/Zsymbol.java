package tiger.symbol;

import java.util.HashMap;

public class Zsymbol {

    private String name;

    private Zsymbol(String n) {
        name = n;
    }

    public String toString() {
        return name;
    }

    private static HashMap<String, Zsymbol> dict = new HashMap<String, Zsymbol>();

    /**
     * Make return the unique symbol associated with a string. Repeated calls to <tt>symbol("abc")</tt> will return the
     * same Symbol.
     */
    public static Zsymbol symbol(String n) {
        String u = n.intern();
        Zsymbol s = dict.get(u);
        if (s == null) {
            s = new Zsymbol(u);
            dict.put(u, s);
        }
        return s;
    }
}

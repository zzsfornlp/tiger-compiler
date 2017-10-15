package tiger.ra;

import java.util.*;

public class Graph{
	class Node{
		public int num;
		public int degree=0;
		public HashSet<Integer> edges = new HashSet<Integer>();
		Node(int n){
			num = n;
		}
		void plus(int e){
			edges.add(e);
			degree = edges.size();
		}
		void minus(int e){
			edges.remove(e);
			degree = edges.size();
		}
		public Object clone(){
			Node tmp = new Node(num);
			for(int i : edges)
				tmp.plus(i);
			return tmp;
		}
	}
	//nodes from num-0 --- num-n
	public ArrayList<Node> list = new ArrayList<Node>();
	public int num;
	
	public Graph(int n){
		num = n;
		for(int i=0;i<n;i++)
			list.add(new Node(i));
	}
	public void add_edge(int e1,int e2){
		//don't check, exception is error
		Node x = list.get(e1);
		x.plus(e2);
	}
	
	public void debug_print(){
		for(int i=0;i<num;i++){
			System.err.print(i+":");
			System.err.println(list.get(i).edges);
		}
	}
	
	//coloring graph --- 0 means spilling
	List<Integer> coloring(int max){
		//deep copy
		ArrayList<Node> list_copy =new ArrayList<Node>();
		for(int i=0;i<num;i++)
			list_copy.add((Node)list.get(i).clone());

		//stack
		LinkedList<Node> stack = new LinkedList<Node>();
		//push to stack until empty
		while(list.size() > 0){
			Node out = null;
			//whether < max ...
			for(int i=0;i<list.size();i++){
				out = list.get(i);
				if(out.degree < max)
					break;
				else
					out = null;
			}
			if(out == null){
				//not finding --- spill the most degree
				out = list.get(0);
				for(int i=0;i<list.size();i++){
					Node tmp = list.get(i);
					if(tmp.degree > out.degree)
						out = tmp;
				}
			}
			stack.push(list_copy.get(out.num));
			list.remove(out);
			for(Node n : list)
				n.minus(out.degree);
		}
		//pop the stack and alloc reg
		List<Integer> regs_allocating = new ArrayList<Integer>();
		for(int i=0;i<num;i++)
			regs_allocating.add(0);

		
		while(!stack.isEmpty()){
			boolean [] allocated = new boolean[max];
			Arrays.fill(allocated, false);
			//pop one
			Node x = stack.pop();
			for(int k : x.edges){
				int t = regs_allocating.get(k);
				if(t > 0)	//allocated
					allocated[t-1] = true;
			}
			//try to get one
			for(int i=0;i<max;i++){
				if(allocated[i]==false){
					int reg_getting = i+1;
					regs_allocating.set(x.num, reg_getting);
					break;
				}		
			}
		}
		return regs_allocating;
	}
}

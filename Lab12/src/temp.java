import java.util.ArrayList;

public class temp {
	public static void printBinTree(Node root)
	{	//YOUR BONUS CODE GOES HERE
		/*nodeList = new ArrayList<Node>();
		if(root!= null) {
			getNodeQueue(root);
			for(Node key:nodeList) {
				System.out.println(key.id + " Level: " + treelevel(key, 0));
			}
			System.out.println();
			boolean globalStop = false;
			for(int i=0;i!=nodeList.size();) {
//				System.out.println("globalstop: " + globalStop);
				if(i == 0) {
					int headspc = 0;
					switch(treelevel(nodeList.get(0), 0)) {
					case 0: headspc = 0;	break;
					case 1: headspc = 2;	break;
					case 2: headspc = 5;	break;
					case 3: headspc = 11;	break;
					}
					for(int spc = 0;spc < headspc;spc++) {
						System.out.print(" ");
					}
					System.out.println(nodeList.get(0).id);
					for(int spc = 0;spc < treelevel(nodeList.get(0), 0)*2-1;spc++) {
						System.out.print(" ");
					}
					if(nodeList.get(0).left!=null) System.out.print("/");
					else System.out.print(" ");
					for(int spc = 0;spc < Math.pow(3, treelevel(nodeList.get(0), 0)-1);spc++) {
						System.out.print(" ");
					}
					if(nodeList.get(0).right!=null) System.out.println("\\");
					else System.out.println(" ");
					i++;
				}
//				wait(3);
//				System.out.println(nodeList.size());
				int currentlevel;
				boolean lineStart = true;
				boolean smallStop = false;
				while(!smallStop){
					currentlevel = treelevel(nodeList.get(i), 0);
//					System.out.println("CurrentLV: " + currentlevel);
					if(lineStart) {
						for(int spc = 0;spc<currentlevel*2;spc++) {
							System.out.print(" ");
						}
					}
					System.out.print(nodeList.get(i).id);
					int spc_pair = 0;
					switch(currentlevel) {
					case 0: spc_pair = 3;	break;
					case 1: spc_pair = 5;	break;
					case 2: spc_pair = 11;	break;
					}
					for(int spc = 0;spc < spc_pair;spc++) {
						System.out.print(" ");
					}
					i++;
//					System.out.println("i=" + i);
//					if(treelevel(nodeList.get(i), 0) != currentlevel) break;
					System.out.print(nodeList.get(i).id);
					System.out.print(" ");
//					System.out.println("i=" + i);
					i++;
//					System.out.println("i = " + i);
					
					
					if(i == (nodeList.size())) {
						globalStop = true;
						smallStop = true;
//						System.out.println("globalstop: " + globalStop);
					}
					else {
						
						if(treelevel(nodeList.get(i), 0) != currentlevel) smallStop = true; globalStop = true;
					}
					
				}
				
				System.out.println();
				for()
			}
		}*/
		bruteTree(root);
	}
	
	public static void bruteTree(Node s) {
		nodeList = new ArrayList<Node>();
		for(int i = treelevel(s, 0);i>=0;i--) {
			if(i == treelevel(s, 0)) {
				nodeList.add(s);
			}
			else if(i == treelevel(s, 0)-1) {
				nodeList = new ArrayList<Node>();
				nodeList.add(s);
				if(s.left != null)	nodeList.add(s.left);
				else nodeList.add(null);
				if(s.right != null)	nodeList.add(s.right);
				else nodeList.add(null);
			}
			else if(i == treelevel(s, 0)-2) {
				nodeList = new ArrayList<Node>();
				nodeList.add(s);
				if(s.left != null)	{
					if(s.left.left != null) nodeList.add(s.left.left);
					else nodeList.add(null);
					if(s.left.right != null) nodeList.add(s.left.right);
					else nodeList.add(null);
				}
				else {
					nodeList.add(null);
					nodeList.add(null);
				}
				if(s.right != null)	{
					if(s.right.left != null) nodeList.add(s.right.left);
					else nodeList.add(null);
					if(s.right.right != null) nodeList.add(s.right.right);
					else nodeList.add(null);
				}
				else {
					nodeList.add(null);
					nodeList.add(null);
				}
			}
		}
		for(Node lev:nodeList) {
			if(lev == null) System.out.print("N ");
			else System.out.print(lev.id + " ");
		}
		
		
	}
	
	/*
	 * if(root.left != null) printBinTree(root.left);
			//print spacing between each node
			for(int node_spc = 0;node_spc < treelevel(root, 0);node_spc++) {
				System.out.print(" ");
			}
			if(root.right != null) {
				printBinTree(root.right);
			}
			System.out.println();   								//print new line after finish each breadth
	*/

	//Can't print the top most node
	//if(root == null) System.out.println("");
	//else {
////		System.out.println(root.id);
////		System.out.println("\\ /");
//		if(root.left != null) System.out.println(root.left.id);
//		if(root.right != null) System.out.println(root.right.id);
//		if(root.left != null) printBinTree(root.left);
//		if(root.right != null) printBinTree(root.right);
	//}

	/* 	//Almost work
	if(root == null) System.out.println("");
	else {
		if(topmost) {
			for(int spc = 0;spc < treelevel(root, 0)*2;spc++) {
				System.out.print(" ");
			}
			System.out.println(root.id);
			for(int spc = 0;spc < (treelevel(root, 0)*2-1);spc++) {
				System.out.print(" ");
			}
			System.out.println("/ \\");
		}
		topmost = false;
		if(root.left != null) {
			for(int spc = 0;spc < treelevel(root.left, 0)*2;spc++) {
				System.out.print(" ");
			}
			System.out.print(root.left.id);
		}
		for(int spc = 0;spc < treelevel(root.left, 0)*2;spc++) {
			System.out.print(" ");
		}
		if(root.right != null) {
			System.out.print(root.right.id);
		}
		System.out.println();
		if(root.left != null) {
			for(int spc = 0;spc < (treelevel(root.left, 0)*2-1);spc++) {
				System.out.print(" ");
			}
			System.out.print("/ \\");
		}
		if(root.right != null) {
			for(int spc = 0;spc < (treelevel(root.left, 0)*2-1);spc++) {
				System.out.print(" ");
			}
			System.out.print("/ \\");
		}
		System.out.println();
		if(root.left != null) printBinTree(root.left);
		if(root.right != null) printBinTree(root.right);
		
		1
	   / \
	  3  4
	 / \ / \
	67
	/ \/ \




	810
	/ \/ \
	*/
	
	
	public static Node getMaxSubTree(Node left, Node right) {
		if(left.id<right.id) {
			return right;
		}
		else return left;
	}
	
	public static Node getMinSubTree(Node left, Node right) {
		if(left.id>right.id) {
			return right;
		}
		else return left;
	}
	
	public static Node nodeReArrange(Node root){
		int rt = root.id;
		int left = root.left.id;
		int right = root.right.id;
	}
}

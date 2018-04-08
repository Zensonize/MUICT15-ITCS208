import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class FullBinaryTreeTester {
	static boolean topmost = true;
	static List<Node> nodeList;
	public static void inOrderTraverse(Node root)
	{
		if(root != null) {
			if(root.left == null && root.right == null) System.out.print(root.id + " ");
			else{
				if(root.left != null) inOrderTraverse(root.left);
				System.out.print(root.id + " ");
				if(root.right != null) inOrderTraverse(root.right);
				
			}
		}
	}
	
	public static boolean isFullBinTree(Node root)
	{	//YOUR CODE GOES HERE
		//Check root = null --> is leaf --> is origin of leaf --> is origin of origin of leaf then travel left and right
		if(root == null) return true;
		else if(root.left == null && root.right == null) return true;
		else if(root.left == null || root.right == null) return false;
		else {
			boolean dive_Left = isFullBinTree(root.left);
			boolean dive_Right = isFullBinTree(root.right);
			return (dive_Left && dive_Right);
		}
	}
	
	public static void normalTester()
	{
		Node[] ts = new Node[7];
		int count = 0;
		ts[count++] = null;
		ts[count++] = new Node(16, null, null);
		
		ts[count++] = new Node(16, new Node(14, null, null), null);
		
		ts[count++] = new Node(1, new Node(3, new Node(6, null, null), new Node(7, null, null)), 
				new Node(4, new Node(8, null, null), new Node(10, null, null)));
		
		ts[count++] = new Node(1, new Node(3, null, null), 
				new Node(4, new Node(8, null, null), new Node(10, null, null)));
		
		ts[count++] = new Node(1, new Node(3, new Node(6, null, null), null), 
				new Node(4, new Node(8, null, null), new Node(10, null, null)));
		
		ts[count++] = new Node(1, new Node(3, new Node(6, null, null), new Node(7, null, null)), 
				null);
		
		for(int i = 0; i < ts.length; i++)
		{
			System.out.print("[T"+i+"] in-order: ");
			inOrderTraverse(ts[i]);
			System.out.println("\n[T"+i+"] is"+(isFullBinTree(ts[i])?" ":" NOT ")+"a full binary tree.\n");
		}
		
	}
	
	
	/**************BONUS STARTS***************/
	public static void printBinTree(Node root)
	{	//YOUR BONUS CODE GOES HERE
		nodeList = new ArrayList<Node>();
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
			}
		}
	}
	
	public static void getNodeQueue(Node root){
		if(topmost) {
			nodeList.add(root);
//			System.out.println(root.id);
		}
		topmost = false;
		if(root.left != null) {
			nodeList.add(root.left);
//			System.out.print(root.left.id);
		}
		if(root.right != null) {
			nodeList.add(root.right);
//			System.out.print(root.right.id);
		}
		if(root.left != null) getNodeQueue(root.left);
		if(root.right != null) getNodeQueue(root.right);
	}
	
//	public static int getNodePos(int curr, Node root, int pos) {
//		if(root.id == curr) return pos;
//		else
//		return 0;
//	}
	public static Node getBinSearchTree(Node root)
	{	//YOUR BONUS CODE GOES HERE
		return null;
	}
	
	public static void bonusTester()
	{
		Node t = new Node(1, new Node(3, new Node(6, null, null), new Node(7, null, null)), 
				new Node(4, new Node(8, null, null), new Node(10, null, null)));
		System.out.println(treelevel(t, 0));
//		System.out.println("Before Transforming: ");
		printBinTree(t);
//		System.out.println("After Transforming: ");
//		getBinSearchTree(getBinSearchTree(t));
		
	}
	
	//find tree level
	public static int treelevel(Node root, int level) {
		if(root == null) return level;
		else {
//			System.out.println("at node : " + root.id + " Level: " + level);
			int left = 0,right = 0;
			if(root.left == null && root.right == null) return level;
			if(root.left != null) {
				left =  treelevel(root.left, level+1);
			}
			if(root.right != null) { 
				right = treelevel(root.right, level+1);
			}
			return Math.max(left, right);
		}
	}
	
	/**************BONUS ENDS***************/
		
	public static void main(String[] args)
	{
//		normalTester();
		
		//Uncomment for bonus
		bonusTester();
	}
	
	public static void wait(int duration) {
		try {
			for(int i=0;i<duration;i++) {
				TimeUnit.SECONDS.sleep(1);
//				System.out.print(" .");
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
////	System.out.println(root.id);
////	System.out.println("\\ /");
//	if(root.left != null) System.out.println(root.left.id);
//	if(root.right != null) System.out.println(root.right.id);
//	if(root.left != null) printBinTree(root.left);
//	if(root.right != null) printBinTree(root.right);
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

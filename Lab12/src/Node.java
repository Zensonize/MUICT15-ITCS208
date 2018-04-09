
public class Node {
	public int id;
	public Node left = null;
	public Node right = null;
	
	public Node(int _id, Node _left, Node _right)
	{	id = _id;
		left = _left;
		right = _right;
	}
	
	public void addLeft(Node _left) {
		left = _left;
	}
	
	public void addRight(Node _right) {
		right = _right;
	}
}

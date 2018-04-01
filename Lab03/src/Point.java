//# COPYRIGHT KrittametK

public class Point {
	private int x;
	private int y;
	
	public Point(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int manhattanDistance(Point other) {
		return Math.abs(x-other.getX()) + Math.abs(y-other.getY());
	}
	
	public double distance(Point other) {
		return Math.pow(Math.pow(other.getX()-x, 2) + Math.pow(other.getY()-y, 2), 0.5);
	}
}

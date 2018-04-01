//# COPYRIGHT KrittametK
import java.net.SocketTimeoutException;

public class Circle {
	private Point center;
	private double r;
	public Circle(Point center,double r) {
		this.center = center;
		this.r = r;
	}
	
	public Point getCenter() {
		return center;
	}
	
	public double getRadius() {
		return r;
	}
	
	public double getArea() {
		return Math.PI*Math.pow(r, 2);
	}
	
	public double getCircumference() {
		return 2*Math.PI*r;
	}
	
	public String toString(){
		return "Circle[center=(" + center.getX() + "," + center.getY() + "),radius=" + r +"]";
	}
	
	public boolean contains(Point p) {
		if(Math.pow(Math.pow(p.getX()-center.getX(), 2) + Math.pow(p.getY()-center.getY(), 2), 2) <= Math.pow(r, 2)) {
			return true;
		}
		else return false;
	}
	
	public boolean contains(Circle c) {
		if(this.contains(c.getCenter())) {
			if(c.getRadius()<=getRadiusmax(c)) {
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	private double getRadiusmax(Circle c) {
		return r-center.distance(c.getCenter());
	}
	
	
	
}

//# COPYRIGHT KrittametK
import java.util.Scanner;

public class CircleDrawer {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Please input (x,y): ");
		Point a = new Point(in.nextInt(),in.nextInt());
		System.out.print("Please input r: ");
		Circle A = new Circle(a,in.nextInt());
		System.out.print("Please input (x,y): ");
		Point b = new Point(in.nextInt(),in.nextInt());
		System.out.print("Please input r: ");
		Circle B = new Circle(b,in.nextInt());
		System.out.println("Distance Between a-b = " + a.manhattanDistance(b));
		System.out.println(A.toString());
		System.out.println(B.toString());
		System.out.println("Circum of A: " + A.getCircumference());
		System.out.println("Area of A: " + A.getArea());
		System.out.println("Circum of B: " + B.getCircumference());
		System.out.println("Area of B: " + B.getArea());
		System.out.println("Is A contain b: " + A.contains(b));
		System.out.println("Is B contain a: " + B.contains(a));
		System.out.println("Is A contain B: " + A.contains(B));
		System.out.println("Is B contain A: " + B.contains(A));

	}

}

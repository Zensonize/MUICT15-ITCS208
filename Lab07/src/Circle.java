
public class Circle extends Shape {
	private double radius;
	
	public Circle() {
		
	}
	
	public Circle(String color, double radius) {
		super.setColor(color);
		this.radius = radius;
	}
	
	@Override
	   public String toString() {
			return "Triangle[radius="+ radius + ",Shape[color=" + super.getColor() + "]]";
	   }

	   // Override the inherited getArea() to provide the proper implementation
	   @Override
	   public double getArea() {
		   return Math.PI*Math.pow(radius, 2);
	   }

	   public double getArea(double radius) {
		   this.radius = radius;
		   return Math.PI*Math.pow(radius, 2);
	   }
}

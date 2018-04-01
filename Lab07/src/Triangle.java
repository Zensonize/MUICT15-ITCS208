

/*  The structure of Triangle class is similar to Rectangle */
public class Triangle extends Shape {
	
	private double base;
	private double height;
	
	public Triangle() {
		
	}
	
	public Triangle(String color, double base, double height) {
		super.setColor(color);
		this.base = base;
		this.height = height;
	}

	@Override
	   public String toString() {
			return "Triangle[base="+ base + ",height=" + height + ",Shape[color=" + super.getColor() + "]]";
	   }

	   // Override the inherited getArea() to provide the proper implementation
	   @Override
	   public double getArea() {
		   return base*height*0.5;
	   }

	   public double getArea(double base, double height) {
		   this.base = base;
		   this.height = height;
		   return base*height*0.5;
	   }

}

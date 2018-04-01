
/*
 * The Rectangle class, subclass of Shape
 */
public class Rectangle extends Shape {
   // Private member variables
   private double length;
   private double width;

   // Constructors
   public Rectangle()
   {

   }
   public Rectangle(String color, double length, double width) {
	   super.setColor(color);
	   this.length = length;
	   this.width = width;
   }

   @Override
   public String toString() {
	   return "Rectangle[length="+ length + ",width=" + width + ",Shape[color=" + super.getColor() + "]]";
   }

   // Override the inherited getArea() to provide the proper implementation
   @Override
   public double getArea() {
	   return length*width;
   }

   public double getArea(double length, double width) {
	   this.length = length;
	   this.width = width;
	   return length*width;
   }
}

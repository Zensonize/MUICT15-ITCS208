
public class Rectangle {

	public int width;
	public int height;
	
	public Rectangle() {
		width = 0;
		height = 0;
	}
	
	public void setWidth(int w) {
		width = w;
	}
	
	public void setHeight(int h) {
		height = h;
	}
	
	public int area() {
		return width * height;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Rectangle myRect = new Rectangle();
		myRect.setWidth(40);
		myRect.setHeight(50);
		System.out.println("myRect's area is " + myRect.area());
	}

}

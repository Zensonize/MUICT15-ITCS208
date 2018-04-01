
public class DateTester {

	public static void main(String[] args) {
		/*MyDate a = new MyDate();
		System.out.println("Object Number (a): " + a.getObjectNumber());
		System.out.println("a's Date: " + a.toString());
		System.out.println("a's Date: " + a.previousDay());
		System.out.println("a's Date: " + a.nextDay());
		System.out.println("a's Date: " + a.previousMonth());
		System.out.println("a's Date: " + a.nextMonth());
		a.setDate(13,4,2000);
		System.out.println("a's Date: " + a.toString());
		System.out.print("a's Year is " + a.getYear() + ", ");
		if(a.isLeapYear(a.getYear()) == true) System.out.println("Which is a leap year.");
		else System.out.println("Which is not a leap year.");
		
		System.out.println();*/
		MyDate b = new MyDate(28,2,2016);
		System.out.println("Object Number (b): " + b.getObjectNumber());
		System.out.println("b's Date: " + b.toString());
		System.out.println("b's Date: " + b.nextDay());
		System.out.println("b's Date: " + b.nextDay());
		System.out.println("b's Date: " + b.nextYear());
		System.out.println("b's Date: " + b.previousYear());
		System.out.print("b's Year is " + b.getYear() + ", ");
		if(b.isLeapYear(b.getYear()) == true) System.out.println("Which is a leap year.");
		else System.out.println("Which is not a leap year.");
		
		System.out.println();
		MyDate c = new MyDate(2,3,2017);
		System.out.println("Object Number (c): " + c.getObjectNumber());
		System.out.println("c's Date: " + c.previousDay());
		System.out.println("c's Date: " + c.previousDay());
		System.out.println("c's Date: " + c.previousYear());
		System.out.println("c's Date: " + c.nextDay());
		System.out.println("c's Date: " + c.previousYear());
		System.out.print("c's Year is " + c.getYear() + ", ");
		if(c.isLeapYear(c.getYear()) == true) System.out.println("Which is a leap year.");
		else System.out.println("Which is not a leap year.");
	}

}

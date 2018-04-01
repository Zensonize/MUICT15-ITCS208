
public class TimeSpanTester {
	public static void main(String[] args) {
		TimeSpan a = new TimeSpan(2,30);
		TimeSpan b = new TimeSpan(1,47);
		System.out.println(a.getHours());
		System.out.println(a.getMinutes());
		a.add(b);
		System.out.println(a.toString());
		System.out.println(a.getTotalHours());
		
		
	}
}

import java.util.Arrays;
import java.util.Map;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double[] a = new double[5];
		
		for(int i =0;i<5;i++) {
			System.out.println(a[i]);
		}
		String t = Arrays.toString(a);
		t = t.replaceAll(",", "");
		t = t.replace("[", "");
		t = t.replace("]", "");
		System.out.println("toString " + t);
	}

}

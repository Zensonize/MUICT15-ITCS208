
public class rec {
	public static void ppp(int n) {
		if (n != 0) {
			System.out.println("*");
			ppp(n-1);
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ppp(5);
	}

}

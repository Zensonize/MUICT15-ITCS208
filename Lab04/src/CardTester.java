//# COPYRIGHT KrittametK
import java.util.Scanner;

public class CardTester {

	public static void main(String[] args) {
		Card a = new Card("5J");
		Card b = new Card("6S");
		Card c = new Card("3K");
		System.out.println(a.getDescription());
		System.out.println(b.getDescription());
		System.out.println(c.getDescription());
		
		
		Scanner in = new Scanner(System.in);
		for(int i=0;i<3;i++) {
			System.out.print("Please input: ");
			a.setNotation(in.nextLine());
			System.out.println(a.getDescription());
		}
		
		String inc = new String();
		
		while(true) {
			System.out.print("Please input ('q' to end): ");
			inc = in.nextLine();
			if(inc.charAt(0) == 'q') {
				System.out.println("Thank you! Bye!");
				break;
			}
			else {
				a.setNotation(inc);
				System.out.println(a.getDescription());
			}
		}
	}

}

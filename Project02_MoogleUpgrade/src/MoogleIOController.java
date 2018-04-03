import java.util.Scanner;

public class MoogleIOController {
	public static char readChar(char minChar,char maxChar) {
		Scanner in = new Scanner(System.in);
		while(true) {
			System.out.print("\nYour Choice: ");
			char stream = in.next().charAt(0);
			if((int) stream >= (int) minChar && (int) stream <= (int) maxChar) return stream;
			else if(stream == 'E' || stream == 'e') return stream;
			else {
				System.out.println("Wrong input");
			}
		}
	}
	public static int readInt(int minInt, int maxInt) {
		return 0;
	}
	public static String readString() {
		return "";
	}
	public static String readFileSource(String Requirement) {
		return "";
	}
}

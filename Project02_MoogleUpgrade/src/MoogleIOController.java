import java.util.Scanner;

public class MoogleIOController {
	public static char readChar(char minChar,char maxChar) {
		Scanner in = new Scanner(System.in);
		while(true) {
			char stream = in.next().charAt(0);
			if(stream > minChar && stream < maxChar) return stream;
			else if(stream == 'E' || stream == 'e') return stream;
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

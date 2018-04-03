import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("resource")

public class MoogleIOController {
	
	public static char readChar(char minChar,char maxChar, String toEnter) {
		
		while(true) {
			Scanner in = new Scanner(System.in);
			System.out.print(toEnter);
			char stream = in.next().charAt(0);
			if((int) stream >= (int) minChar && (int) stream <= (int) maxChar) 	return stream;
			else if(stream == 'E' || stream == 'e') 							return 'E';
			else System.out.println("Wrong input\n");
		}
	}
	public static int readInt(int minInt, int maxInt, String toEnter) {
		
		while(true) {
			Scanner in = new Scanner(System.in);
			int stream = Integer.MIN_VALUE;
			System.out.print(toEnter);
			try {
				if(in.hasNextInt()) {
					stream = in.nextInt();
				}
			} catch(InputMismatchException e) {
				e.printStackTrace();
			}
			if(stream >= minInt && stream <= maxInt) return stream;
			else System.out.println("Wrong input\n");
		}
		
	}
	public static String readString(String toEnter) {
	
		while(true) {
			Scanner in = new Scanner(System.in);
			System.out.print(toEnter);
			String stream = in.next();
			if(!stream.isEmpty()) return stream;	
		}
	}
	public static String readFileSource(String Requirement) {
		return "";
	}
	
	public static void wait(int duration) {
		try {
			for(int i=0;i<duration;i++) {
				TimeUnit.SECONDS.sleep(1);
				System.out.print(" .");
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		    byte[] array = md.digest(md5.getBytes());
		    StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < array.length; ++i) {
		       sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		    }
		    return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {}
		
		return null;
	}
	
	public static void blankSpace() {
		System.out.println("\n\n\n\n\n");
	}
	
	public static void headerLine() {
		System.out.println("=============================\n");
	}
}

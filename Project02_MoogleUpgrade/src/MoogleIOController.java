import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("resource")

public class MoogleIOController {
	
	/* NOTE
	 *  this method use in menu selection
	 *  this method receive minChar and maxChar to control the possible input and string toEnter uses to print input header
	 *  this method let 'E' passed because 'E' is an exit command
	 */
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
	
	/* NOTE
	 *  this method use in all integer input
	 *  this method receive minInt and maxInt to control the possible input if entered integer, toEnter uses to print input header
	 */
	public static int readInt(int minInt, int maxInt, String toEnter) {
		
		while(true) {
			Scanner in = new Scanner(System.in);
			int stream = Integer.MIN_VALUE;										//prevent null input
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
	
	/* NOTE
	 *  this method use in all String input
	 *  this method receive toEnter uses to print input header
	 */
	public static String readString(String toEnter) {
	
		while(true) {
			Scanner in = new Scanner(System.in);
			System.out.print(toEnter);
			String stream = in.next();
			if(!stream.isEmpty()) return stream;	
		}
	}
	
	/* NOTE
	 *  this method use in search
	 *  this method receive toEnter uses to print input header
	 */
	public static String readLine(String toEnter) {
		while(true) {
			Scanner in = new Scanner(System.in);
			System.out.print(toEnter);
			String stream = in.nextLine();
			if(!stream.isEmpty()) return stream;	
		}
	}
	
	/* Note
	 * this method use to wait to show error then refresh the program page
	 */
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
	
	/*
	 * this method use to encrypt password
	 */
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
	
	/*
	 * this method use to separate between page
	 */
	public static void blankSpace() {
		System.out.println("\n\n\n\n\n");
	}
	
	/*
	 * this method use to print separator line
	 */
	public static void headerLine() {
		System.out.println("=============================\n");
	}
	
}

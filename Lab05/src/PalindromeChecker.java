//# COPYRIGHT KrittametK

import java.util.Scanner;

public class PalindromeChecker {

	public static void checkPalin(String in) {
		Boolean isPhrase = false;
		StringBuilder checknon = new StringBuilder();
		
		for(int i=0;i<in.length();i++) {
			if(Character.isLetter(in.charAt(i)) || Character.isDigit(in.charAt(i))) {
				checknon.append(in.charAt(i));
			}
			else {
				isPhrase = true;
				continue;
			}
		}
		
		if(checknon.toString().equalsIgnoreCase(checknon.reverse().toString())) {
			if(isPhrase) {
				System.out.println("The input phrase \"" + in + "\" is a palindrome ");
			}
			else {
				System.out.println("The input word \"" + in + "\" is a palindrome ");
			}
			
		}
		else {
			if(isPhrase) {
				System.out.println("The input phrase \"" + in + "\" is not a palindrome ");
			}
			else {
				System.out.println("The input word \"" + in + "\" is not a palindrome ");
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a word or phrase to check if it is a palindrome: ");
		String a = in.nextLine();
		checkPalin(a);
	}
}

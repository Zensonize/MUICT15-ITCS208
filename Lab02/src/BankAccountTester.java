//# COPYRIGHT KrittametK

import java.util.Scanner;

public class BankAccountTester {

	public static void main(String[] args) {
		
		/*
		BankAccount myMoney = new BankAccount();
		myMoney.deposit(1000);
		myMoney.withdraw(500);
		myMoney.withdraw(400);
		System.out.println(myMoney.getBalance());
		*/
		
		//#THIS PART IS FOR BONUS
		
		int bal;
		System.out.println("NOW RUNNING BONUS...");
		String name = new String();
		String Email = new String();
		Scanner in = new Scanner(System.in);
		System.out.print("Enter Name: ");
		name = in.nextLine();
		System.out.print("Enter Email: ");
		Email = in.nextLine();
		System.out.print("Enter Balance: ");
		bal = in.nextInt();
		Student st_a = new Student(name,Email);
		BankAccount myMoney = new BankAccount(bal,st_a);
		System.out.println(myMoney.getOwner().getName());
		System.out.println(myMoney.getOwnerName());
		System.out.println(myMoney.getOwner().getEmail());
		System.out.println(myMoney.getOwnerEmail());
		System.out.println(myMoney.toString());
		
		//#THIS IS AN END OF BONUS PART
	}

}

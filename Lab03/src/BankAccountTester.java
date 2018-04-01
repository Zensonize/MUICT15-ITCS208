//# COPYRIGHT KrittametK

public class BankAccountTester {

	public static void main(String[] args) {
		BankAccount a = new BankAccount("AA");
		a.deposit(10);
		a.deposit(50);
		a.deposit(10);
		a.deposit(70);
		System.out.println(a.getBalance());
		a.transactionFee(5);
		System.out.println(a.getBalance());
		a.transactionFee(10);
		System.out.println(a.getBalance());

	}

}

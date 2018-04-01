//# COPYRIGHT KrittametK

public class BankAccount {
	private String id;
	private double balance;
	private int transactions;
	
	public BankAccount(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public double getBalance() {
		return balance;
	}

	public int getTransactions() {
		return transactions;
	}
	
	public void deposit(double amount) {
		transactions++;
		balance += amount;
	}
	
	public void withdraw(double amount) {
		transactions++;
		balance -= amount;
	}
	
	public boolean transactionFee(double amount) {
		balance -= amount*transactions*(transactions+1)/2;
		if(balance > 0) return true;
		else {
			balance = 0;
			return false;
		}
	}
	
	
}

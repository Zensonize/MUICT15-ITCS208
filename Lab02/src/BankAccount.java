//# COPYRIGHT KrittametK

public class BankAccount {
	public double balance;
	private Student owner;					//# THIS IS FOR BONUS
	
	
	 
	public BankAccount() {
		balance = 0;
	}
	
	public BankAccount(double balance){
	
		this.balance = balance;
	}

	
	
	public BankAccount(double balance,Student owner) {
		this.balance = balance;
		this.owner = owner;
	}
	
	
	public void deposit(double deposit) {
		balance += deposit;
	}
	
	public void withdraw(double withdraw) {
		balance -= withdraw;
	}
	
	public double getBalance() {
		return balance;
	}
	
	
	//#THIS PART IS FOR BONUS
	public String getOwnerName() {
		return owner.getName();
	}
	
	public String getOwnerEmail() {
		return owner.getEmail();
	}
	
	public String toString() {
		return "Account [owner= " + getOwnerName() + ", email= " + getOwnerEmail() + ", balance= " + balance + "]";
	}
	
	public Student getOwner() {
		return owner;
	}
	//#THIS IS AN END OF BONUS PART
	
	
}

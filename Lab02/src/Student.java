//# COPYRIGHT KrittametK

public class Student {
	private int ID;
	private String std_name = new String();
	private String std_Email = new String();
	
	public Student(String std_name,String std_Email) {
		this.std_name = std_name;
		this.std_Email = std_Email;
		ID = 0;
	}
	
	public String getName() {
		return std_name;
	}
	
	public String getEmail() {
		return std_Email;
	}
}

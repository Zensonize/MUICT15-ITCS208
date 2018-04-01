//#Krittamet 6088063

public class StudentProfile {
	private int studentID;
	private String name = new String();
	private int Age;
	private String Address = new String();
	static int count =0;									//#THIS IS BONUS
	
	
	public StudentProfile() {
		count();											//#THIS IS BONUS
	}
	
	
	public int getStudentID() {
		return studentID;
	}


	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getAge() {
		return Age;
	}


	public void setAge(int age) {
		Age = age;
	}

	private String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}
	
	public static void getAllinfo(StudentProfile in) {
		System.out.println(in.getStudentID() + " : " + in.getName() + ", " + in.getAge());
		System.out.println(in.getAddress());
	}
	
	//#THIS IS BONUS
	static void count() {
		count++;
	}
	
	static int getCount() {
		return count;
	}

	public static int ageDiff(StudentProfile in1,StudentProfile in2) {
		int age;
		if(in1.getAge() > in2.getAge()) age =  in1.getAge()-in2.getAge();
		else age = in2.getAge() - in1.getAge();
		return age;
	}
	
	/*public static void main(String[] args) {
		StudentProfile student1 = new StudentProfile();
		student1.setStudentID(5888023);
		student1.setName("Peter Copper");
		student1.setAge(22);
		student1.setAddress("26 Waterloo street, Leamington spa CV312RT");
		getAllinfo(student1);
	}*/
	
	
}

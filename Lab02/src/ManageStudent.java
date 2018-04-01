//#Krittamet 6088063

public class ManageStudent {
	public static void main(String[] args) {
		StudentProfile student1 = new StudentProfile();
		student1.setStudentID(5888023);
		student1.setName("Peter Copper");
		student1.setAge(22);
		student1.setAddress("26 Waterloo street, Leamington spa CV312RT");
		StudentProfile.getAllinfo(student1);
		System.out.println(StudentProfile.getCount());
		
		StudentProfile student2 = new StudentProfile();
		student2.setStudentID(6088063);
		student2.setName("Zensonize Ezinosnez");
		student2.setAge(18);
		student2.setAddress("27 Waterloo street, Leamington spa CV312RT");
		StudentProfile.getAllinfo(student2);
		System.out.println(StudentProfile.ageDiff(student1,student2));
		System.out.println(StudentProfile.getCount());
	}

	private static char[] ageDiff(StudentProfile student1, StudentProfile student2) {
		// TODO Auto-generated method stub
		return null;
	}
}

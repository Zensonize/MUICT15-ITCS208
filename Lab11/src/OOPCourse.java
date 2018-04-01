import java.util.HashMap;
import java.util.Map;

public class OOPCourse {
	public static void main(String[] args){
		// instantiate HashMap collection
		Map<String, GradedActivity> studentMidterm = new HashMap<String, GradedActivity>();
		
		// create midterm scores for each student
		GradedActivity firstStudent = new Midterm(6, 20);
		firstStudent.setScore(2);		// missed 2 questions
		GradedActivity secondStudent = new Midterm(6, 20);
		secondStudent.setScore(3);		// missed 3 questions
		GradedActivity thirdStudent = new Midterm(6, 20);
		thirdStudent.setScore(1);		// missed 1 questions
		GradedActivity forthStudent = new Midterm(6, 20);
		forthStudent.setScore(4);		// missed 4 questions
		Midterm test = new Midterm(6,20);
		
		// Add data into HashMap collection
		studentMidterm.put("5988000", firstStudent);
		studentMidterm.put("5988999", secondStudent);
		studentMidterm.put("5988998", thirdStudent);
		studentMidterm.put("5988997", forthStudent);
		
		//print the midterm's score and grade letter of all students
		System.out.println("Here is the list of all students and their midterm grade");
		for(String stuID: studentMidterm.keySet()){
			System.out.println("StudentID: " + stuID + " " + studentMidterm.get(stuID).toString());
		}
		
		
		// Task 1: print number of students who have midterm's score higher than 59
		System.out.println("Task 1 -------------------------------------------------");
		// CODE HERE
		int amount = 0;
		for(String key:studentMidterm.keySet()) {
			if(studentMidterm.get(key).getScore()>60) amount++;
		}
		System.out.println("Number of students whose midterm's score is higher than 60 is " + amount);
		
		
		// Task 2:
		System.out.println("Task 2 -------------------------------------------------");
		// suppose the average midterm grade is as follow
		GradedActivity avgMidterm = new Midterm(6, 20);
		avgMidterm.setScore(3);
		// print student ID, score and grade letter of students who are at the average grade (Hint: uses equals() method)
		// CODE HERE
		for(String stuID: studentMidterm.keySet()){
			if(((Midterm) studentMidterm.get(stuID)).equals(avgMidterm)) System.out.println("StudentID: " + stuID + " " + studentMidterm.get(stuID).toString());
			
		}
		
		// BONUS
		System.out.println("Bonus -------------------------------------------------");
		// You have to implement isGreater and isLessThan methods in Midterm class
		// print number of students who have higher rank than the average midterm grade (avgMidterm)
		// CODE HERE
		
		amount = 0;
		for(String stuID: studentMidterm.keySet()){
			if(((Midterm) studentMidterm.get(stuID)).isGreaterThan(avgMidterm)) amount++;
		}
		
		System.out.println("Number of students whose midterm are higher than the average midterm grade is " + amount);
		
	}
	
}

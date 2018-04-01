
public class Midterm extends GradedActivity implements Comparable {
	int numQuestions, pointPerQuestion;
	
	public Midterm(int numQuestions, int pointPerQuestion){
		this.numQuestions = numQuestions;
		this.pointPerQuestion = pointPerQuestion;
	}
	
	public void setScore(int numMissedQuestions){
		// calculate the total score and store in score field
		super.setScore((this.numQuestions - numMissedQuestions)*this.pointPerQuestion);
	}
	
	@Override
	public boolean equals(GradedActivity a) {
		// CODE HERE
		// two midterms are equals when both of them have the same letter grade
		
		if((int) this.getLetterGrade() == (int) a.getLetterGrade()) return true;
		return false;
	}


	@Override
	public boolean isGreaterThan(GradedActivity a) {
		// BONUS: compare midterm by checking at letter grade
		// A is higher than B, B is higher than C, C is higher than D, and D is higher than F
		// You may refer to Comparable interface for more information
		// === CODE HERE ===
		
		if((int) this.getLetterGrade() < (int) a.getLetterGrade()) return true;
		return false;
	}

	@Override
	public boolean isLessThan(GradedActivity a) {
		// BONUS: compare midterm by checking at letter grade
		// F is less than D, D is less than C, C is less than B, and B is less than A
		// You may refer to Comparable interface for more information
		// === CODE HERE ===
		
		if((int) this.getLetterGrade() < (int) a.getLetterGrade()) return false;
		return true;
	}

	@Override
	public char getLetterGrade() {
		if(getScore() >= 80)		return 'A';
		else if(getScore() >= 70)	return 'B';
		else if(getScore() >= 60)	return 'C';
		else if(getScore() >= 50)	return 'D';
		else return 'F';
		
	}
	
}

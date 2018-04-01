
public abstract class GradedActivity {
	private int score;	// numeric score
	
	public GradedActivity(){
		score = 0;
	}
	
	// The setScore method stores its argument in score field.
	public void setScore(int score){
		this.score = score;
	}
	
	// The getScore method returns the score field.
	public int getScore(){
		return score;
	}
	
	// The getGrade method returns a letter grade determined from the score field.
	public abstract char getLetterGrade();
	
	public String toString(){
		return "score: " + this.getScore() + ", grade: " + this.getLetterGrade();
	}
	
}

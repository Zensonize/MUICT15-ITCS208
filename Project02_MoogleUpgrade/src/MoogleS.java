import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MoogleS {
	
	public Map<Integer, Movie> movies;
	public Map<Integer, User> users = new HashMap<Integer,User>();
	String movieFile,ratingFile,userFile;
	
	public MoogleS(String movieFile, String ratingFile, String userFile) {
		this.movieFile = movieFile;
		this.ratingFile = ratingFile;
		this.userFile = userFile;
	}
	
	public boolean loadData() {
		//Test new Branch
		return false;
	}
	
	public void start() {
		System.out.println("Welcome to Moogle");
		System.out.println("=============================\n");
		System.out.println("\t[0] Register\n\t[1] Login\n\t[2] Skip Login and Use Moogle Search Engine\n\n\t[E] Exit\n");
		System.out.print("\nYour Choice: ");
		switch(MoogleIOController.readChar('0', '2')) {
			case '0': while(Register()); return true;
			case '1': while(Login()); return true;
			case '2': while(SearchEngine(null)); return true;
			case 'E': return false;	
		}
		return true;
	}
	
}

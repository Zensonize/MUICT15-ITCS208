import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;


public class MoogleTester {
	
	public static void main(String[] args){
		SampleSearch();
	}
	
	public static void SampleSearch(){
		String movieFileName = "output/movies.csv";
		String ratingFileName = "output/ratings.csv";
		String userFileName = "output/users.csv"; 
		
		MoogleSystem begin = new MoogleSystem(movieFileName, ratingFileName, userFileName);
		try {
			begin.UpdateFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}

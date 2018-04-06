
public class Moogle {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String movieFile,ratingFile,userFile;
		movieFile 	= "output/movies.csv";
		ratingFile 	= "output/ratings.csv";
		userFile 	= "output/users.csv";
		MoogleS program = new MoogleS();
		if(program.loadData(movieFile, ratingFile, userFile)) {					//exit if load data failed
			program.start();
			program.updateFile();
		}
	}

}

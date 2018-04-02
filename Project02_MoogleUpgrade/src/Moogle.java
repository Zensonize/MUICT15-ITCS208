
public class Moogle {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String movieFile,ratingFile,userFile;
//		movieFile 	= MoogleIOController.readFileSource("Movie file Location");
//		ratingFile 	= MoogleIOController.readFileSource("Rating file Location");
//		userFile 	= MoogleIOController.readFileSource("User file Location");
		movieFile 	= "output/movies.csv";
		ratingFile 	= "output/ratings.csv";
		userFile 	= "output/users.csv";
		MoogleS program = new MoogleS();
		if(program.loadData(movieFile, ratingFile, userFile)) {
			program.start();
		}
	}

}


public class Moogle {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String movieFile,ratingFile,userFile;
		movieFile 	= MoogleIOController.readFileSource("Movie file Location");
		ratingFile 	= MoogleIOController.readFileSource("Rating file Location");
		userFile 	= MoogleIOController.readFileSource("User file Location");
		MoogleS program = new MoogleS(movieFile, ratingFile, userFile);
		if(program.loadData()) {
			program.start();
		}
	}

}

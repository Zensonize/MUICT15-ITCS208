
public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleMovieRecommender s = new SimpleMovieRecommender();
		s.loadData("small\\movies.csv", "small\\users.train.csv");
		s.trainModel("output\\micro.test.model");
		s.loadModel("output\\micro.test.model");
	}

}

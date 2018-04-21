
public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleMovieRecommender s = new SimpleMovieRecommender();
		s.loadData("micro\\movies.csv", "micro\\users.test.csv");
		s.trainModel("output\\micro.test.model");
		s.loadModel("output\\micro.test.model");
		System.out.println(s.predict(s.movies.get(108190), s.users.get(475)));
	}

}

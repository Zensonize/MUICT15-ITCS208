import java.util.List;
import java.util.Map;

public class SimpleMovieRecommender implements BaseMovieRecommender {

	@Override
	public Map<Integer, Movie> loadMovies(String movieFilename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, User> loadUsers(String ratingFilename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadData(String movieFilename, String userFilename) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<Integer, Movie> getAllMovies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void trainModel(String modelFilename) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadModel(String modelFilename) {
		// TODO Auto-generated method stub

	}

	@Override
	public double predict(Movie m, User u) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MovieItem> recommend(User u, int fromYear, int toYear, int K) {
		// TODO Auto-generated method stub
		return null;
	}

}

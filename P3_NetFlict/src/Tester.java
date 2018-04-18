import java.util.Map;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String movieFileName = "micro/movies.csv";
		String ratingFileName = "micro/users.test.csv";

		SimpleMovieRecommender s = new SimpleMovieRecommender();
		
		s.loadData(movieFileName, ratingFileName);
		Map<Integer, Movie> movies = s.getAllMovies();
		Map<Integer, User> users = s.getAllUsers();
		
		for(Integer key: movies.keySet()){
			System.out.println(movies.get(key).toString());
		}
		
		for(Integer key: users.keySet()){
			System.out.println(users.get(key).toString());
		}
		System.out.println("************************************");
		System.out.println("Total number of movies: " + movies.size());
		System.out.println("Total number of users: " + users.size());
	}

}

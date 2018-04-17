import java.util.Map;

public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String movieFileName = "data-micro/movies.csv";
		String ratingFileName = "data-micro/ratings.csv";

		SimpleMovieRecommender s = new SimpleMovieRecommender();
		
		s.loadData(movieFileName, ratingFileName);
		Map<Integer, Movie> movies = s.getAllMovies();
		Map<Integer, Rating> ratings;
		int numRatings = 0;
		
		for(Integer key: movies.keySet()){
			System.out.println(movies.get(key).toString());
			ratings = movies.get(key).getRating();
			numRatings += ratings.size();
			for(Integer uid: ratings.keySet()){
				System.out.println( "   " + ratings.get(uid).toString());
			}
		}
		System.out.println("************************************");
		System.out.println("Total number of movies: " + movies.size());
		System.out.println("Total number of ratings: " + numRatings);
	}

}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleMovieRecommender implements BaseMovieRecommender {
	
	Map<Integer, Movie> movies;
	Map<Integer, User> users;
	
	@Override
	public Map<Integer, Movie> loadMovies(String movieFilename) {
		// TODO Auto-generated method stub
		Map<Integer, Movie> movieStream = new HashMap<Integer, Movie>();
		
		try {
			File moviesCSV = new File(movieFilename);
			InputStream reader = new FileInputStream(moviesCSV);
			BufferedReader br = new BufferedReader(new InputStreamReader(reader));
			String stream = "";
			
			while((stream = br.readLine()) != null) {
				String regex = "(\\d+),(\"?)(.+) [(](\\d{4})[)](\"?),(.+)";
				
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(stream);
				
				if(m.find()) {
					int mid = Integer.parseInt(m.group(1));
					String title = m.group(3);
					int year = Integer.parseInt(m.group(4));
					String[] tags = m.group(6).split("\\|");
					
					movieStream.put(mid,new Movie(mid,title,year));
					
					for(String k: tags) {
						movieStream.get(mid).addTag(k);
					}
				}
									
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return movieStream;
	}

	@Override
	public Map<Integer, User> loadUsers(String ratingFilename) {
		// TODO Auto-generated method stub 
		Map<Integer, User> userStream = new HashMap<Integer, User>();
		
		try {
			File usersCSV = new File(ratingFilename);
			InputStream reader = new FileInputStream(usersCSV);
			BufferedReader br = new BufferedReader(new InputStreamReader(reader));
			String stream = "";
			
			while((stream = br.readLine()) != null) {
				
				String[] user = stream.split("\\,");
				int uid = Integer.parseInt(user[0]);
				int mid = Integer.parseInt(user[1]);
				double rating = Double.parseDouble(user[2]);
				long timestamp = Long.parseLong(user[3]);
				
				if(rating>0.00001) {
					if(userStream.containsKey(uid)) {
						userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
					}
					
					else {
						userStream.put(uid, new User(uid));
						userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
					}
				}
				
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return userStream;
	}

	@Override
	public void loadData(String movieFilename, String userFilename) {
		// TODO Auto-generated method stub
		movies = loadMovies(movieFilename);
		users = loadUsers(userFilename);
	}

	@Override
	public Map<Integer, Movie> getAllMovies() {
		// TODO Auto-generated method stub
		return movies;
	}

	@Override
	public Map<Integer, User> getAllUsers() {
		// TODO Auto-generated method stub
		return users;
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
		double p = u.getMeanRating();
		double sumU = 0, sumABSU = 0;
		boolean isRated = false;
		for(Integer userV: users.keySet()) {
			if(!users.get(userV).equals(u) && users.get(userV).ratings.containsKey(m.mid)) {								//get user which is not User u and has rated
				isRated = true;
				double similar = similarity(u, users.get(userV));
				sumU += similar*(users.get(userV).ratings.get(m.mid).rating - users.get(userV).getMeanRating());
				sumABSU += Math.abs(similar); 
			}
		}
		if(isRated && sumABSU != 0) return p+(sumU/sumABSU);
		else return 0;
	}

	public double similarity(User u, User v) {
		boolean isDisjoint = true;
		double sumUV = 0, sumUsq = 0, sumVsq = 0;
		for(Integer rated: u.getRating().keySet()) {
			if(v.getRating().containsKey(rated)) {
				if(u.getRating().get(rated).rating > 0 && v.getRating().get(rated).rating > 0) {
					isDisjoint = false;
					sumUV += (u.getRating().get(rated).rating - u.getMeanRating())*(v.getRating().get(rated).rating - v.getMeanRating());
					sumUsq += Math.pow(u.getRating().get(rated).rating - u.getMeanRating(), 2);
					sumVsq += Math.pow(v.getRating().get(rated).rating - v.getMeanRating(), 2);
				}
			}
		}
		if(isDisjoint) return 0;
		else return sumUV/((Math.pow(sumUsq, 0.5))*(Math.pow(sumVsq, 0.5)));
	}
	
	@Override
	public List<MovieItem> recommend(User u, int fromYear, int toYear, int K) {
		// TODO Auto-generated method stub
		return null;
	}
}

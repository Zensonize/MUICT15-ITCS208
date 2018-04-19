import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleMovieRecommender implements BaseMovieRecommender {
	
	Map<Integer, Movie> movies;
	Map<Integer, User> users;
	
//	double[][] ratingModel_mat = new double[users.size()][movies.size()+1];
//	double[][] usersimModel_mat = new double[users.size()][users.size()];
	
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
					
					if(!movieStream.containsKey(mid))movieStream.put(mid,new Movie(mid,title,year));
					
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
				String regex = "(\\d+),(\\d+),(\\d+)(.?)(\\d+?),(\\d+)";
				
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(stream);
				
				if(m.find()) {
					String[] user = m.group(0).split("\\,");
					int uid = Integer.parseInt(user[0]);
					int mid = Integer.parseInt(user[1]);
					double rating = Double.parseDouble(user[2]);
					long timestamp = Long.parseLong(user[3]);
					
					if(rating>0.00001) {														// User has rated that movie (> 0)
						if(userStream.containsKey(uid)) {										// If user exist 
							userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
						}
						
						else {
							userStream.put(uid, new User(uid));
							userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
						}
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
		double[][] rating_mat = new double[users.size()][movies.size()+1];
		double[][] usersim_mat = new double[users.size()][users.size()];
		int uidx = 0;
		for(Integer ukey: users.keySet()) {
			int midx = 0;
			for(Integer mkey: movies.keySet()) {
				if(users.get(ukey).ratings.containsKey(mkey)) rating_mat[uidx][midx] = users.get(ukey).ratings.get(mkey).rating;
				else rating_mat[uidx][midx] = 0.0;
				midx++;
			}
			
			int vidx = 0;
			for(Integer vkey: users.keySet()) {
				usersim_mat[uidx][vidx] = similarity(users.get(ukey), users.get(vkey));
				vidx++;
			}
			uidx++;
		}
		
		// Export model files
		System.out.println("@@@ Writing out model file");
		try {
			PrintWriter pw = new PrintWriter(new File(modelFilename));
			pw.println("@NUM_USERS " + users.size());
			pw.print("@USER_MAP {");
			int counter = 0;
			for(Integer key: users.keySet()) {
				pw.print(counter++ + "=" + users.get(key).uid);
				if(counter == users.size()) pw.println("}");
				else pw.print(", ");
			}
			pw.println("@NUM_MOVIES " + movies.size());
			pw.print("@MOVIE_MAP {");
			counter = 0;
			for(Integer key: movies.keySet()) {
				pw.print(counter++ + "=" + movies.get(key).mid);
				if(counter == movies.size()) pw.println("}");
				else pw.print(", ");
			}
			pw.println("@RATING_MATRIX");
			for(int i=0;i<users.size();i++) {
				for(int j=0;j<movies.size()+1;j++) {
					pw.print(rating_mat[i][j] + " ");
				}
				pw.println();
			}
			pw.println("@USERSIM_MATRIX");
			for(int i=0;i<users.size();i++) {
				for(int j=0;j<users.size();j++) {
					pw.print(usersim_mat[i][j] + " ");
				}
				pw.println();
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
		for(Integer rated: u.ratings.keySet()) {
			if(v.ratings.containsKey(rated)) {
				if(u.ratings.get(rated).rating > 0 && v.ratings.get(rated).rating > 0) {
					isDisjoint = false;
					sumUV += (u.ratings.get(rated).rating - u.getMeanRating())*(v.ratings.get(rated).rating - v.getMeanRating());
					sumUsq += Math.pow(u.ratings.get(rated).rating - u.getMeanRating(), 2);
					sumVsq += Math.pow(v.ratings.get(rated).rating - v.getMeanRating(), 2);
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

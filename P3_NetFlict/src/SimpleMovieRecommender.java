import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class SimpleMovieRecommender implements BaseMovieRecommender {

	Map<Integer, Movie> movies;
	Map<Integer, User> users;
	
	@Override
	public Map<Integer, Movie> loadMovies(String movieFilename) {
		// TODO Auto-generated method stub
		Map<Integer, Movie> movieStream = new HashMap<Integer, Movie>();
		
		try {
			File moviesCSV = new File(movieFilename);
			LineIterator it = FileUtils.lineIterator(moviesCSV, "UTF-8");
			String regex = "(\\d+),(\"?)(.+) [(](\\d{4})[)](\"?),(.+)";
			
			 try {
			   while (it.hasNext()) {
			     String stream = it.nextLine();
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
			 } finally {
			   LineIterator.closeQuietly(it);
			 }
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return movieStream;
	}

	@Override
	public Map<Integer, User> loadUsers(String userFilename) {
		// TODO Auto-generated method stub 
		Map<Integer, User> userStream = new HashMap<Integer, User>();
		
		try {
			File usersCSV = new File(userFilename);
			LineIterator it = FileUtils.lineIterator(usersCSV, "UTF-8");
			
			try {
				while (it.hasNext()) {
			     String[] stream = it.nextLine().split("\\,");
			     if(stream.length == 4) {
			    	 try {
			    		 int uid = Integer.parseInt(stream[0]);
				    	 int mid = Integer.parseInt(stream[1]);
				    	 double rating = Double.parseDouble(stream[2]);
				    	 long timestamp = Long.parseLong(stream[3]);
				    	 
				    	 if(userStream.containsKey(uid)) {										// If user exist 
				    		 userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
						 }
							
						 else {
							 userStream.put(uid, new User(uid));
							 userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
						 }
			    	 } catch(NumberFormatException e) {
//			    		 e.printStackTrace();
			    	 }
			    	 
			     }
				
			   }
			 } finally {
			   LineIterator.closeQuietly(it);
			 }
		} catch(FileNotFoundException e) {
			e.printStackTrace();
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
		return 0;
	}
	
	public double similarity(User u,User v) {
		if(u.equals(v)) return 1;
		if(u.ratings.size() <= 1 || v.ratings.size() <= 1) return 0;				// this case make r = average r --> denominator = 0
		
		List<Movie> intersec = new ArrayList<Movie>();
		
		for(Integer key: u.ratings.keySet()) {
			if(v.ratings.containsKey(key)) intersec.add(u.ratings.get(key).m);		// count no.of int
		}
		
		if(intersec.isEmpty()) return 0;											// user didn't rate same movie
		
		else {
			double sumA = 0,sumB = 0,sumC = 0;										// (sumA) / [(sumB)(sumC)]
			
			for(ListIterator<Movie> k = intersec.listIterator();k.hasNext();) {
				Movie cal = k.next();
				double dU = u.ratings.get(cal.mid).rating-u.getMeanRating();
				double dV = v.ratings.get(cal.mid).rating-v.getMeanRating();
				sumA += dU*dV;
				sumB += Math.pow(dU, 2);
				sumC =+ Math.pow(dV, 2);
			}
			return sumA/ ((Math.pow(sumB, 0.5))*(Math.pow(sumC, 0.5)));
		}
		
	}
	@Override
	public List<MovieItem> recommend(User u, int fromYear, int toYear, int K) {
		// TODO Auto-generated method stub
		return null;
	}

}

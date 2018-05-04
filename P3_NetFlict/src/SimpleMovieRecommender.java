//# Krittamet Kiattukilwattana
//# 6088063

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class SimpleMovieRecommender implements BaseMovieRecommender {

	Map<Integer,User> users = new HashMap<Integer,User>();
	Map<Integer,Movie> movies = new HashMap<Integer,Movie>();
	
	Map<Integer,Double> userMeanRating = new HashMap<Integer,Double>();
	//data to build model
	Map<Integer,Integer> movieIndex = new HashMap<Integer,Integer>(); //MovieID --> index
	Map<Integer,Integer> userIndex = new HashMap<Integer,Integer>();
	double[][] ratings_out;
	double[][] similarity_out;
	//data from model file
	double[][] ratings_Model;
	double[][] similarity_Model;
	Map<Integer,Integer> userIndex_Model = new HashMap<Integer,Integer>();
	Map<Integer,Integer> movieIndex_Model = new HashMap<Integer,Integer>();
	//for faster prediction
	Map<Integer,List<Movie>> movieYearMap = new HashMap<Integer,List<Movie>>();
	@Override
	public Map<Integer, Movie> loadMovies(String movieFilename) {
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
					
					for(String k: tags) {																			// add tags to movie
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
				    	 
				    	 if(userStream.containsKey(uid)) {															// If user exist just add rating 
				    		 if(rating>0.0) userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
						 }
							
						 else {
							 userStream.put(uid, new User(uid));													// Create new user and add rating
							 if(rating>0.0) userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
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
		int idx = 0;
		for(Integer K:movies.keySet()) {
			movieIndex.put(K, idx++);
		}
		idx = 0;
		for(Integer K:users.keySet()) {
			userIndex.put(K, idx++);
			userMeanRating.put(K, users.get(K).getMeanRating());
		}
	}

	@Override
	public Map<Integer, Movie> getAllMovies() {
		return movies;
	}

	@Override
	public Map<Integer, User> getAllUsers() {
		return users;
	}

	@Override
	public void trainModel(String modelFilename) {
		// TODO Auto-generated method stub
		try{
			PrintWriter pw = new PrintWriter(new File(modelFilename));
			
			pw.println("@NUM_USERS " + users.size());
			pw.print("@USER_MAP {");
			int idx = 0;
			for(Integer k:users.keySet()) {
				pw.print(idx++ + "=" + k);
				if(idx == users.size()) pw.println("}");
				else pw.print(", ");
			}
			pw.println("@NUM_MOVIES " + movies.size());
			pw.print("@MOVIE_MAP {");
			//-----------------------------------------------------------------------------------------------
			System.out.println("@@@ Computing user rating matrix");
			
			idx = 0;
			for(Integer k:movies.keySet()) {
				pw.print(idx++ + "=" + k);
				if(idx == movies.size()) pw.println("}");
				else pw.print(", ");
			}
			pw.println("@RATING_MATRIX");
			for(Integer k:users.keySet()) {
				pw.println(getRatingsArray(users.get(k),movies.size()+1, movieIndex));
			}
			int y=0,x=0;
			//-----------------------------------------------------------------------------------------------
			System.out.println("@@@ Computing user sim matrix");
			similarity_out = new double[users.size()][users.size()];
			for(Integer Y:users.keySet()) {
				x=0;
				for(Integer X:users.keySet()) {
					if(y<=x) {
						similarity_out[y][x] = similarity(users.get(Y), users.get(X));
						similarity_out[x][y] = similarity_out[y][x];
					}
					x++;
				}
				y++;
			}
			pw.println("@USERSIM_MATRIX");
			for(y = 0;y<similarity_out.length;y++) {
				for(x = 0;x<similarity_out[y].length-1;x++) {
					pw.print(similarity_out[y][x] + " ");
				}
				pw.println(similarity_out[y][x]);
			}
			//-----------------------------------------------------------------------------------------------
			System.out.println("@@@ Writing out model file");
			pw.close();
		} catch (FileNotFoundException e) {
			
		}
	}

	@Override
	public void loadModel(String modelFilename) {
		// TODO Auto-generated method stub
		String stream;
		try {
			File model = new File(modelFilename);
			LineIterator it = FileUtils.lineIterator(model, "UTF-8");
			try {
				int num_user = Integer.parseInt(it.nextLine().replace("@NUM_USERS ", ""));
				stream = it.nextLine().replace("@USER_MAP {", "");
				String[] userMap = stream.split("\\, ");
				for(String idlist:userMap) {
					Pattern p = Pattern.compile("(\\d+)=(\\d+)(}?)");
					Matcher m = p.matcher(idlist);
					if(m.find()) {
						userIndex_Model.put(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
					}
				}
				//-----------------------------------------------------------------------------------------------
				int num_movie = Integer.parseInt(it.nextLine().replace("@NUM_MOVIES ", ""));
				stream = it.nextLine().replace("@MOVIE_MAP {", "");
				String[] movieMap = stream.split("\\, ");
				for(String idlist:movieMap) {
					Pattern p = Pattern.compile("(\\d+)=(\\d+)(}?)");
					Matcher m = p.matcher(idlist);
					if(m.find()) {
						int mid = Integer.parseInt(m.group(2));
						int idx = Integer.parseInt(m.group(1));
						movieIndex_Model.put(mid,idx);
						
						//create a map of list of movie by each year
						if(movieYearMap.containsKey(movies.get(mid).year)) movieYearMap.get(movies.get(mid).year).add(movies.get(mid));
						else {
							List<Movie> aYear = new ArrayList<Movie>();
							aYear.add(movies.get(mid));
							movieYearMap.put(movies.get(mid).year,aYear);
						}
								
					}
				}
				//-----------------------------------------------------------------------------------------------
				it.nextLine();										//skip rating_matrix header
				ratings_Model = new double[num_user][num_movie+1];
				for(int i = 0;i<num_user;i++) {
					String[] ratingA = it.nextLine().split("\\ ");
					for(int x=0;x<ratingA.length;x++) {
						ratings_Model[i][x] = Double.parseDouble(ratingA[x]);
					}
				}
				//-----------------------------------------------------------------------------------------------
				it.nextLine();										//skip similarity matrix header
				similarity_Model = new double[num_user][num_user];
				for(int i = 0;i<num_user;i++) {
					String[] simA = it.nextLine().split("\\ ");
					for(int x=0;x<simA.length;x++) {
						similarity_Model[i][x] = Double.parseDouble(simA[x]);
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
	}

	@Override
	public double predict(Movie m, User u) {
		// TODO Auto-generated method stub
		if(!userIndex_Model.containsKey(u.uid)) return u.getMeanRating();
		int u_idx = userIndex_Model.get(u.uid);
		int m_idx = movieIndex_Model.get(m.mid);
		double sumU = 0.0,sumD = 0.0;
		for(int y=0;y<ratings_Model.length;y++) {
			if(y == u_idx) continue;
			if(ratings_Model[y][m_idx] > 0.0) {
					double s = similarity_Model[y][u_idx];
					sumU += s*(ratings_Model[y][m_idx] - ratings_Model[y][ratings_Model[y].length-1]);
					sumD += Math.abs(s);
			}
		}
		
		if(sumU == 0.0 || sumD == 0.0) return ratings_Model[u_idx][ratings_Model[u_idx].length-1];
		double toReturn = ratings_Model[u_idx][ratings_Model[u_idx].length-1] + (sumU/sumD);
		if(toReturn > 5.0) return 5.0;
		if(toReturn < 0.0) return 0;
		return toReturn;
		//use all data from .model 

	}

	@Override
	public List<MovieItem> recommend(User u, int fromYear, int toYear, int K) {
		// TODO Auto-generated method stub
		List<MovieItem> calculated = new ArrayList<MovieItem>();
		for(int i = fromYear;i<=toYear;i++) {
			if(movieYearMap.containsKey(i)) {
				for(Movie m:movieYearMap.get(i)) {
					calculated.add(new MovieItem(m, predict(m, u)));
				}
			}
		}
		
		Collections.sort(calculated);
		
		if(calculated.size() < K) return calculated;
		
		//Get top K movies
		List<MovieItem> toReturn = new ArrayList<MovieItem>();
		for(int i=0;i<K;i++) {
			toReturn.add(calculated.get(i));
		}
		return toReturn;
	}

	public double similarity(User u,User v) {
		double sumA = 0.0, sumB = 0.0, sumC = 0.0,rU,rV;
		if(u.uid == v.uid) return 1.0;
		else {
			User small = getSmall(u,v);													// User who rated lesser movie
			User big = getOther(u,v);													// User who rated more movie
			for(Integer mov: small.ratings.keySet()) {
				if(big.ratings.containsKey(mov)) {
					rU = big.ratings.get(mov).rating;
					rV = small.ratings.get(mov).rating;
					if(rU == 0.0 || rV == 0.0) continue;
					else {
						rU -= userMeanRating.get(big.uid);
						rV -= userMeanRating.get(small.uid);
						sumA += rU*rV;
						sumB += Math.pow(rU, 2);
						sumC += Math.pow(rV, 2);
					}
				}
			}
			if(sumB == 0.0 || sumC == 0.0) return 0.0;
			sumB = Math.sqrt(sumB);
			sumC = Math.sqrt(sumC);
			double sim = sumA/(sumB*sumC);
			if(Double.isNaN(sim)) return 0.0;
			if(sim > 1.0) 	return 1.0;						// fix sim = 1.0000000000000002 --> change to 1.0
			if(sim < -1.0) 	return -1.0;					// fix sim = -1.0000000000000002 --> change to -1.0
			return sim;
		}
	}
	
	//this method compare size of rating and return the user who has the smallest rating size
	private User getSmall(User u, User v){
		if(u.ratings.size() > v.ratings.size()) return v;
		return u;
	}
	//this method return the opposite of getSmall
	private User getOther(User u, User v){
		if(u.ratings.size() > v.ratings.size()) return u;
		return v;
	}
	
	//This method return string of rating of User u
	public String getRatingsArray(User u,int size, Map<Integer,Integer> index) {
		double[] ratingArr = new double[size];
		
		for(Integer k: u.ratings.keySet()) {
			ratingArr[index.get(u.ratings.get(k).m.mid)] = u.ratings.get(k).rating;
		}
		
		StringBuilder rA = new StringBuilder();
		for(int i=0;i<size-1;i++) {
			rA.append(ratingArr[i] + " ");
		}
		rA.append(u.getMeanRating());
		
		return rA.toString();
	}
}

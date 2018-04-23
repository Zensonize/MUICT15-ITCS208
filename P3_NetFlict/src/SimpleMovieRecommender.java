import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.google.common.collect.Sets;

import javafx.util.Pair;

public class SimpleMovieRecommender implements BaseMovieRecommender {

	Map<Integer,Movie> movies= new HashMap<Integer, Movie>();
	Map<Integer,User> users = new HashMap<Integer, User>();
	List<Integer> movieIndex = new ArrayList<Integer>();
	List<Integer> userIndex = new ArrayList<Integer>();
	Map<Integer,List<Integer>> movieClassifiedByYear = new HashMap<Integer,List<Integer>>();
	double[][] similarOutArray;
	List<double[]> similarArray = new ArrayList<double[]>();
	
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
					
					if(!movieStream.containsKey(mid)) {
						movieStream.put(mid,new Movie(mid,title,year));
						if(movieClassifiedByYear.containsKey(year)) movieClassifiedByYear.get(year).add(mid);
						else{
							List<Integer> mYear = new ArrayList<Integer>();
							mYear.add(mid);
							movieClassifiedByYear.put(year, mYear);
						}
					}
					
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
				    		 if(rating>0.0) userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
						 }
							
						 else {
							 userStream.put(uid, new User(uid));
							 if(rating>0.0) {
								 userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
								 movies.get(mid).ratedUser.add(userStream.get(uid));
							 }
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
		
		for(Integer k:movies.keySet()) {				//generate movie index
			movieIndex.add(k);
			movies.get(k).addIndex(idx++);
		}
		idx = 0;
		for(Integer k:users.keySet()) {					//generate user index
			userIndex.add(k);
			users.get(k).addIndex(idx++);
			users.get(k).addAveRating(users.get(k).getMeanRating());
		}
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
		
		try{
			PrintWriter pw = new PrintWriter(new File(modelFilename));
			
			pw.println("@NUM_USERS " + users.size());
			//----------------------------------------------------------
			pw.print("@USER_MAP {");
			StringBuilder um = new StringBuilder();
			for(int i =0;i<userIndex.size();i++) {
				um.append(i + "=" + userIndex.get(i));
				if(i == userIndex.size()-1) um.append("}");
				else um.append(" ");
			}
			pw.println(um.toString());
			//----------------------------------------------------------
			pw.println("@NUM_MOVIES " + movies.size());
			//----------------------------------------------------------
			pw.print("@MOVIE_MAP {");
			StringBuilder mm = new StringBuilder();
			for(int i =0;i<movieIndex.size();i++) {
				mm.append(i + "=" + movieIndex.get(i));
				if(i == movieIndex.size()-1) mm.append("}");
				else mm.append(" ");
			}
			pw.println(mm.toString());
			//----------------------------------------------------------
			System.out.println("@@@ Computing user rating matrix");
			pw.println("@RATING_MATRIX");
			for(int y = 0;y<userIndex.size();y++) {
				pw.println(users.get(userIndex.get(y)).getRatingArray(movieIndex));
			}
			//----------------------------------------------------------
			System.out.println("@@@ Computing user sim matrix");
			pw.println("@USERSIM_MATRIX");
			similarOutArray = new double[users.size()][users.size()];
			for(int y = 0;y<userIndex.size();y++) {
				for(int x = y;x<userIndex.size();x++) {
					similarOutArray[y][x] = similarity(users.get(userIndex.get(y)), users.get(userIndex.get(x)));
					similarOutArray[x][y] = similarOutArray[y][x];
				}
				
				for(int x = 0;x<userIndex.size();x++) {
					pw.print(similarOutArray[y][x] + " ");
				}
				pw.println();
				
			}
			
			//----------------------------------------------------------
			System.out.println("@@@ Writing out model file");
			pw.close();
		} catch (FileNotFoundException e) {
			
		}
		
	}

	@Override
	public void loadModel(String modelFilename) {
		// TODO Auto-generated method stub
		try {
			File model = new File(modelFilename);
			LineIterator it = FileUtils.lineIterator(model, "UTF-8");
			
			int numUser,numMov;
			try {
				String stream;
				while(!(stream = it.nextLine()).contains("@USERSIM_MATRIX")) {}
				while(it.hasNext()){
					stream = it.nextLine();
					String[] simArr = stream.split("\\ ");
					double[] simArrD = new double[simArr.length];
					for(int i =0;i<simArr.length;i++) {
						simArrD[i] = Double.parseDouble(simArr[i]);
					}
					similarArray.add(simArrD);
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

		double sum_upper = 0,sum_lower = 0;
		boolean isEmpty = true;
		if(m.ratedUser.isEmpty()) return 0;
		for(User v: m.ratedUser) {
			if(v.uid != u.uid) {
				double similar = similarArray.get(u.index)[v.index];
				sum_upper += similar*(v.ratings.get(m.mid).rating - v.avRating);
				sum_lower += Math.abs(similar);
			}
		}
		double res = sum_upper/sum_lower;
		return u.avRating + res;

	}
	
	public double similarity(User u,User v) {
		
		double sumA = 0.0,sumB = 0.0,sumC = 0.0,rU,rV;
		if(u.uid == v.uid) return 1.0;
		Set<Integer> mU = u.ratings.keySet();
		Set<Integer> mV = v.ratings.keySet();
		Set<Integer> ins = Sets.intersection(mU, mV);
		
		if(ins.size() == 1) return 0.0;
		for(Integer movU: ins) {
			
				rU = u.ratings.get(movU).rating - u.avRating;
				rV = v.ratings.get(movU).rating - v.avRating;
				sumA += rU*rV;
				sumB += Math.pow(rU, 2);
				sumC += Math.pow(rV, 2);
			
		}
		if(sumB == 0.0 || sumC == 0.0) return 0.0;
		sumB = Math.sqrt(sumB);
		sumC = Math.sqrt(sumC);
		double sim = sumA/(sumB*sumC);
		if(Double.isNaN(sim)) return 0.0;
		if(sim *1000 < 0.0) return 0.0;
		if(sim > 1.0) 	return 1.0;						//my sim = 1.0000000000000002 --> change to 1.0
		if(sim < -1.0) 	return -1.0;					//my sim = -1.0000000000000002 --> change to -1.0
		return sim;
	}
	
	@Override
	public List<MovieItem> recommend(User u, int fromYear, int toYear, int K) {
		List<MovieItem> recommendedMov = new ArrayList<MovieItem>();
		List<MovieItem> toReturn = new ArrayList<MovieItem>();
		
		for(int y = fromYear;y<=toYear;y++) {
			if(movieClassifiedByYear.containsKey(y)) {
				for(Integer m: movieClassifiedByYear.get(y)) {
					double predicted_V = predict(movies.get(m), u);
					recommendedMov.add(new MovieItem(movies.get(m),predicted_V));
				}
			}
		}
		
		Collections.sort(recommendedMov);
		
		for(int i = 0 ;i<K;i++) {
			if(i<recommendedMov.size()) {
				toReturn.add(recommendedMov.get(i));
			}
		}
		
//		for(Integer key: movies.keySet()) {
//			if(movies.get(key).year>= fromYear && movies.get(key).year <= toYear) {
//				double predicted_V = predict(movies.get(key), u);
//				recommendedMov.add(new MovieItem(movies.get(key),predicted_V));
//			}
//		}
//		
//		Collections.sort(recommendedMov);
//		
//		for(int i = 0 ;i<K;i++) {
//			if(i<recommendedMov.size()) {
//				toReturn.add(recommendedMov.get(i));
//			}
//		}
		return toReturn;
	}
	
	
}

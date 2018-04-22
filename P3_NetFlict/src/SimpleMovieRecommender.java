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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import javafx.util.Pair;

public class SimpleMovieRecommender implements BaseMovieRecommender {

	Map<Integer, Movie> movies;
	Map<Integer, User> users;
	Double[][] ratingModel,simModel;
	List<Integer> useridx = new ArrayList<Integer>();
	List<Integer> movieidx = new ArrayList<Integer>();
	List<Double[]> ratingArray = new ArrayList<Double[]>();
	List<Double[]> similarArray = new ArrayList<Double[]>();
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
				    		 if(rating>0.0) userStream.get(uid).addRating(movies.get(mid), rating, timestamp);
						 }
							
						 else {
							 userStream.put(uid, new User(uid));
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
		double[][] ratingMat = new double[users.size()][movies.size()+1];
		double[][] simMat = new double[users.size()][users.size()];
		int y=0,x=0;
		System.out.println("@@@ Computing user rating matrix");
		System.out.println("@@@ Computing user sim matrix");
		for(Integer user: users.keySet()) {
			x=0;
			for(Integer mov: movies.keySet()) {
				if(users.get(user).ratings.containsKey(mov)) ratingMat[y][x++] = users.get(user).ratings.get(mov).rating;
				else ratingMat[y][x++] = 0;
			}
			ratingMat[y][x] = users.get(user).getMeanRating();
			x=0;
			for(Integer userB: users.keySet()) {
				simMat[y][x++] = similarity(users.get(user), users.get(userB));
			}
			y++;
		}
		
		System.out.println("@@@ Writing out model file");
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
			idx = 0;
			for(Integer k:movies.keySet()) {
				pw.print(idx++ + "=" + k);
				if(idx == movies.size()) pw.println("}");
				else pw.print(", ");
			}
			pw.println("@RATING_MATRIX");
			for(y = 0;y<users.size();y++) {
				for(x = 0;x<movies.size();x++) {
					pw.print(ratingMat[y][x] + " ");
				}
				pw.println(ratingMat[y][x]);
			}
			pw.println("@USERSIM_MATRIX");
			for(y = 0;y<users.size();y++) {
				for(x = 0;x<users.size()-1;x++) {
					pw.print(simMat[y][x] + " ");
				}
				pw.println(simMat[y][x]);
			}
			
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
				it.nextLine();										//skip num_user
				String stream = it.nextLine().replace("@USER_MAP {", "");
				String[] userMap = stream.split("\\, ");
				for(String idlist:userMap) {
					Pattern p = Pattern.compile("(\\d+)=(\\d+)(}?)");
					Matcher m = p.matcher(idlist);
					if(m.find()) {
						useridx.add(Integer.parseInt(m.group(2)));
					}
				}
				it.nextLine();										//skip num_movies
				stream = it.nextLine().replace("@MOVIE_MAP {", "");
				String[] movieMap = stream.split("\\, ");
				for(String idlist:movieMap) {
					Pattern p = Pattern.compile("(\\d+)=(\\d+)(}?)");
					Matcher m = p.matcher(idlist);
					if(m.find()) {
						movieidx.add(Integer.parseInt(m.group(2)));
					}
				}
				it.nextLine();										//skip rating_matrix header
				int idxy = 0;
				while(!(stream = it.nextLine()).contains("@USERSIM_MATRIX")){
					String[] ratingArr = stream.split("\\ ");
					Double[] ratingArrD = new Double[ratingArr.length];
					for(int i =0;i<ratingArr.length;i++) {
						ratingArrD[i] = Double.parseDouble(ratingArr[i]);
					}
					ratingArray.add(ratingArrD);
				}
				while(it.hasNext()){
					stream = it.nextLine();
					String[] simArr = stream.split("\\ ");
					Double[] simArrD = new Double[simArr.length];
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
		// TODO Auto-generated method stub
		int u_idx = useridx.indexOf(u.uid);
		int m_idx = movieidx.indexOf(m.mid);
		double sum_upper = 0,sum_lower = 0;
		boolean isEmpty = true;
		for(int v_idx = 0;v_idx<ratingArray.size();v_idx++) {			
			if(v_idx!= u_idx && ratingArray.get(v_idx)[m_idx] > 0.01) {
				isEmpty = false;
				double similarity = similarArray.get(v_idx)[u_idx];
				sum_upper += similarity*(ratingArray.get(v_idx)[m_idx]- ratingArray.get(v_idx)[movieidx.size()]);
				sum_lower += Math.abs(similarity);
			}
		}
		if(isEmpty || sum_lower == 0) return 0;
		return ratingArray.get(u_idx)[movieidx.size()] + (sum_upper/sum_lower);
	}
	
	public double similarity(User u,User v) {
		double sumA = 0.0,sumB = 0.0,sumC = 0.0,rU,rV;
		if(u.uid == v.uid) return 1.0;
		for(Integer movU: u.ratings.keySet()) {
			if(v.ratings.containsKey(movU)){
				rU = u.ratings.get(movU).rating - u.getMeanRating();
				rV = v.ratings.get(movU).rating - v.getMeanRating();
				sumA += rU*rV;
				sumB += Math.pow(rU, 2);
				sumC += Math.pow(rV, 2);
			}
		}
		if(sumB == 0.0 || sumC == 0.0) return 0.0;
		sumB = Math.sqrt(sumB);
		sumC = Math.sqrt(sumC);
		double sim = sumA/(sumB*sumC);
		if(Double.isNaN(sim)) return 0.0;
		if(sim > 1.0) 	return 1.0;						//my sim = 1.0000000000000002 --> change to 1.0
		if(sim < -1.0) 	return -1.0;					//my sim = -1.0000000000000002 --> change to -1.0
		return sim;
	}
	
	@Override
	public List<MovieItem> recommend(User u, int fromYear, int toYear, int K) {
		List<MovieItem> recommendedMov = new ArrayList<MovieItem>();
		List<MovieItem> toReturn = new ArrayList<MovieItem>();
		
		for(Integer key: movies.keySet()) {
			if(movies.get(key).year>= fromYear && movies.get(key).year <= toYear) {
				double predicted_V = predict(movies.get(key), u);
				recommendedMov.add(new MovieItem(movies.get(key),predicted_V));
			}
		}
		
		Collections.sort(recommendedMov);
		
		for(int i = 0 ;i<K;i++) {
			if(i<recommendedMov.size()) {
				toReturn.add(recommendedMov.get(i));
			}
		}
		return toReturn;
	}
	
}

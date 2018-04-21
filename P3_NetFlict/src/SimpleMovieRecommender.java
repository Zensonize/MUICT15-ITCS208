import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
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
		List<MovieItem> recommended = new ArrayList<MovieItem>();
		List<MovieItem> toReturn = new ArrayList<MovieItem>();
		for(Integer key: movies.keySet()) {
			if(movies.get(key).year>= fromYear && movies.get(key).year <= toYear) {
				recommended.add(new MovieItem(movies.get(key),predict(movies.get(key), u)));
			}
		}
		if(!recommended.isEmpty()) Collections.sort(recommended);
		else System.out.println("Recommend is empty!");
		int idx = 0;
		for(ListIterator<MovieItem> topK = recommended.listIterator();topK.hasNext();) {
			idx++;
			toReturn.add(topK.next());
			if(idx == K) break;
		}
		return toReturn;
	}
	
}

//class RecommendComparator implements Comparator<MovieItem>{
//
//	@Override
//	public int compare(MovieItem arg0, MovieItem arg1) {
//		if(arg0.getScore()>arg1.getScore()) return -1;
//		else if(arg0.getScore()<arg1.getScore()) return 1;
//		return 0;
//	}
//	
//}

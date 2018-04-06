// Name: Krittamet Kiattikulwattana
// Student ID: 6088063
// Section: 1

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleMovieSearchEngine implements BaseMovieSearchEngine {
	public Map<Integer, Movie> movies;
	
	@Override
	public Map<Integer, Movie> loadMovies(String movieFilename) {
		
		Map<Integer, Movie> loadedData = new HashMap<Integer, Movie>();
		
		try {
			File movieData = new File(movieFilename);
			InputStream reader = new FileInputStream(movieData);
			BufferedReader read = new BufferedReader(new InputStreamReader(reader));
			String stream = "";
			
			while((stream = read.readLine()) != null) {
				String regex = "(\\d+),(\"?)(.+) [(](\\d{4})[)](\"?),(.+)";
				
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(stream);
				
				if(m.find()) {
					int mid = Integer.parseInt(m.group(1));												//-| 
					String title = m.group(3);															// | apply RegEx
					int year = Integer.parseInt(m.group(4));											// |
					String[] tags = m.group(6).split("\\|");											//-|
					
					loadedData.put(mid, new Movie(mid,title,year));
					
					for(String key: tags) {																//-| add tags
						loadedData.get(mid).addTag(key);
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return loadedData;
	}

	@Override
	public void loadRating(String ratingFilename) {
		Map<Integer,User> users = new HashMap<Integer,User>();
		try {
			File ratingData = new File(ratingFilename);
			InputStream reader = new FileInputStream(ratingData);
			BufferedReader read = new BufferedReader(new InputStreamReader(reader));
			String stream = "";
			
			while((stream = read.readLine()) != null) {
				
				String regex = "(\\d+),(\\d+),(\\d+.\\d+),(\\d+)";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(stream);
				
				if(m.find()) {
					
					int uid = Integer.parseInt(m.group(1));								// -|
					int movKey = Integer.parseInt(m.group(2));							//  |
					double rating = Double.parseDouble(m.group(3));						//	| Applying RegEx
					long timestamp = Long.parseLong(m.group(4));						// -|
					
					if(!users.containsKey(uid)) {										// -|
						users.put(uid, new User(uid));									//  | Create a user List to prevent user duplication
					}																	// -|
					
					if(movies.containsKey(movKey)) {
						if(movies.get(movKey).getRating().containsKey(uid)) {
							if(movies.get(movKey).getRating().get(uid).timestamp < timestamp) movies.get(movKey).getRating().replace(uid, new Rating(users.get(uid),movies.get(movKey),rating,timestamp)); 
						}
						else {
							movies.get(movKey).addRating(users.get(uid), movies.get(movKey), rating, timestamp);
						}
					}
				}
			}
			
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadData(String movieFilename, String ratingFilename) {
		movies = loadMovies(movieFilename);
		loadRating(ratingFilename);
	}
	

	@Override
	public Map<Integer, Movie> getAllMovies() {
		return movies;
	}

	@Override
	public List<Movie> searchByTitle(String title, boolean exactMatch) {

		List<Movie> searchRes = new ArrayList<Movie>();
		if(exactMatch){																					
			for(Integer key:movies.keySet()) {
				if(movies.get(key).getTitle().toLowerCase().equals(title.toLowerCase())) {				//exact match case
					searchRes.add(movies.get(key));
				}
			}
		}
		else {
			for(Integer key:movies.keySet()) {															// contains case
				if(movies.get(key).getTitle().toLowerCase().contains(title.toLowerCase())){
					searchRes.add(movies.get(key));
				}
			}
		}
		return searchRes;
	}

	@Override
	public List<Movie> searchByTag(String tag) {

		List<Movie> searchRes = new ArrayList<Movie>();
		for(Integer key:movies.keySet()) {
			if(movies.get(key).getTags().contains(tag)) {
				searchRes.add(movies.get(key));
			}
		}
		
		return searchRes;
	}

	@Override
	public List<Movie> searchByYear(int year) {

		List<Movie> searchRes = new ArrayList<Movie>();
		for(Integer key:movies.keySet()) {
			if(movies.get(key).getYear() == year) {
				searchRes.add(movies.get(key));
			}
		}
		
		return searchRes;
	}

	@Override
	public List<Movie> advanceSearch(String title, String tag, int year) {

		// YOUR CODE GOES HERE
		List<Movie> searchRes = new ArrayList<Movie>();
		for(Integer key:movies.keySet()) {
			if(year>1) {
				if(title!=null) {
					if(tag != null) {
						//------- Year + Tag + Title --------
						if(movies.get(key).getYear() == year && movies.get(key).getTags().contains(tag) && movies.get(key).getTitle().toLowerCase().contains(title.toLowerCase())) {
							searchRes.add(movies.get(key));
						}
					}
					else {
						//------- Year + Title --------
						if(movies.get(key).getYear() == year && movies.get(key).getTitle().toLowerCase().contains(title.toLowerCase())) {
							searchRes.add(movies.get(key));
						}
					}
				}
				else {	
					if(tag != null) {
						//------- Year + Tag --------
						if(movies.get(key).getYear() == year && movies.get(key).getTags().contains(tag)) {
							searchRes.add(movies.get(key));
						}
					}
					else {
						//------- Year --------
						searchRes = searchByYear(year);
					}
				}
			}
			else{
				if(title!=null) {
					if(tag != null) {
						//------- Tag + Title --------
						if(movies.get(key).getTags().contains(tag) && movies.get(key).getTitle().toLowerCase().contains(title.toLowerCase())) {
							searchRes.add(movies.get(key));
						}
					}
					else {
						//------- Title --------
						searchRes = searchByTitle(title, false);
					}
				}
				else {
					if(tag != null) {
						//------- Tag --------
						searchRes = searchByTag(tag);
					}
				}
			}
		}
		
		return searchRes;
		
	}

	@Override
	public List<Movie> sortByTitle(List<Movie> unsortedMovies, boolean asc) {
		
		if(asc) Collections.sort(unsortedMovies, new SortByTitleAsc());
		else Collections.sort(unsortedMovies,new SortByTitleDsc());
		return unsortedMovies;
	}

	@Override
	public List<Movie> sortByRating(List<Movie> unsortedMovies, boolean asc) {

		if(asc) Collections.sort(unsortedMovies, new SortByRatingAsc());
		else Collections.sort(unsortedMovies,new SortByRatingDsc());
		return unsortedMovies;
	}
		
}

//------------------- Additional Class for Sorting Function [Comparator] ----------------------------------------

class SortByTitleAsc implements Comparator<Movie>{
	public int compare(Movie a,Movie b) {
		return a.getTitle().compareTo(b.getTitle());
	}
}

class SortByTitleDsc implements Comparator<Movie>{
	public int compare(Movie a,Movie b) {
		return b.getTitle().compareTo(a.getTitle());
	}
}

class SortByRatingAsc implements Comparator<Movie>{
	public int compare(Movie a,Movie b) {
		return (int) ((a.getMeanRating()-b.getMeanRating())*10);
	}
}

class SortByRatingDsc implements Comparator<Movie>{
	public int compare(Movie a,Movie b) {
		return (int) ((b.getMeanRating()-a.getMeanRating())*10);
	}
}


//------------------- END of Additional Class for Sorting Function ----------------------------------------

// Name:
// Student ID:
// Section: 

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
		
		Map<Integer, Movie> movList = new HashMap<Integer, Movie>();
		try {
			
			File inputMov = new File(movieFilename);
			
			InputStream inputFS = new FileInputStream(inputMov);						// -|
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));		//  | Read File
			String stream = "";															// -|
			
			br.readLine(); 																// -| Skip header of .csv file
			
			while((stream = br.readLine()) != null) {
				Movie check = readMovieData(stream);
				if(check != null) {
					movList.put(check.getID(), check);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return movList;
	}

	@Override
	public void loadRating(String ratingFilename) {

		try {
			File inputRating = new File(ratingFilename);
			InputStream inputFS = new FileInputStream(inputRating);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			String stream = "";
			br.readLine();																// | Skip header of .csv file
			
			int line = 0;
			
			while((stream = br.readLine()) != null) {
				
				String regex = "(\\d+),(\\d+),(\\d+.\\d+),(\\d+)";						// -|
				Pattern p = Pattern.compile(regex);										//  | Basic thing for RegEx
				Matcher m = p.matcher(stream);											// -|
				
				Map<Integer,User> userList = new HashMap<Integer,User>();
				
				if(m.find()) {
					int uid = Integer.parseInt(m.group(1));								// -|
					int movKey = Integer.parseInt(m.group(2));							//  |
					double rating = Double.parseDouble(m.group(3));						//	| Applying RegEx
					long timestamp = Long.parseLong(m.group(4));						// -|
					
					if(!userList.containsKey(uid)) {									// -|
						userList.put(uid, new User(uid));								//  |Create a user List to prevent user duplication
					}																	// -|
					
					if(movies.containsKey(movKey)) {
						movies.get(movKey).addRating(userList.get(uid), movies.get(movKey), rating, timestamp);
					}
					else System.out.println("No movies");
				}
				else System.out.println("Wrong Rating regex @line:" + line);
				line++;
			}
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
				if(movies.get(key).getTitle().toLowerCase().equals(title.toLowerCase())) {
					searchRes.add(movies.get(key));
				}
			}
		}
		else {
			for(Integer key:movies.keySet()) {
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
		
	private Movie readMovieData(String stream) {
		String regx1 = "(\\d+),\"(.+) [(](\\d{4})[)]\",(.+)";
		String regx2 = "(\\d+),(.+) [(](\\d{4})[)],(.+)";
		
		String title = null;															// -|
		int mid = -1;																	//	| Initialize value
		int year = -1;																	// -|
		String tags =  null;
		boolean found = false;
		
		Pattern p = Pattern.compile(regx1);
		Matcher m = p.matcher(stream);
		Movie mov = null;
		
		if(found = m.find()) {
			mid = Integer.parseInt(m.group(1));
			title = m.group(2);
			year = Integer.parseInt(m.group(3));
			tags = m.group(4);
		}
		
		if(!found) {
			p = Pattern.compile(regx2);
			m=p.matcher(stream);
			
			if(found = m.find()) {
				mid = Integer.parseInt(m.group(1));
				title = m.group(2);
				year = Integer.parseInt(m.group(3));
				tags = m.group(4);
			}
		}
		
		if(!found || mid == -1 || title == null || year == -1) {
			return null;
		}
		
		mov = new Movie(mid,title,year);
		
		Pattern pt = Pattern.compile("[^| ]+");										// -|
		Matcher mt = pt.matcher(tags);												//  |
																					//  | Split Tags
		while(mt.find()) {															//  |
			mov.addTag(mt.group(0));												//  |
		}
		
		return mov;
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

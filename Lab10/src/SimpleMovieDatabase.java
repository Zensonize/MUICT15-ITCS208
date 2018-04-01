//#Krittamet 6088063

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class SimpleMovieDatabase {
	public Map<Integer, Movie> movies = null;
	
	public void importMovies(String movieFilename)
	{	
		movies = new HashMap<Integer, Movie>();
		try {
			File inputMovie = new File(movieFilename);
			InputStream inputFS = new FileInputStream(inputMovie);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			String stream = "";
			
			while((stream = br.readLine()) != null) {
				
				String regex = "(\\d+),(\\w[\\S ]+),(.+)";				 				// -|
				Pattern p = Pattern.compile(regex);										//  | Basic thing for RegEx
				Matcher m = p.matcher(stream);											// -|
				
				if(m.find()) {
					int id = Integer.parseInt(m.group(1));
					String title = m.group(2);
					String tags = m.group(3);
					if(tags.contains("(")) tags = " ";
					Set<String> tag = new HashSet<String>();
					p = Pattern.compile("[^| ]+");
					m = p.matcher(tags);
					while(m.find()) {
						tag.add(m.group(0));
					}
					movies.put(id, new Movie(id,title));
					if(!title.equals(" ")) {
						movies.get(id).addTags(tag);
					}
					if(movies.get(id).tags.isEmpty()) movies.get(id).addTags("(no genres listed)");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//-------------------BONUS----------------------
	public List<Movie> searchMovies(String query) 
	{
		//YOUR BONUS CODE GOES HERE
		List<Movie> searchRes = new ArrayList<Movie>();
		for(Integer key:movies.keySet()) {
			if(movies.get(key).title.toLowerCase().contains(query.toLowerCase())){
				searchRes.add(movies.get(key));
			}
		}
		return searchRes;
	}
	
	public List<Movie> getMoviesByTag(String tag)
	{
		//YOUR BONUS CODE GOES HERE
		List<Movie> searchRes = new ArrayList<Movie>();
		for(Integer key:movies.keySet()) {
			if(movies.get(key).tags.contains(tag)) {
				searchRes.add(movies.get(key));
			}
		}
		
		return searchRes;
	}
	
	
	public static void main(String[] args)
	{
		SimpleMovieDatabase mdb = new SimpleMovieDatabase();
		mdb.importMovies("lab11_movies.txt");
		System.out.println("Done importing "+mdb.movies.size()+" movies");
		int[] mids = new int[]{139747, 141432, 139415, 139620, 141305};
		for(int mid: mids)
		{
			System.out.println("Retrieving movie ID "+mid+": "+mdb.movies.get(mid));
		}
		
		//Uncomment for bonus
		System.out.println("\n////////////////////////// BONUS ///////////////////////////////");
		String[] queries = new String[]{"america", "thai", "thailand"};
		for(String query: queries)
		{
			System.out.println("Results for movies that match: "+query);
			for(Movie m: mdb.searchMovies(query))
			{
				System.out.println("\t"+m);
			}
			System.out.println();
		}
		
		String[] tags = new String[]{"Musical", "Action", "Thriller"};
		for(String tag: tags)
		{
			System.out.println("Results for movies in category: "+tag);
			for(Movie m: mdb.getMoviesByTag(tag))
			{
				System.out.println("\t"+m);
			}
			System.out.println();
		}
		
		
	}

}

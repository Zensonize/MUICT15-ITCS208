import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoogleS {
	
	public Map<Integer, Movie> movies;
	public Map<Integer, User> users;
	String movieFile,ratingFile,userFile;
	int numRating = 0;
	
	public MoogleS() {
		movies = new HashMap<Integer, Movie>();
		users  = new HashMap<Integer,User>();
	}
	
	public boolean loadData(String movieFile, String ratingFile, String userFile){
		movies = loadMovies(movieFile);
		if(!movies.isEmpty()) {
			File user = new File(userFile);
			if(user.exists()) users = loadUsers(user);
			loadRating(ratingFile);
			System.out.println("Loaded " + movies.size() + " movies");
			System.out.println("Loaded " + numRating + " ratings");
			System.out.println("Loaded " + users.size() + " users");
			System.out.println("=========================================\n");
			return true;
		}
		System.out.println("Empty Movie");
		return false;
	}
	
	public Map<Integer, Movie> loadMovies(String movieFile){
		Map<Integer, Movie> loadedData = new HashMap<Integer, Movie>();
		
		try {
			File movieData = new File(movieFile);
			InputStream reader = new FileInputStream(movieData);
			BufferedReader read = new BufferedReader(new InputStreamReader(reader));
			String stream = "";
			
			while((stream = read.readLine()) != null) {
				String regex = "(\\d+),(\"?)(.+) [(](\\d{4})[)](\"?),(.+)";
				
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(stream);
				
				if(m.find()) {
					int mid = Integer.parseInt(m.group(1));
					String title = m.group(3);
					int year = Integer.parseInt(m.group(4));
					String[] tags = m.group(6).split("\\|");
					
					loadedData.put(mid, new Movie(mid,title,year));
					
					for(String key: tags) {
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
	
	public Map<Integer, User> loadUsers(File userFile){
		Map<Integer, User> loadedData = new HashMap<Integer, User>();
		
		try {
			File userData = userFile;
			InputStream reader = new FileInputStream(userData);
			BufferedReader read = new BufferedReader(new InputStreamReader(reader));
			String stream = "";
			
			while((stream = read.readLine()) != null) {
				String regex = "(\\d+),(.+),(.+)";
				
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(stream);
				
				if(m.find()) {
					int uid = Integer.parseInt(m.group(1));								// -|
					String uname = m.group(2);											//  | Applying RegEx
					String pwd = m.group(3);											// -|
					
					loadedData.put(uid, new User(uid,uname,pwd));
					
				}
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return loadedData;
	}
	
	public void loadRating(String ratingFile) {
		
		try {
			File ratingData = new File(ratingFile);
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
							numRating++;
						}
					}
				}
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void start() {
		System.out.println("Welcome to Moogle");
		System.out.println("=============================\n");
		System.out.println("\t[0] Register\n\t[1] Login\n\t[2] Skip Login and Use Moogle Search Engine\n\n\t[E] Exit\n");
		switch(MoogleIOController.readChar('0', '2')) {
			case '0':	MoogleRegister.register(this);			start();		break;
			case '1': 	MoogleLogin.login(this);				start();		break;
			case '2': 	MoogleSearchEngine.searchEngine(this);	start();		break;
			case 'E': 	break;
			case 'e':	break;
			default: start();
		}
	}
	
	public Map<Integer, Movie> getMovies(){
		return movies;
	}
	
	public Map<Integer, User> getUser(){
		return users;
	}
}

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sound.midi.SoundbankResource;

public class MoogleSystem {
	
	Map<Integer,User> users;
	Map<Integer, Movie> movies;
	SimpleMovieSearchEngine s = new SimpleMovieSearchEngine();
	
	public MoogleSystem(String movieFileName,String ratingFileName, String userFileName) {
		
		s.loadData(movieFileName, ratingFileName, userFileName);
		movies = s.getAllMovies();
		users = s.getAllUsers();
		while(MoogleHome());
	}
	
	public boolean MoogleHome() {

		System.out.println("Welcome to Moogle");
		System.out.println("=============================\n");
		System.out.println("\t[0] Register\n\t[1] Login\n\t[2] Skip Login and Use Moogle Search Engine\n\n\t[E] Exit\n");
		System.out.print("\nYour Choice: ");
		Scanner in = new Scanner(System.in);
		switch(in.next().charAt(0)) {
			case '0': while(Register()); return true;
			case '1': while(Login()); return true;
			case '2': while(SearchEngine(null)); return true;
			case 'E': return false;	
		}
		return true;
	}
	
	private boolean Register() {
		int UserID;
		String Password,PasswordC;
		boolean isDuplicate = false;
		
		System.out.println("Register");
		System.out.println("=============================\n");
		System.out.print("UserID: ");
		Scanner in = new Scanner(System.in);
		UserID = in.nextInt();
		System.out.print("Password: ");
		Password = in.next();
		System.out.print("Confirm Password: ");
		PasswordC = in.next();
		if(!Password.equals(PasswordC)) {
			System.out.println("Error Password Not match");
			System.out.println("Refreshinging in 5 seconds");
			wait(5);
			return true;
		}
		
		for(Integer key: users.keySet()) {
			if(users.get(key).getID() == UserID) {
				isDuplicate=true;
				break;
			}
		}
		if(isDuplicate) {
			System.out.println("Error Duplicated UserID");
			System.out.println("Refreshinging in 5 seconds");
			wait(5);
			return true;
		}
		users.put(UserID, new User(UserID, MD5(Password)));
		return false;
	}
	
	private boolean Login() {
		int UserID = -9999;
		String Password = "";
		
		System.out.println("Login");
		System.out.println("=============================\n");
		System.out.print("UserID: ");
		Scanner in = new Scanner(System.in);
		UserID = in.nextInt();
		System.out.print("Password: ");
		Password = in.next();
		if(users.containsKey(UserID)) {
			if(users.get(UserID).getPassword().equals(MD5(Password))) {
				while(Home(users.get(UserID)));
				return false;
			}
		}
		System.out.println("Incorrect UserID or Password");
		return true;
	}
	
	private boolean Home (User me) {
		if(me.getName() != null) System.out.println("Welcome to Moogle " + users.get(me.getID()).getName());
		else System.out.println("Welcome to Moogle " + me.getID());
		System.out.println("=============================\n");
		System.out.println("\t[0] Search Engine\n\t[1] Edit my Account\n\n\t[E] Log Out\n");
		System.out.print("\nYour Choice: ");
		Scanner in = new Scanner(System.in);
		switch(in.next().charAt(0)) {
			case '0': while(SearchEngine(me)); return true;
			case '1': while(EditAccount(me)); return true;
			case 'E': return false;	
			default : return true;
		}
	}
	
	public boolean SearchEngine(User me) {
		List<Movie> results;

		System.out.println("Movie Search Engine");
		System.out.println("=============================\n");
		System.out.println("Search Guide:\t\t-m <Title> to Filter by Title");
		System.out.println("\t\t\t-y <Year> to Filter by Year");
		System.out.println("\t\t\t-t  <Tags> to Filter by Tags");
		System.out.println("\t\t\t-r  <Ratings> to Filter by Ratings\n");
		System.out.println("\t\t\t-s  <Sorting> change Sorting Behavior (title as default)");
		
		System.out.println("\t\t\t-----------------------------------\n");
		System.out.println("\t\t\t/i  <Tags>, to Include Specific Tags");
		System.out.println("\t\t\t/x  <Tags>, to Exclude Specific Tags");
		System.out.println("\t\t\t =  <Year or Ratings> to get the exact value");
		System.out.println("\t\t\t <  <Year or Ratings> to Include value less than the given value");
		System.out.println("\t\t\t >  <Year or Ratings> to Include value more than the given value");
		System.out.println("\t\t\t |  <Year or Ratings> to Include value between the given value\n");
		System.out.println("\t\t\t/1  <Sorting> to sort by Title  in Descending Order");
		System.out.println("\t\t\t/2  <Sorting> to sort by Rating in Ascending Order");
		System.out.println("\t\t\t/3  <Sorting> to sort by Rating in Descending Order");
		System.out.println("\t\t\t/4  <Sorting> to sort by Year   in Ascending Order");
		System.out.println("\t\t\t/5  <Sorting> to sort by Year   in Descending Order");
		System.out.println("\t\t\t/6  <Sorting> to sort by Rating in Descending Order then by Rating in Ascending Order");

		System.out.println("\t\t\t-----------------------------------\n");
		System.out.println("Ex. -m <title> -t /i <Tag1>,<Tag2> /x <Tag3>,<Tag4> -y | <Year1>,<Year2> -s /4");
		System.out.println("\n[E] Back");
		System.out.println("\nSearch: ");
		Scanner in = new Scanner(System.in);
		String Search = in.nextLine();
		if(Search.charAt(0) == 'E') return false;
		results = SearchCompiler(Search);
		System.out.println("==================== Results ====================");
		for(ListIterator<Movie> res = results.listIterator(); res.hasNext();) {
			System.out.println(res.nextIndex() + " " + res.next());
		}
		System.out.println("=================================================");
		System.out.println("Found " + results.size() + " movies");
		
		System.out.println("\n-----------------------------------------");
		if(me != null) {
			System.out.println("\t[R] Rate the [i]th movie");
			System.out.println("\t[S] Search Again");
		}
		else System.out.println("\t[S] Search Again");
		System.out.println("\n\t[E] Exit");
		
		switch(in.next().charAt(0)) {
			case 'R': 
				if(me == null) {System.out.println("Sorry You have to login first"); wait(5); return false;}
				else {
					System.out.println("Enter movie Index: ");
					RateMovie(me,in.nextInt());
					return false;
				}
			case 'S': return true;
			case 'E': return false;
		}
		
		return true;
	}
	
	public boolean RateMovie(User me, int index) {
		return false;
	}
	
	public boolean EditAccount(User me) {

		if(me.getName() != null) System.out.println("Edit Account " + users.get(me.getID()).getName());
		else System.out.println("Edit Account " + me.getID());
		System.out.println("=============================\n");
		System.out.println("\tUSER ID: " + me.getID());
		if(me.getName() != null) System.out.println("\tUsername: " + me.getName());
		else System.out.println("\tUsername: " + "NULL");
		System.out.println("\t-----------------------------------\n");
		System.out.println("\n\t[0] Change Username\n\t[1] Change Password\n\n\t[E] Back\n");
		Scanner in = new Scanner(System.in);
		switch(in.next().charAt(0)) {
			case '0': while(SetName(me)); return true;
			case '1': while(SetPassword(me)); return true;
			case 'E': return false;	
			default : return true;
		}
	}
	
	private List<Movie> SearchCompiler(String request){
		List<Movie> results = new ArrayList<Movie>();
		
		String regx_title = "-m ([\\d\\S]+)";														//-|	
		String regx_iTags = "-t.+/i ([\\S]+)";													// |
		String regx_xTags = "-t.+/x ([\\S]+)";													// |
		String regx_year0 = "-y (|) (\\d{4}),(\\d{4})";											// | All RegEx to compile search request
		String regx_year1 = "-y ([<>|=]) (\\d{4})";												// |
		String regx_rate0 = "-r (|) (\\d{4}),(\\d{4})";											// |
		String regx_rate1 = "-r ([<>|]=) (\\d{4})";												// |
		String regx_sort = "-s /(\\d)";															//-|
		
		boolean haveTitle = false;
		boolean haveYear = false;
		boolean haveTags = false;
		
		int tag_type = -1;																		//-| 0: include only  1: exclude only 	2: both
		int year_type = -1;																		//-| 0: Equals 		  1: Less than		2: More than 	3.Between
		int rating_type = -1;																	//-| 0: Equals 		  1: Less than		2: More than 	3.Between
		int sort_type = 0;
		
		String title = null;
		int[] year = {0,0};
		int[] rate = {0,0};
		String[] includeTags = new String[20];
		String[] excludeTags = new String[20];
		
		Pattern p = Pattern.compile(regx_title);												//-|
		Matcher m = p.matcher(request);															// | Compile Title															
		if(haveTitle = m.find()) {																// |
			title = m.group(1);																	//-|
		}
		else{
			System.out.println("Wrong title regx");
		}
		
//		System.out.println(title);
		
		p = Pattern.compile(regx_iTags);														//-|
		m = p.matcher(request);																	// |
		if(m.find()) {																			// | Compile Included tags
			tag_type = 0;																		// |
			System.out.println(m.group(1));
			includeTags = m.group(1).split(",");												//-|
		}
		
		p = Pattern.compile(regx_xTags);														//-|
		m = p.matcher(request);																	// |
		if(m.find()) {																			// |
			if(tag_type == 0) tag_type = 2;														// | Compile Excluded tags
			else tag_type = 1;																	// |
			excludeTags = m.group(1).split(",");												// |
		}																						//-|
//		System.out.println("Include tags: " + includeTags[0] );
		p = Pattern.compile(regx_year0);														//-|
		m = p.matcher(request);																	// |
		if(m.find()) {																			// |
			year_type = 3;																		// |
			year[0] = Integer.parseInt(m.group(2));												// |
			year[1] = Integer.parseInt(m.group(3));												// |
		}																						// |
																								// | Compile Year
		if(year_type == -1) {																	// |
			p = Pattern.compile(regx_year1);													// |
			m = p.matcher(request);																// |
			if(m.find()) {																		// |
				year[0] = Integer.parseInt(m.group(2));											// |
				switch(m.group(1).charAt(0)) {													// |
				case '=' : year_type = 0; break;												// |
				case '<' : year_type = 1; break;												// |
				case '>' : year_type = 2; break;												// |
				}																				// |
			}																					// |
		}																						//-|
//		
		System.out.println("year: " + year[0]);
		
		p = Pattern.compile(regx_rate0);														//-|
		m = p.matcher(request);																	// |
		if(m.find()) {																			// |
			rating_type = 3;																	// |
			rate[0] = Integer.parseInt(m.group(2));												// |
			rate[1] = Integer.parseInt(m.group(3));												// |
		}																						// |
																								// | Compile Ratings
		if(rating_type == -1) {																	// |
			p = Pattern.compile(regx_rate1);													// |
			m = p.matcher(request);																// |
			if(m.find()) {																		// |
				rate[0] = Integer.parseInt(m.group(2));											// |
				switch(m.group(1).charAt(0)) {													// |
				case '=' : rating_type = 0; break;												// |
				case '<' : rating_type = 1; break;												// |
				case '>' : rating_type = 2; break;												// |
				}																				// |
			}																					// |
		}	
		
		System.out.println("title: " + title);
		System.out.println("Year: " + year[0] + "," + year[1]);
		System.out.println("Included tags " + includeTags[0]);
		System.out.println("Excluded tags " + excludeTags[0]);
		System.out.println("Ratings " + rate[0] + "," + rate[1]);
		
		//--------------- Begin Searching --------------- 
		
		if(haveTitle) {																			//-|
			results = s.searchByTitle(title, false);											// | Search for title first (If have)
		}																						//-|
		
				//----------------------- Year Searching ------------------------------------------
		if(!haveTitle) {																		//-| Searching using year since Title is not present
			switch(year_type) {
				case 0 : results = s.searchByYear(year[0]); break;								//-| Search by exact Year
				
				case 1 : 
					for(int i = 1000;i< year[0];i++) {												//-|
						List<Movie> subResult = new ArrayList<Movie>();								// | 
						subResult = s.searchByYear(i);												// |
						if(!subResult.isEmpty()) {													// |
							for(ListIterator<Movie> a = subResult.listIterator(); a.hasNext();) {	// | Search for Older Year
								Movie temp = a.next();												// |
								if(!results.contains(temp)) results.add(temp);						// |
							}																		// |
						}																			// |
					} break;																		//-|
				
				
				case 2 : 
					for(int i = 2019;i>year[0];i--) {												//-|
						List<Movie> subResult = new ArrayList<Movie>();								// |
						subResult = s.searchByYear(i);												// |
						if(!subResult.isEmpty()) {													// |
							for(ListIterator<Movie> a = subResult.listIterator(); a.hasNext();) {	// | Search for Newer Year
								Movie temp = a.next();												// |
								if(!results.contains(temp)) results.add(temp);						// |
							}																		// |
						}																			// |
					} break;																		//-|
				
				case 3 : 
					for(int i =  year[0];i< year[1];i++) {											//-|
						List<Movie> subResult = new ArrayList<Movie>();								// |
						subResult = s.searchByYear(i);												// |
						if(!subResult.isEmpty()) {													// | Search by the year between
							for(ListIterator<Movie> a = subResult.listIterator(); a.hasNext();) {	// |
								Movie temp = a.next();												// |
								if(!results.contains(temp)) results.add(temp);						// |
							}																		// |
						}																			// |
					} break;																		//-|
				
			}
		}
		
		else {																												//-|
																															// |
			for(ListIterator<Movie> a = results.listIterator(); a.hasNext();) {												// |
				Movie temp = a.next();																						// |
				switch(year_type) {																							// |
				case 0: if(temp.getYear() != year[0]) results.remove(temp);										break;		// |
				case 1: if(temp.getYear() > year[0]) results.remove(temp);										break;		// | Apply Filtering by year
				case 2: if(temp.getYear() < year[0]) results.remove(temp);										break;		// |
				case 3: if(temp.getYear() < year[0] || temp.getYear() > year[1]) results.remove(temp);			break;		// |
																															// |
				}																											// |
			}																												// |
		}																													//-|
				
				//----------------------- Tags Searching ------------------------------------------
		
		if(!haveTitle && !haveYear) {
			switch(tag_type) {			//0:include 1: exclude 2: both
			case 0: 
				for(int i=0;i<includeTags.length;i++) {																		//-|
					List<Movie> subResult = s.searchByTag(includeTags[i]);													// |
					for(ListIterator<Movie> a = subResult.listIterator(); a.hasNext();) {									// |
						Movie temp = a.next();																				// | Search for movie according to tag
						if(!results.contains(temp))results.add(temp);														// |
					}																										// |
				}																											//-|
				
			case 1:
				for(Integer key: s.getAllMovies().keySet()){																//-|
					results.add(s.getAllMovies().get(key));																	// | Get All movies first
				}																											// |
																															// |
				for(ListIterator<Movie> a = results.listIterator(); a.hasNext();) {											// | Eliminate with excluded tags
					Movie temp = a.next();																					// |
					for(int i=0;i<excludeTags.length;i++) {																	// |
						if(temp.getTags().contains(excludeTags[i])) results.remove(temp);									// |
					}																										// |
				}																											//-|
				
				break;
			
			
			case 2: 
				for(Integer key: s.getAllMovies().keySet()) {																				//-|
					Movie temp = s.getAllMovies().get(key);																					// |
					for(int i=0;i<includeTags.length;i++) {																					// | get all Included tags without Excluded tags
						if(temp.getTags().contains(includeTags[i]) && !temp.getTags().contains(excludeTags[i])) results.add(temp);			// |
					}																														// |
				} break;																													//-|
			}																															
		}
		else {
			switch(tag_type) {			//0:include 1: exclude 2: both
			case 0: 
				for(ListIterator<Movie> a = results.listIterator(); a.hasNext();) {															//-|
					Movie temp = a.next();																									// |
					for(int i=0;i<includeTags.length;i++) {																					// | remove all non relevant movies
						if(!temp.getTags().contains(includeTags[i])) results.remove(temp);													// |
					}																														// |
				} break;																													//-|
				
			case 1:
				for(ListIterator<Movie> a = results.listIterator(); a.hasNext();) {															//-|
					Movie temp = a.next();																									// |
					for(int i=0;i<excludeTags.length;i++) {																					// | Filtered all relevant tags
						if(temp.getTags().contains(excludeTags[i])) results.remove(temp);													// |
					}																														// |
				} break;																													//-|
			
			
			case 2: 
				for(ListIterator<Movie> a = results.listIterator(); a.hasNext();) {															//-|
					Movie temp = a.next();																									// |
					for(int i=0;i<includeTags.length;i++) {																					// | Filtered all not relevant tags
						if(!temp.getTags().contains(includeTags[i]) && temp.getTags().contains(excludeTags[i])) results.remove(temp);		// |
					}																														// |
				} break;																													//-|
			}
		}
		
		
				//----------------------- Rating Searching ------------------------------------------
		if(!haveTitle && !haveYear && !haveTags) {												//-| Searching using Ratings since Title Year and Tags is not present
			switch(rating_type) {
				case 0 : results = s.searchByRating(rate[0]); break;							//-| Search by exact Ratings
				
				case 1 : for(double i = 0;i < rate[0];i+=0.1) {									//-|
					List<Movie> subResult = new ArrayList<Movie>();								// | 
					subResult = s.searchByRating(i);											// |
					if(!subResult.isEmpty()) {													// |
						for(ListIterator<Movie> a = subResult.listIterator(); a.hasNext();) {	// | Search for Lower Ratings
							Movie temp = a.next();												// |
							if(!results.contains(temp)) results.add(temp);						// |
						}																		// |
					}																			// |
				} break;																		//-|
				
				
				case 2 : for(double i = 10;i>rate[0];i-=0.1) {									//-|
					List<Movie> subResult = new ArrayList<Movie>();								// |
					subResult = s.searchByRating(i);											// |
					if(!subResult.isEmpty()) {													// |
						for(ListIterator<Movie> a = subResult.listIterator(); a.hasNext();) {	// | Search for Higher Ratings
							Movie temp = a.next();												// |
							if(!results.contains(temp)) results.add(temp);						// |
						}																		// |
					}																			// |
				} break;																		//-|
				
				case 3 : for(double i =  rate[0];i< rate[1];i+=0.1) {							//-|
					List<Movie> subResult = new ArrayList<Movie>();								// |
					subResult = s.searchByRating(i);											// |
					if(!subResult.isEmpty()) {													// | Search by the Ratings between
						for(ListIterator<Movie> a = subResult.listIterator(); a.hasNext();) {	// |
							Movie temp = a.next();												// |
							if(!results.contains(temp)) results.add(temp);						// |
						}																		// |
					}																			// |
				} break;																		//-|
				
			}
		}
		
		else {																					//# Apply movie filtering by Ratings
			
			for(ListIterator<Movie> a = results.listIterator(); a.hasNext();) {
				Movie temp = a.next();
				switch(year_type) {
				case 0: if((temp.getMeanRating()*10)%100 != (rate[0]*10)%100) results.remove(temp);	break;
				case 1: if((temp.getMeanRating()*10)%100 > (rate[0]*10)%100) results.remove(temp);	break;
				case 2: if((temp.getMeanRating()*10)%100 < (rate[0]*10)%100) results.remove(temp);	break;
				case 3: if((temp.getMeanRating()*10)%100 < (rate[0]*10)%100 || (temp.getMeanRating()*10)%100 > (rate[1]*10)%100) results.remove(temp);	break;
				
				}
			}
		}
				//----------------------- Sorting Selection ------------------------------------------
		p = Pattern.compile(regx_sort);
		m = p.matcher(request);
		if(m.find()) {
			sort_type = Integer.parseInt(m.group(1));
		}
		switch(sort_type) {
		case 1:	results = s.sortByTitle(results, false); 	break;
		case 2: results = s.sortByRating(results, true); 	break;
		case 3:	results = s.sortByRating(results, false); 	break;
		case 4: results = s.sortByYear(results, true);		break;
		case 5:	results = s.sortByYear(results, false);		break;
		case 6: results = s.CustomSort(results); 			break;
			default: results = s.sortByTitle(results, true);
		}
		return results;
		
	}
	
	//------------------ Edit Account ------------------
	private boolean SetName (User me) {
		String name;
		if(me.getName() != null) System.out.println("Edit Account " + users.get(me.getID()).getName());
		else System.out.println("Edit Account " + me.getID());
		System.out.println("=============================\n");
		System.out.println("New Username: ");
		Scanner in = new Scanner(System.in);
		name = in.next();
		users.get(me.getID()).addName(name);
		me.addName(name);
		
		return false;
	}
	
	private boolean SetPassword (User me) {
		String Password,PasswordC;
		System.out.print("Password: ");
		Scanner in = new Scanner(System.in);
		Password = in.next();
		System.out.print("Confirm Password: ");
		PasswordC = in.next();
		if(!Password.equals(PasswordC)) {
			System.out.println("Error Password Not match");
			System.out.println("Refreshinging in 5 seconds");
			wait(5);
			return true;
		}
		users.get(me.getID()).setPassword(MD5(Password));
		return false;
	}

	public void UpdateFile() throws FileNotFoundException {	
		PrintWriter pw = new PrintWriter(new File("output\\users.csv"));
        StringBuilder userOut = new StringBuilder();
        userOut.append("uid,uname,pwd\n");												//add header of csv file
        for(Integer key: users.keySet()) {
        	userOut.append(key + ",");
        	if(users.get(key).getName()== null) userOut.append("-,");
        	else userOut.append(users.get(key).getName()+",");
        	if(users.get(key).getPassword() == null) userOut.append("-\n");
        	else userOut.append(users.get(key).getPassword() + "\n");
        }
        
        pw.write(userOut.toString());
        pw.close();
        
        pw = new PrintWriter(new File("output\\movies.csv"));
        StringBuilder movieOut = new StringBuilder();
        movieOut.append("movieId,title,genres\n");
        for(Integer key : movies.keySet()) {
        	movieOut.append(key + ",");
        	movieOut.append(movies.get(key).getTitle() + " (" + movies.get(key).getYear() + "),");
        	Iterator<String> it = movies.get(key).getTags().iterator();
            while(it.hasNext()){
               movieOut.append(it.next() + "|");
            }
            movieOut.append("\n");
        }
        
        pw.write(movieOut.toString());
        pw.close();
        
        pw = new PrintWriter(new File("output\\ratings.csv"));
        StringBuilder ratingOut = new StringBuilder();
        ratingOut.append("userId,movieId,rating,timestamp\n");
        for(Integer key : movies.keySet()) {
        	ratingOut.append(movies.get(key).getStringRating());
        }
        
        pw.write(ratingOut.toString());
        pw.close();
	}

	//------------------ Miscellaneous ----------------------
	
	private void wait(int sec) {
		try {
			TimeUnit.SECONDS.sleep(sec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		    byte[] array = md.digest(md5.getBytes());
		    StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < array.length; ++i) {
		       sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		    }
		    return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {}
		
		return null;
	}
}

class SortByID implements Comparator<User>{										//-| For User Ascending sort
	public int compare(User a, User b) {
		return a.getID()-b.getID();
	}
}

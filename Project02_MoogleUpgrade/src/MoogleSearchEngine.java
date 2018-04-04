import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoogleSearchEngine {
	
	public static void start(MoogleS system, User me) {
		
		List<Movie> results= new ArrayList<Movie>();
		MoogleIOController.blankSpace();
		if(me != null) {
			
		}
		searchGuide();
		
		String request = MoogleIOController.readLine("Search: ");
		if(!(request.toUpperCase().charAt(0) == 'E')) {
			results = searchCompiler(system,request);
			
			int index = 0;
			
			for(ListIterator<Movie> a = results.listIterator();a.hasNext();index++) {
				System.out.println("[" + index + "]  " + a.next());
			}
			
			switch(MoogleIOController.readChar('0', '1', "Your Choice: ")) {
				case 'E':						break;
				default: 	start(system,me);	break;
			}
		}
		
		
	}
	
	private static void searchGuide() {
		System.out.println("Search Guide:\t\t-m <Title> to Filter by Title");
		System.out.println("\t\t\t-y  <Year> to Filter by Year");
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
	}

	private static List<Movie> searchCompiler(MoogleS system, String request) {
		String title = compileTitle(request);
		int year[] = compileYear(request);
		String[][] tags = compileTags(request);
		double ratings[] = compileRatings(request);
	
		return results(system,title,year,tags,ratings);
	}
	
	private static String compileTitle(String request) {
		String prased = new String();
		String regx = "-m ([^-]+)";
		
		Pattern p = Pattern.compile(regx);														//-|
		Matcher m = p.matcher(request);															// | Compile Title															
		if(m.find()) {																			// |
			prased = m.group(1);																//-|
			prased = prased.trim();																//-| remove white in the back if typed "The  -"
		}
		else{
			System.out.println("No title");
			return null;
		}
		
		return prased;
	}
	
	private static int[] compileYear(String request) {
		int[] prased = {-1,0,0};
		
		return prased;
	}
	
	private static double[] compileRatings(String request) {
		double[] prased = {0.0,0.0,0.0};
		
		return prased;
	}
	
	private static String[][] compileTags(String request){
		String[][] prased = new String[5][5];
		String[] includeTags; 
		String[] excludeTags;
		
		String regx_include = "-t.+/i ([\\S]+)";
		String regx_exclude = "-t.+/x ([\\S]+)";
		
		Pattern p = Pattern.compile(regx_include);														//-|
		Matcher m = p.matcher(request);																	// |
		if(m.find()) {																					// | Compile Included tags
			includeTags = m.group(1).split(",");														//-|
			for(int i = 0; i<includeTags.length;i++) {
				prased[0][i] = includeTags[i];
			}
		}
		else System.out.println("no Include");
		
		p = Pattern.compile(regx_exclude);														//-|
		m = p.matcher(request);																	// |
		if(m.find()) {																			// | Compile Excluded tags
			excludeTags = m.group(1).split(",");												// |
			for(int i = 0; i<excludeTags.length;i++) {
				prased[1][i] = excludeTags[i];
			}
		}
		else System.out.println("no Exclude");
		
		return prased;
	}
	
	private static List<Movie> results(MoogleS system, String title, int[] year, String[][] tags, double[] ratings) {
		List<Movie> results = new ArrayList<Movie>();
		
		if(title != null) {
			results = searchByTitle(system, title);
			
			if(year != null) {
				results = eliminateByYear(results, year);
				
			}
		}
		
		else if(year != null) {
			results = searchByYear(system, year);
		}
		
		else if(tags != null) {
			results = searchByTags(system, tags);
		}
		
		else if(ratings != null) {
			results = searchByRating(system, ratings);
		}
		
		return results;
	}
	
	private static List<Movie> searchByTitle(MoogleS system, String title){
		List<Movie> results = new ArrayList<Movie>();
		for(Integer key: system.getMovies().keySet()) {
			if(system.getMovies().get(key).getTitle().toLowerCase().contains(title.toLowerCase())) results.add(system.getMovies().get(key));
		}
		return results;
	}
	
	private static List<Movie> searchByTags(MoogleS system,String[][] tags){
		List<Movie> results = new ArrayList<Movie>();
		
		if(tags[0].length != 0) {
			
			for(int includeTags = 0;includeTags<tags[0].length;includeTags++) {
				
				for(Integer key: system.getMovies().keySet()) {
					
					if(system.getMovies().get(key).getTags().contains(tags[0][includeTags])) {
						
						if(!results.contains(system.getMovies().get(key))) results.add(system.getMovies().get(key));
					
					}
				}	
			}
		}
		else {
			
			for(int excludeTags = 0;excludeTags<tags[1].length;excludeTags++) {
				
				for(Integer key: system.getMovies().keySet()) {
					
					if(!system.getMovies().get(key).getTags().contains(tags[0][excludeTags])) {
						
						if(!results.contains(system.getMovies().get(key))) results.add(system.getMovies().get(key));
					
					}
				}	
			}
		}
		
		return results;
	}
	
	private static List<Movie> searchByYear(MoogleS system, int[] year){
		List<Movie> results = new ArrayList<Movie>();
		
		switch(year[0]) {
			case 0:	results = searchByYear_Exact(system, year[1]);				break;
			case 1: results = searchByYear_Lessthan(system,year[1]);			break;
			case 2: results = searchByYear_Morethan(system, year[1]);			break;
			case 3: results = searchByYear_Between(system, year[1], year[2]);	break;
		}
		return results;
	}
	
	private static List<Movie> searchByYear_Exact(MoogleS system,int year){
		List<Movie> results = new ArrayList<Movie>();
		
		for(Integer key: system.getMovies().keySet()) {
			if(system.getMovies().get(key).getYear() == year) {
				results.add(system.getMovies().get(key));
			}
		}
		
		return results;
	}
	
	private static List<Movie> searchByYear_Lessthan(MoogleS system,int lessthan){
		return null;
	}
	
	private static List<Movie> searchByYear_Morethan(MoogleS system,int morethan){
		return null;
	}
	
	private static List<Movie> searchByYear_Between(MoogleS system,int a, int b){
		return null;
	}
	
	private static List<Movie> searchByRating(MoogleS system,double[] rating){
		List<Movie> results = new ArrayList<Movie>();
		
		switch((int) rating[0]) {
			case 0:	results = searchByRating_Exact(system, rating[1]);				break;
			case 1: results = searchByRating_Lessthan(system,rating[1]);			break;
			case 2: results = searchByRating_Morethan(system, rating[1]);			break;
			case 3: results = searchByRating_Between(system, rating[1], rating[2]);	break;
		}
		return results;
	}
	
	private static List<Movie> searchByRating_Exact(MoogleS system,double rating){
		return null;
	}
	
	private static List<Movie> searchByRating_Lessthan(MoogleS system,double lessthan){
		return null;
	}
	
	private static List<Movie> searchByRating_Morethan(MoogleS system,double morethan){
		return null;
	}
	
	private static List<Movie> searchByRating_Between(MoogleS system,double a, double b){
		return null;
	}
	
	//Elimination
	private static List<Movie> eliminateByYear(List<Movie> toFilter, int[] year){
		List<Movie> filtered = toFilter;
		
		switch(year[0]) {
			case 0:	filtered = eliminateByYear_Exact(filtered, year[1]);				break;
			case 1: filtered = eliminateByYear_Lessthan(filtered,year[1]);				break;
			case 2: filtered = eliminateByYear_Morethan(filtered, year[1]);				break;
			case 3: filtered = eliminateByYear_Between(filtered, year[1], year[2]);		break;
		}
		return filtered;
	}
	
	private static List<Movie> eliminateByYear_Exact(List<Movie> toFilter,int year){
		return null;
	}
	
	private static List<Movie> eliminateByYear_Lessthan(List<Movie> toFilter,int lessthan){
		return null;
	}
	
	private static List<Movie> eliminateByYear_Morethan(List<Movie> toFilter,int morethan){
		return null;
	}
	
	private static List<Movie> eliminateByYear_Between(List<Movie> toFilter,int a, int b){
		return null;
	}
	
	private static List<Movie> eliminateByRating(List<Movie> toFilter,double[] rating){
		List<Movie> filtered = toFilter;
		
		switch((int) rating[0]) {
			case 0:	filtered = eliminateByRating_Exact(filtered, rating[1]);					break;
			case 1: filtered = eliminateByRating_Lessthan(filtered,rating[1]);					break;
			case 2: filtered = eliminateByRating_Morethan(filtered, rating[1]);					break;
			case 3: filtered = eliminateByRating_Between(filtered, rating[1], rating[2]);		break;
		}
		return filtered;
	}
	
	private static List<Movie> eliminateByRating_Exact(List<Movie> toFilter,double rating){
		return null;
	}
	
	private static List<Movie> eliminateByRating_Lessthan(List<Movie> toFilter,double lessthan){
		return null;
	}
	
	private static List<Movie> eliminateByRating_Morethan(List<Movie> toFilter,double morethan){
		return null;
	}
	
	private static List<Movie> eliminateByRating_Between(List<Movie> toFilter,double a, double b){
		return null;
	}
}

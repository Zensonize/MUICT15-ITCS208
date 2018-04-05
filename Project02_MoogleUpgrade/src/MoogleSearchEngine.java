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
			if(!results.isEmpty()) {
				for(ListIterator<Movie> a = results.listIterator();a.hasNext();index++) {
					System.out.println("[" + index + "]  " + a.next());
				}
			}
			
			switch(MoogleIOController.readChar('0', '1', "Your Choice: ")) {
				case '0':	start(system,me);	break;	
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
		System.out.println("\n[E] Back\n");
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
		String regx_year = "-y ([<>|=]) (\\d{4})";
		String regx_year2 = "-y.+\\d,(\\d{4})";
		char type = 'n';
		
		Pattern p = Pattern.compile(regx_year);													//-|
		Matcher m = p.matcher(request);															// | Compile Year														
		if(m.find()) {																			// |
			type = m.group(1).charAt(0);														//-|
			prased[1] = Integer.parseInt(m.group(2));
		}
		else return null;
		
		switch(type) {
			case '=': prased[0] = 0;	break;
			case '<': prased[0] = 1;	break;
			case '>': prased[0] = 2;	break;
			case '|': prased[0] = 3;	
					  p = Pattern.compile(regx_year2);
					  m = p.matcher(request);
					  if(m.find()) {
						  prased[2] = Integer.parseInt(m.group(1));
					  }
					  break;
		}
		
		System.out.println(prased[0] + " " + prased[1] + " " + prased[2]);
		
		return prased;
	}
	
	private static double[] compileRatings(String request) {
		double[] prased = {0.0,0.0,0.0};
		String regx_rating = "-r ([<>|=]) (\\d+(\\.\\d+)?)";
		String regx_rating2 = "-r.+\\d,(\\d+(\\.\\d+)?)";
		char type = 'n';
		
		Pattern p = Pattern.compile(regx_rating);												//-|
		Matcher m = p.matcher(request);															// | Compile Year														
		if(m.find()) {																			// |
			type = m.group(1).charAt(0);														//-|
			prased[1] = Double.parseDouble(m.group(2));
		}
		else return null;
		
		switch(type) {
			case '=': prased[0] = 0;	break;
			case '<': prased[0] = 1;	break;
			case '>': prased[0] = 2;	break;
			case '|': prased[0] = 3;	
					  p = Pattern.compile(regx_rating2);
					  m = p.matcher(request);
					  if(m.find()) {
						  prased[2] = Double.parseDouble(m.group(1));
					  }
					  break;
		}
		
		System.out.println(prased[0] + " " + prased[1] + " " + prased[2]);
		
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
		
		if(prased[0][0] == null && prased[1][0] == null) return null;
		return prased;
	}
	
	private static List<Movie> results(MoogleS system, String title, int[] year, String[][] tags, double[] ratings) {
		List<Movie> results = new ArrayList<Movie>();
		
		if(title != null) {
			results = searchByTitle(system, title);
			
			if(year != null) {
				results = eliminateByYear(results, year);
			}
			if(tags != null) {
				results = eliminateByTags(results, tags);
			}
			if(ratings != null) {
				results = eliminateByRating(results, ratings);
			}
		}
		
		else if(year != null) {
			results = searchByYear(system, year);
			if(tags != null) {
				results = eliminateByTags(results, tags);
			}
			if(ratings != null) {
				results = eliminateByRating(results, ratings);
			}
		}
		
		else if(tags != null) {
			results = searchByTags(system, tags);
			if(ratings != null) {
				results = eliminateByRating(results, ratings);
			}
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
		System.out.println(tags[0].length);
		
		if(tags[0][0] != null) {
			
			for(int includeTags = 0;tags[0][includeTags] != null;includeTags++) {
				
				for(Integer key: system.getMovies().keySet()) {
					
					if(system.getMovies().get(key).getTags().contains(tags[0][includeTags])) {
						
						if(!results.contains(system.getMovies().get(key))) results.add(system.getMovies().get(key));
					
					}
				}	
			}
			
			if(tags[1][0] != null) results = eliminateByTags(results, tags);
		}
		
		else {
			System.out.println("exclude search ");
			for(Integer key: system.getMovies().keySet()) {
				Boolean isValid = true;
				for(int excludeTags = 0;tags[1][excludeTags] != null;excludeTags++) {				
					if(system.getMovies().get(key).getTags().contains(tags[1][excludeTags])) {
						isValid = false;
					}
				}
				if(isValid) results.add(system.getMovies().get(key));
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
		List<Movie> results = new ArrayList<Movie>();
		
		for(int i = 1000;i<=lessthan;i++) {
			List<Movie> subResult = searchByYear_Exact(system, i);
			for(Movie a:subResult) {
				if(!results.contains(a)) results.add(a);
			}
		}
		
		return results;
	}
	
	private static List<Movie> searchByYear_Morethan(MoogleS system,int morethan){
		List<Movie> results = new ArrayList<Movie>();
		
		for(int i = morethan;i<=2018;i++) {
			List<Movie> subResult = searchByYear_Exact(system, i);
			for(Movie a:subResult) {
				if(!results.contains(a)) results.add(a);
			}
		}
		
		return results;
	}
	
	private static List<Movie> searchByYear_Between(MoogleS system,int y1, int y2){
		int from,to;
		if(y1>y2) {
			from = y2;
			to  = y1;
		}
		else {
			from = y1;
			to = y2;
		}
		
		List<Movie> results = new ArrayList<Movie>();
		
		for(int i = from;i<=to;i++) {
			List<Movie> subResult = searchByYear_Exact(system, i);
			
			for(Movie a:subResult) {
				if(!results.contains(a)) results.add(a);
			}
		}
		
		return results;
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
		List<Movie> results = new ArrayList<Movie>();
		int exactRating = (int) (rating*10)%100;
		for(Integer key: system.getMovies().keySet()) {
			if(system.getMovies().get(key).getExactRating() == exactRating) {
				results.add(system.getMovies().get(key));
			}
		}
		
		return results;
	}
	
	private static List<Movie> searchByRating_Lessthan(MoogleS system,double lessthan){
		List<Movie> results = new ArrayList<Movie>();
		int exactRating = (int) (lessthan*10)%100;
		for(int i=1;i<=exactRating;i++) {
			for(Integer key: system.getMovies().keySet()) {
				if(system.getMovies().get(key).getExactRating() == i) {
					results.add(system.getMovies().get(key));
				}
			}
		}
		
		return results;
	}
	
	private static List<Movie> searchByRating_Morethan(MoogleS system,double morethan){
		List<Movie> results = new ArrayList<Movie>();
		int exactRating = (int) (morethan*10)%100;
		for(int i=exactRating;i<=99;i++) {
			for(Integer key: system.getMovies().keySet()) {
				if(system.getMovies().get(key).getExactRating() == i) {
					results.add(system.getMovies().get(key));
				}
			}
		}
		
		return results;
	}
	
	private static List<Movie> searchByRating_Between(MoogleS system,double a, double b){
		List<Movie> results = new ArrayList<Movie>();
		int lower,upper;
		if(a>b) {
			lower = (int) (b*10)%100;
			upper = (int) (a*10)%100;
		}
		else {
			lower = (int) (a*10)%100;
			upper = (int) (b*10)%100;
		}
		for(int i=lower;i<=upper;i++) {
			for(Integer key: system.getMovies().keySet()) {
				if(system.getMovies().get(key).getExactRating() == i) {
					results.add(system.getMovies().get(key));
				}
			}
		}
		
		return results;
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
		List<Movie> toReturn = toFilter;
		List<Movie> toRemove = new ArrayList<Movie>();
		for(Movie a:toReturn) {
			if(a.getYear() == year) toRemove.add(a);
		}
		toReturn.removeAll(toRemove);
		
		return toReturn;
	}
	
	private static List<Movie> eliminateByYear_Lessthan(List<Movie> toFilter,int lessthan){
		List<Movie> toReturn = toFilter;
		List<Movie> toRemove = new ArrayList<Movie>();
		for(Movie a:toReturn) {
			if(a.getYear() > lessthan) toRemove.add(a);
		}
		toReturn.removeAll(toRemove);
		
		return toReturn;
	}
	
	private static List<Movie> eliminateByYear_Morethan(List<Movie> toFilter,int morethan){
		List<Movie> toReturn = toFilter;
		List<Movie> toRemove = new ArrayList<Movie>();
		for(Movie a:toReturn) {
			if(a.getYear() < morethan) toRemove.add(a);
		}
		toReturn.removeAll(toRemove);
		
		return toReturn;
	}
	
	private static List<Movie> eliminateByYear_Between(List<Movie> toFilter,int y1, int y2){
		List<Movie> toReturn = toFilter;
		List<Movie> toRemove = new ArrayList<Movie>();
		
		for(Movie a:toReturn) {
			if(!(a.getYear() > Math.min(y1, y2) && a.getYear() < Math.max(y1, y2))) toRemove.add(a);
		}
		toReturn.removeAll(toRemove);
		
		return toReturn;
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
		List<Movie> toReturn = toFilter;
		List<Movie> toRemove = new ArrayList<Movie>();
		int exactRating = (int) (rating*10)%100;
		for(Movie a:toReturn) {
			if(a.getExactRating() != exactRating) toRemove.add(a);
		}
		toReturn.removeAll(toRemove);
		
		return toReturn;
	}
	
	private static List<Movie> eliminateByRating_Lessthan(List<Movie> toFilter,double lessthan){
		List<Movie> toReturn = toFilter;
		List<Movie> toRemove = new ArrayList<Movie>();
		int exactRating = (int) (lessthan*10)%100;
		for(Movie a:toReturn) {
			if(a.getExactRating() > exactRating) toRemove.add(a);
		}
		toReturn.removeAll(toRemove);
		
		return toReturn;
	}
	
	private static List<Movie> eliminateByRating_Morethan(List<Movie> toFilter,double morethan){
		List<Movie> toReturn = toFilter;
		List<Movie> toRemove = new ArrayList<Movie>();
		int exactRating = (int) (morethan*10)%100;
		for(Movie a:toReturn) {
			if(a.getExactRating() < exactRating) toRemove.add(a);
		}
		toReturn.removeAll(toRemove);
		
		return toReturn;
	}
	
	private static List<Movie> eliminateByRating_Between(List<Movie> toFilter,double r1, double r2){
		List<Movie> toReturn = toFilter;
		List<Movie> toRemove = new ArrayList<Movie>();
		int lowerBound = (int) (Math.min(r1,r2)*10)%100;
		int upperBound = (int) (Math.max(r1,r2)*10)%100;
		for(Movie a:toReturn) {
			if(!(a.getExactRating() >= lowerBound && a.getExactRating() <= upperBound)) toRemove.add(a);
		}
		toReturn.removeAll(toRemove);
		
		return toReturn;
	}
	
	private static List<Movie> eliminateByTags(List<Movie> toFilter, String[][] tags){
		List<Movie> toReturn = toFilter;

		//change from and to or >> move include array inside movie loop
		if(tags[0][0] != null) {
			List<Movie> toRemove = new ArrayList<Movie>();
			
			for(Movie chk:toReturn) {
				boolean mustRemove = true;
				for(int include = 0;tags[0][include] != null;include++) {
					if(chk.getTags().contains(tags[0][include])) mustRemove = false;
				}
				if(mustRemove) toRemove.add(chk);
			}
			toReturn.removeAll(toRemove);
		}
		
		if(tags[1][0] != null) {
			for(int exclude = 0; tags[1][exclude] != null;exclude++) {
				List<Movie> toRemove = new ArrayList<Movie>();
				for(Movie a:toReturn) {
					if(a.getTags().contains(tags[1][exclude])) {
						toRemove.add(a);
					}
				}
				toReturn.removeAll(toRemove);
			}
		}
		
		return toReturn;
	}
}


public class MoogleSearchEngine {
	
	public static void start(MoogleS system, User me) {
		MoogleIOController.blankSpace();
		if(me != null) {
			
		}
		searchGuide();
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
}

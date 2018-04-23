import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Movie {
	public int mid = -1;
	public String title = null;
	public int year = -1;
	public Set<String> tags = null;
	public List<User> ratedUser = new ArrayList<User>();
	public double predicted = 0;
	public int index;
	public Movie(int _mid, String _title, int _year)
	{
		mid = _mid;
		title = _title;
		year = _year;
		tags = new HashSet<String>();
	}
	
	public int getID() {
		return mid;
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public void addTag(String tag)
	{
		tags.add(tag);
	}
	
	public String toString()
	{
		return "[mid: "+mid+":"+title+" ("+year+") "+tags+"]";
	}
	
	public void addPredicted(double p) {
		predicted = p;
	}
	
	public void addIndex(int index) {
		this.index = index;
	}
}

//#Krittamet 6088063
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Movie {
	public int mid = -1;
	public String title = null;
	public Set<String> tags = null;
	public Movie(int _mid, String _title)
	{
		mid = _mid;
		title = _title;
		tags = new HashSet<String>();
	}
	
	public void addTags(Set<String> _tags) {
		for (Iterator<String> it = _tags.iterator(); it.hasNext(); ) {
	        tags.add(it.next());
	    }
		
	}
	
	public void addTags(String _tags) {
		tags.add(_tags);
	}
	
	public String toString()
	{
		return "[mid: "+mid+":"+title+" "+tags+"]";
	}
	
}

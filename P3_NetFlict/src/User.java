import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class User implements Comparable<User>{
	public int uid = 0;
	Map<Integer, Rating> ratings = null;		//mapping movieID -> rating
	List<Integer> ratingKey = new ArrayList<Integer>();
	double avRating;
	int index;
	
	public User(int _id)
	{
		uid = _id;
		ratings = new HashMap<Integer, Rating>();
	}
	
	public double getMeanRating()
	{
		double result = 0;
		for(Rating r: ratings.values())
		{
			result += r.rating;
		}
		if(ratings.size() > 0) result /= (double)ratings.size();
		
		return result;
	}
	
	public void addRating(Movie movie, double rating, long timestamp)
	{
		Rating r = ratings.get(movie.getID());
		if(r == null) 
		{	r = new Rating(movie, rating, timestamp);
			ratings.put(movie.getID(), r);
			ratingKey.add(movie.getID());
		}
		else
		{
			if(r.timestamp < timestamp)
			{
				r.rating = rating;
				r.timestamp = timestamp;
			}
		}
	}
	
	public String toString()
	{
		StringBuilder  s = new StringBuilder();
		s.append("[user: "+uid+" rates "+ratings.size()+" movies]\n");
		for(Rating rat: ratings.values())
		{
			s.append("\t"+rat+"\n");
		}
		 
		 return s.toString();
	}

	@Override
	public int compareTo(User o) {
		return (new Integer(uid)).compareTo(o.uid);
	}
	
	public String getRatingArray(List<Integer> movieIndex) {
		double[] rating = new double[movieIndex.size()+1];
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<movieIndex.size();i++) {
			if(ratings.containsKey(movieIndex.get(i))){
				sb.append(ratings.get(movieIndex.get(i)).rating);
			}
			else sb.append("0.0");
			sb.append(" ");
		}
		sb.append(avRating);
		
		return sb.toString();
	}
	
	public void addIndex(int index) {
		this.index = index;
	}
	
	public void addAveRating(double avRating) {
		this.avRating = avRating;
	}
}

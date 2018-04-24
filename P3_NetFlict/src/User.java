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
	Map<Integer, Rating> ratingIndex = new HashMap<Integer, Rating>();
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
	
	public String getRatingArray(int moviesSize) {
		double[] rating = new double[moviesSize+1];
		
		StringBuilder sb = new StringBuilder();
		
		for(Integer k: ratings.keySet()) {
			rating[ratings.get(k).m.index] = ratings.get(k).rating;
		}
		
		for(int i=0;i<moviesSize;i++) {
			sb.append(rating[i] + " ");
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

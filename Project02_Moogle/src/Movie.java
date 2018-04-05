// Name: Krittamet Kiattikulwattana
// Student ID: 6088063
// Section: 1

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Movie {
	private int mid;
	private String title;
	private int year;
	private Set<String> tags;
	private Map<Integer, Rating> ratings;	//mapping userID -> rating
	private Double avgRating;
	//additional
	
	public Movie(int _mid, String _title, int _year){
		mid = _mid;
		title = _title;
		year = _year; 
		tags = new HashSet<String>();
		ratings = new HashMap<Integer, Rating>();
		avgRating = 0.0;
	}
	
	public int getID() {
		return mid;
	}
	public String getTitle(){
		return title;
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public void addTag(String tag){
		if(!tags.contains(tag)) {
			tags.add(tag);
		}
	}
	
	public int getYear(){
		return year;
	}
	
	public String toString()
	{
		return "[mid: "+mid+":"+title+" ("+year+") "+tags+"] -> avg rating: " + avgRating;
	}
	
	
	public double calMeanRating(){
		double mean = 0;
		for(Integer key :ratings.keySet()) {
			if(ratings.get(key).getRating() >= 0) {
				mean += ratings.get(key).getRating();
			}
		}
		return mean/ratings.size();
	}
	
	public Double getMeanRating(){
		return avgRating;
	}
	
	public void addRating(User user, Movie movie, double rating, long timestamp) {
		if(ratings.containsKey(user.getID())) {
			if(ratings.get(user.getID()).getTimeStamp() < timestamp) {
				ratings.replace(user.getID(), new Rating(user,movie,rating,timestamp));
			}
		}
		else{
			ratings.put(user.getID(), new Rating(user,movie,rating,timestamp));
		}
		avgRating = calMeanRating();
	}
	
	public Map<Integer, Rating> getRating(){
		return ratings;
	}
	
}

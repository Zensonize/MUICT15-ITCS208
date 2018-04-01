//# COPYRIGHT KrittametK

import java.text.DecimalFormat;

public class Song {
	private String title;
	private double duration;
	
	public Song(String title,double duration) {
		this.title = title;
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public double getDuration() {
		return duration;
	}
	
	public double getDurationInSec() {
		return (int) duration*60 + ((duration % 1.0) * 100);
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		return title + ", " + df.format(duration) + " minutes (" + df.format(getDurationInSec()) + " seconds)";
	}
}

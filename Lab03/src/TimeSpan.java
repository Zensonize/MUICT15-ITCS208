//# COPYRIGHT KrittametK

public class TimeSpan {
	private int hr;
	private int min;
	
	public TimeSpan(int hr,int min) {
		this.hr = hr;
		this.min = min;
	}

	public int getHours() {
		return hr;
	}

	public int getMinutes() {
		return min;
	}
	
	public void add(int hr,int min) {
		this.hr+=hr;
		this.min+=min;
		if(this.min >=60) {
			this.hr++;
			this.min-=60;
		}
	}
	
	public void add(TimeSpan timer) {
		hr+=timer.getHours();
		min+=timer.getMinutes();
		if(this.min >=60) {
			this.hr++;
			this.min-=60;
		}
	}
	
	public String getTotalHours() {
		return hr + ""+ (double)min/60.0;
	}
	
	public String toString() {
		return hr+"h"+min+"m";
	}
	
	
	
}

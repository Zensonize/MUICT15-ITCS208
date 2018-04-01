//# COPYRIGHT KrittametK

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ListIterator;

public class Playlist {
	private String playlistName;
	private ArrayList<Song> myPlaylist;
	private double totDuration;
	
	public Playlist(String playlistName) {
		this.playlistName = playlistName;
		myPlaylist = new ArrayList<Song>();
		totDuration = 0;
	}
	
	public void addSong(Song in) {
		myPlaylist.add(in);
		totDuration += in.getDurationInSec();
	}
	
	public void addSongByIndex(Song in, int index) {
		if(index<0 || index > myPlaylist.size()) {
			System.out.println("Error: Couldn't add song at index " + index);
		}
		else {
			myPlaylist.add(index,in);
			totDuration += in.getDurationInSec();
		}
	}
	
	public void removeSongByIndex(int index) {
		if(index<0 || index > myPlaylist.size()) {
			System.out.println("Error: The index is invalid");
		}
		else {
			totDuration-=myPlaylist.get(index).getDurationInSec();
			myPlaylist.remove(index);
		}
	}
	
	public void removeSongByTitle(String in) {
		Boolean isDeleted = false;
		int idx = 0;
		for(ListIterator<Song> iter = myPlaylist.listIterator(); iter.hasNext();) {
			String cmp = iter.next().getTitle();
			if(in.equals(cmp) == true) {
				isDeleted = true;
				totDuration-=myPlaylist.get(idx).getDurationInSec();
				myPlaylist.remove(idx);
				break;
				
			}
			idx++;
		}
		if(!isDeleted) {
			System.out.println("Error: The title is not found");
		}
	}
	
	public void moveUp(int index) {
		if(index<0 || index> myPlaylist.size()) {
			System.out.println("INPUT OUT OF RANGE");
		}
		else{
			Song temp = myPlaylist.get(index);
			myPlaylist.add(index-1, temp);
			myPlaylist.remove(index+1);
		}
	}
	
	public void moveDown(int index) {
		if(index<0 || index >= myPlaylist.size()) {
			System.out.println("INPUT OUT OF RANGE");
		}
		else{
			Song temp = myPlaylist.get(index);
			myPlaylist.add(index+2, temp);
			myPlaylist.remove(index);
		}
	}
	
	public void showPlaylist() {
		System.out.println(playlistName);
		for(ListIterator<Song> iter = myPlaylist.listIterator(); iter.hasNext();) {
			String print = iter.next().toString();
			System.out.println("[" + (iter.nextIndex()-1) + "] " + print);
		}
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		System.out.println("Total duration is "+ (((int)totDuration/60) +(totDuration%60)/100) + " minutes");
		
	}
}

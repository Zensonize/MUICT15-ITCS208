//KrittametK 6088063

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import javafx.util.Pair;

public class SortingGod {
	
	public static String[] loadTexts(String textFilename) {
		// TODO Auto-generated method stub
		String[] toReturn = null;
		try {
			File txt = new File(textFilename);
			LineIterator it = FileUtils.lineIterator(txt, "UTF-8");
			try {
				StringBuilder all = new StringBuilder();
				
				while (it.hasNext()) {
			    all.append(it.nextLine());
				}
				
				toReturn = all.toString().split("\\ ");
			   
			 } finally {
			   LineIterator.closeQuietly(it);
			 }
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return toReturn;
	}
	
	public static void printAll(String[] texts) {
		if(texts != null) {
			for(String k:texts) {
				System.out.print(k + " ");
			}
		}
		System.out.println();
	}
	
	public static String[] mSort(String[] texts) {
		String[] sorted = null;
		if(texts!= null) {
			if(texts.length>1) {
				int half = texts.length/2;
//				System.out.println("lower = " + half + "Upper = " + (texts.length-half));
				String[] Upper = new String[texts.length-half]; 
				String[] Lower = new String[half];
				
				for(int i = 0;i<half;i++) {
					Lower[i] = texts[i];
				}
				for(int i = 0;half + i<texts.length;i++) {
					Upper[i] = texts[half + i];
				}
				
				Upper = mSort(Upper);
				Lower = mSort(Lower);
				
				sorted = mSortMerge(Lower, Upper);
			}
			else return texts;
		}
		return sorted;
	}
	
	public static String[] mSortMerge(String[] Lower,String[] Upper) {
		String[] toReturn = new String[Lower.length + Upper.length];
		int l_idx = 0;
		int u_idx = 0;
		for(int i = 0;i<toReturn.length;i++) {
			if(l_idx < Lower.length && u_idx< Upper.length) {
				if(Lower[l_idx].compareTo(Upper[u_idx]) < 0){ 
					toReturn[i] = Upper[u_idx++];
				}
				else toReturn[i] = Lower[l_idx++];
			}
			else {
				if(l_idx == Lower.length) {
					toReturn[i] = Upper[u_idx++];
				}
				else {
					toReturn[i] = Lower[l_idx++];
				}
			}
		}
		return toReturn;
	}
	
	public static List<ModeString> mode(String[] texts){
		List<ModeString> modeMap = new ArrayList<ModeString>();
		List<ModeString> topTen = new ArrayList<ModeString>();
		String Last = texts[0];
		modeMap.add(new ModeString(Last));
		for(int i = 1;i<texts.length;i++) {
			if(texts[i].compareTo(Last) != 0) {
				Last = texts[i];
				modeMap.add(new ModeString(Last));
			}
			else modeMap.get(modeMap.size()-1).increase();
		}
		
		Collections.sort(modeMap);
		
		int idx = 0;
		for(ListIterator<ModeString> topK = modeMap.listIterator();topK.hasNext();) {
			topTen.add(modeMap.get(idx++));
			if(idx == 10) break;
		}
		return topTen;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] texts = loadTexts("data.txt");
		if(texts != null) {
//			System.out.println(texts.length);
			printAll(texts);
			texts = mSort(texts);
			printAll(texts);
//			System.out.println(texts.length);
			List<ModeString> topK = mode(texts);
			for(ListIterator<ModeString> k = topK.listIterator();k.hasNext();) {
				System.out.println(k.next().toString());
			}
		}
		
	}
	

}

class ModeString implements Comparable<ModeString>{
	String key;
	int mode = 1;
	public ModeString(String key) {
		this.key = key;
	}
	
	public void increase() {
		mode = mode+1;
	}
	
	public String getKey() {
		return key;
	}
	public int getMode() {
		return mode;
	}
	@Override
	public String toString() {
		return key + " Mode: " + mode;
	}
	@Override
	public int compareTo(ModeString arg0) {
		// TODO Auto-generated method stub
		if(arg0.mode > mode) return 1;
		else if(arg0.mode < mode) return -1;
		else return 0;
	}
}

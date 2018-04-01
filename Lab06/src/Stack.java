import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

public class Stack<T> extends ArrayList<T>{
	ArrayList<T> a;
	private T[] anArray;
	private int capacity = 0;
	private boolean isUnlimited = true; 
	
	public Stack() {
		a = new ArrayList<T>();
	}
	
	public Stack(int capacity) {
		a = new ArrayList<T>();
		isUnlimited = false; 
		this.capacity = capacity;
	}
	
	public boolean push(T object) {
		//System.out.println("SIZE of A: " + a.size()+ " Capacity: " + capacity + " Idx left: " + (capacity-a.size()) );
		if(isUnlimited) {
			a.add(object);
	        this.a = a;
			return true;
		}
		else if(a.size()<capacity){
			a.add(object);
			return true;
		}
		return false;
	}
	
	public T pop() {
		if(a.size() == 0) {
			return null;
		}
		T temp = a.get(a.size()-1);
		a.remove(a.size()-1);
		return temp;
		
	}
	
	public T peek() {
		if(a.size() == 0){
			return null;
		}
		return a.get(a.size()-1);
	}
	
	public T[] toArray() {
		if(a.size() == 0) {
			return null;
		}
		Object[] out = new Object[a.size()];
		for(int i=0;i<a.size();i++) {
			out[i] = a.get(a.size()-1-i);
		}
		return (T[]) out;
	}
	
	public T getValueAtIndex(int index) {
		return a.get(index);
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for(int i=a.size()-1;i>=0;i--) {
			out.append("|\t" + i + "\t|\t" + a.get(i) + "\t|\n");
		}
		return out.toString();
	}
	
	public int size() {
		return a.size();
	}
	
	
}

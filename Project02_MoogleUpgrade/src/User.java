// Name:
// Student ID:
// Section: 

public class User {
	public int uid;
	private String uname = null;
	private String pwd = null;
	
	public User(int _id){
		uid = _id;
	}
	
	public User(int _id,String _pwd) {
		uid = _id;
		pwd = _pwd;
	}
	
	public User(int _id,String _uname,String _pwd) {
		uid = _id;
		uname = _uname;
		pwd = _pwd;
	}
	
	public void addName(String name) {
		uname = name;
	}
	
	public void setPassword(String _pwd) {
		pwd = _pwd;
	}
	
	public int getID(){
		return uid;
	}
	
	public String getPassword(){
		return pwd;
	}
	
	public String getName() {
		return uname;
	}

}

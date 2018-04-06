
public class MoogleLogin {
	public static void login (MoogleS system) {
		int userID;
		String password;
		
		MoogleIOController.blankSpace();
		System.out.println("Login");
		MoogleIOController.headerLine();
		
		userID = MoogleIOController.readInt(0, Integer.MAX_VALUE, "UserID: ");
		password = MoogleIOController.MD5(MoogleIOController.readString("Password: "));
				
		if(system.getUser().containsKey(userID)) {		
			System.out.println(system.getUser().get(userID).getPassword());//check if the user id is present
			System.out.println(system.getUser().containsKey(userID));
			if(system.getUser().get(userID).getPassword().equals("-") && password.equals(MoogleIOController.MD5("root"))) {			//check if account is a predefined account
				MoogleHome.start(system, system.getUser().get(userID));
			}
			else if(system.getUser().get(userID).getPassword().equals(password)) {
				MoogleHome.start(system, system.getUser().get(userID));
			}
			
		}
		else System.out.println("Wrong UserID or Password");
	}
}

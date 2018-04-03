
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
			if(system.getUser().get(userID).getPassword().equals(password)) {
				MoogleUser.start(system, system.getUser().get(userID));
			}
		}
	}
}

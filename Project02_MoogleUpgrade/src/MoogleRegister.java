
public class MoogleRegister {
	
	public static void register(MoogleS system) {
		System.out.println("Register!");
		int userID;
		String password,confirmPassword;
		
		System.out.println("\n\n\n\n\n");
		System.out.println("Register");
		System.out.println("=============================\n");
		
		userID = MoogleIOController.readInt(0, Integer.MAX_VALUE, "UserID: ");
		password = MoogleIOController.readString("Password: ");
		confirmPassword = MoogleIOController.readString("Confirm Password: ");
		
		if(password.equals(confirmPassword)) {
			if(!system.getUser().containsKey(userID)) {
				system.getUser().put(userID, new User(userID,password));
			}
		}
		else {
			System.out.println("Wrong Password");
		}
	}
}

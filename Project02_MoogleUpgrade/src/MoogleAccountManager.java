
public class MoogleAccountManager {
	public static void start(MoogleS system, User me) {
		
		MoogleIOController.blankSpace();
		if(!me.getName().equals("-")) System.out.println("Edit Account " + system.getUser().get(me.getID()).getName());
		else System.out.println("Edit Account " + me.getID());
		MoogleIOController.headerLine();
		
		userInfo(system,me);
		
		System.out.println("\t[0] Change Username");
		System.out.println("\t[1] Change Password\n");
		System.out.println("\t[E] Back\n");
		
		switch(MoogleIOController.readChar('0', '1', "Your Choice: ")) {
			case '0': setName(system,me); 		start(system,me);	break;
			case '1': setPassword(system,me); 	start(system,me);	break;
			case 'E': 										break;
			default:					start(system,me);
		}
		
	}
	
	private static void setName(MoogleS system, User me) {
		MoogleIOController.blankSpace();
		if(!me.getName().equals("-")) System.out.println("Edit Account " + system.getUser().get(me.getID()).getName());
		else System.out.println("Edit Account " + me.getID());
		MoogleIOController.headerLine();
		
		userInfo(system,me);
		
		String username = MoogleIOController.readString("New Username: ");
		system.getUser().get(me.getID()).addName(username);
		me = system.getUser().get(me.getID());
	}
	
	private static void setPassword(MoogleS system, User me) {
		MoogleIOController.blankSpace();
		if(!me.getName().equals("-")) System.out.println("Edit Account " + system.getUser().get(me.getID()).getName());
		else System.out.println("Edit Account " + me.getID());
		MoogleIOController.headerLine();
		
		userInfo(system,me);
		
		String password = MoogleIOController.readString("New Password: ");
		String confirmPassword = MoogleIOController.readString("Confirm Password: ");
		
		if(password.equals(confirmPassword)) {
			system.getUser().get(me.getID()).setPassword(MoogleIOController.MD5(password));
			me = system.getUser().get(me.getID());
		}
		else {
			System.out.print("Incorrect Password ");
			MoogleIOController.wait(3);
		}
		
	}
	
	private static void userInfo(MoogleS system, User me) {
		System.out.println("\tUSER ID: " + me.getID());
		if(!me.getName().equals("-")) System.out.println("\tUsername: " + me.getName());
		else System.out.println("\tUsername: " + "EMPTY");
		System.out.println("\t-----------------------------------\n\n");
	}
	
}

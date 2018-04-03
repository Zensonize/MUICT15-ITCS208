
public class MoogleHome {
	
	public static void start(MoogleS system, User me) {
		MoogleIOController.blankSpace();
		if(!me.getName().equals("-")) System.out.println("Welcome to Moogle " + system.getUser().get(me.getID()).getName());
		else System.out.println("Welcome to Moogle " + me.getID());
		MoogleIOController.headerLine();
		System.out.println("\t[0] Search Engine");
		System.out.println("\t[1] Edit my Account");
		System.out.println("\n\t[E] Log Out\n");
		
		switch(MoogleIOController.readChar('0', '1', "Your Choice: ")) {
			case '0': MoogleSearchEngine.start(system, me); 		start(system,me);	break;
			case '1': MoogleAccountManager.start(system, me); 		start(system,me);	break;
			case 'E': 																	break;
			default : 												start(system,me);
		}
		
	}
	
}

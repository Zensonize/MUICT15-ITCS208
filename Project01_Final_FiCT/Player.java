import java.net.SocketTimeoutException;

public class Player {
	public enum PlayerType {Healer, Tank, Samurai, BlackMage, Phoenix, Cherry};
	
	private PlayerType type;
	private double maxHP;
	private double currentHP;
	
	private double atk;
	
	private int numSP;
	private int currentSP;
	
	private boolean isSleeping;
	private boolean isCursed;
	private boolean isTaunting;
	private boolean isAlive;
	
	public Player(PlayerType _type) {
		type = _type;
		switch(_type) {
			case Healer:	maxHP = 4790; 	atk = 238;	numSP = 4;	break;
			case Tank: 		maxHP = 5340; 	atk = 255;	numSP = 4;	break;
			case Samurai:	maxHP = 4005; 	atk = 368;	numSP = 3;	break;
			case BlackMage: maxHP = 4175; 	atk = 303;	numSP = 4;	break;
			case Phoenix:	maxHP = 4175; 	atk = 209;	numSP = 8;	break;
			case Cherry: 	maxHP = 3560; 	atk = 198;	numSP = 4;	break;
		}
		currentHP = maxHP;	currentSP = numSP-1;
		isAlive = true;		isCursed = false;		isSleeping = false;		isTaunting = false;
	}
	
	public double getCurrentHP() {
		return currentHP;
	}
	
	public Player.PlayerType getType(){
		return type;
	}
	
	public double getMaxHP() {
		return maxHP;
	}
	
	public boolean isSleeping() {
		return isSleeping;
	}
	
	public boolean isCursed() {
		return isCursed;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public boolean isTaunting() {
		return isTaunting;
	}
	
	public void attack(Player target) {
		target.currentHP -=atk;
		if(target.currentHP<=0) {
			target.currentHP = 0;
			target.currentSP = numSP-1;
			target.isAlive = false;	isCursed = false;	isSleeping = false;	isTaunting = false;
		}
	}
	
	public void useSpecialAbility(Player[][] myTeam, Player[][] theirTeam, int round, Arena arena) {
		switch(type) {
			case Healer:	heal(findHealTarget(myTeam));				break;
			case Tank:		taunt();								break;
			case Samurai:	doubleSlash(findTarget(theirTeam));		break;
			case BlackMage:	curse(findTarget(theirTeam));			break;
			case Phoenix:	revive(findRevive(myTeam));				break;
			case Cherry:	fortuneCookie(theirTeam, arena);		break;
		}
	}
	
	public void takeAction(Arena arena) {
		if(currentSP == 0) {
			switch(type) {
				case BlackMage: System.out.println("# " + arena.getTeam(this) + "["+ arena.getCoordinateCol(this) +"]["+ arena.getCoordinateRow(this) + "]" + " {"+ this.type + "}" + " curses " + arena.getTeam(findTarget(arena.getOtherTeam(this))) +  "["+ arena.getCoordinateCol(findTarget(arena.getOtherTeam(this))) +"][" + arena.getCoordinateRow(findTarget(arena.getOtherTeam(this))) 
					+ "] {" + findTarget(arena.getOtherTeam(this)).getType() + "}");											break;
				case Healer:	System.out.println("# " + arena.getTeam(this) + "["+ arena.getCoordinateCol(this) +"]["+ arena.getCoordinateRow(this) + "]" + " {"+ this.type + "}" + " heals " + arena.getTeam(findHealTarget(arena.getMyTeam(this))) +  "["+ arena.getCoordinateCol(findHealTarget(arena.getMyTeam(this))) +"][" + arena.getCoordinateRow(findHealTarget(arena.getMyTeam(this))) 
					+ "] {" + findHealTarget(arena.getMyTeam(this)).getType() + "}");											break;
				case Phoenix:	if(findRevive(arena.getMyTeam(this)) != null) System.out.println("# " + arena.getTeam(this) + "["+ arena.getCoordinateCol(this) +"]["+ arena.getCoordinateRow(this) + "]" + " {"+ this.type + "}" + " revives " + arena.getTeam(findRevive(arena.getMyTeam(this))) +  "["+ arena.getCoordinateCol(findRevive(arena.getMyTeam(this))) +"][" + arena.getCoordinateRow(findRevive(arena.getMyTeam(this))) 
					+ "] {" + findRevive(arena.getMyTeam(this)).getType() + "}");
								else {System.out.println("# " + arena.getTeam(this) + "["+ arena.getCoordinateCol(this) +"]["+ arena.getCoordinateRow(this) + "]" + " {"+ this.type + "}" + " revives No one");}	break;
				case Samurai: 	System.out.println("# " + arena.getTeam(this) + "["+ arena.getCoordinateCol(this) +"]["+ arena.getCoordinateRow(this) + "]" + " {"+ this.type + "}" 
						+" double-slashes " + arena.getTeam(findTarget(arena.getOtherTeam(this))) +  "["+ arena.getCoordinateCol(findTarget(arena.getOtherTeam(this))) +"][" + arena.getCoordinateRow(findTarget(arena.getOtherTeam(this))) 
						+ "] {" + findTarget(arena.getOtherTeam(this)).getType() + "} "+ " for " + this.atk*2 + " damage");		break;
				case Tank:		System.out.println("# " + arena.getTeam(this) + "["+ arena.getCoordinateCol(this) +"]["+ arena.getCoordinateRow(this) + "]" + " {"+ this.type + "}" + " is taunting"); break;
			}
			useSpecialAbility(arena.getMyTeam(this), arena.getOtherTeam(this), arena.getRound(), arena);
			currentSP = numSP-1;
		}
		else {
			if(currentSP == (numSP-1)) {
				if(type == PlayerType.BlackMage)							fixCurse(arena.getOtherTeam(this));
				if(type == PlayerType.Cherry && currentSP == (numSP-1))		fixCookie(arena.getOtherTeam(this));
			}
			if(isTaunting) isTaunting = false;
			
			System.out.println("# " + arena.getTeam(this) + "["+ arena.getCoordinateCol(this) +"]["+ arena.getCoordinateRow(this) + "]" + " {"+ this.type + "}" 
					+" attacks " + arena.getTeam(findTarget(arena.getOtherTeam(this))) +  "["+ arena.getCoordinateCol(findTarget(arena.getOtherTeam(this))) +"][" + arena.getCoordinateRow(findTarget(arena.getOtherTeam(this))) 
					+ "] {" + findTarget(arena.getOtherTeam(this)).getType() + "} "+ " for " + this.atk + " damage");
			
			attack(findTarget(arena.getOtherTeam(this)));
			currentSP--;
		}
	}
	
	//# ==== Add-ons ====
	
	public boolean frontStillAlive(Player[][] targetTeam) {
		boolean stillAlive = false;
		for(int column = 0; column < targetTeam[0].length; column++) {
			if(targetTeam[0][column].isAlive) {
				stillAlive = true;
				break;
			}
		}
		return stillAlive;
	}
	
	public Player findTarget(Player[][] targetTeam) {
		//find the first taunting tank of the opposite team
		Player min = null;
		for(int column = 0; column<2;column++) {
			for(int row = 0;row<targetTeam[0].length;row++) {
				if(targetTeam[column][row].isAlive && targetTeam[column][row].isTaunting) {
					if(min == null) {
						if(targetTeam[column][row].isAlive) min = targetTeam[column][row];
					}
					else {
						if(targetTeam[column][row].isAlive && targetTeam[column][row].currentHP < min.currentHP) {
							min = targetTeam[column][row];
						}
					}
				}
			}
		}
		if(min != null) return min;
		//find the lowest HP from front to back (Front priority)
		min = null;
		for(int i=0;i<targetTeam[0].length;i++) {
			if(min == null) {
				if(targetTeam[0][i].isAlive) min = targetTeam[0][i];
			}
			else {
				if(targetTeam[0][i].isAlive && targetTeam[0][i].currentHP < min.currentHP) {
					min = targetTeam[0][i];
				}
			}
		}
		
		if(min == null) {
			for(int i=0;i<targetTeam[0].length;i++) {
				if(min == null && targetTeam[1][i].isAlive) {
					min = targetTeam[1][i];
				}
				else if(targetTeam[1][i].isAlive) {
					if(min.currentHP > targetTeam[1][i].currentHP)	min = targetTeam[1][i];
				}
			}
		}
		else {
			return min;
		}
		return min;
	}
	
	public Player findHealTarget(Player[][] targetTeam) {
		//find the first taunting tank of the opposite team
		Player min = null;
		
		for(int column = 0;column<2;column++) {
			for(int row = 0;row < targetTeam[0].length;row++) {
				if(min == null) {
					if(targetTeam[column][row].isAlive && targetTeam[column][row].currentHP != targetTeam[column][row].maxHP) min = targetTeam[column][row];
				}
				else {
					if(targetTeam[column][row].isAlive && targetTeam[column][row].currentHP < min.currentHP && targetTeam[column][row].currentHP != targetTeam[column][row].maxHP) {
						min = targetTeam[column][row];
					}				}
			}
		}
		if(min != null) return min;
		else {
			for(int column = 0;column<2;column++) {
				for(int row = 0;row < targetTeam[0].length;row++) {
					if(min == null) {
						if(targetTeam[column][row].isAlive) min = targetTeam[column][row];
					}
					else {
						if(targetTeam[column][row].isAlive && targetTeam[column][row].currentHP < min.currentHP) {
							min = targetTeam[column][row];
						}				}
				}
			}
		}
		return min;
	}
	
	public Player findRevive(Player[][] myTeam) {
		for(int column = 0; column<2;column++) {
			for(int row = 0;row<myTeam[0].length;row++) {
				if(!myTeam[column][row].isAlive) {
					return myTeam[column][row];
				}
			}
		}
		return null;
	}
	
	
	//# ====== Player Special ======
	public void heal(Player target) {
		if(target != null) {
			if(!target.isCursed) {
				target.currentHP += target.maxHP*0.25;
			}
			if(target.currentHP>target.maxHP) {
				target.currentHP = target.maxHP;
			}
		}
	}
	
	public void taunt() {
		isTaunting = true;
	}
	
	public void doubleSlash(Player target) {
		attack(target);
		attack(target);
	}
	
	public void curse(Player target) {
		target.isCursed = true;
	}
	
	public void revive(Player target) {
		if(target == null) ;
		else {
			target.isAlive = true;
			target.currentHP = target.maxHP*0.3;
			target.currentSP = target.numSP-1;
		}
	}
	
	public void fortuneCookie(Player[][] theirTeam, Arena arena) {
		for(int column = 0; column<2;column++) {
			for(int row = 0;row<theirTeam[0].length;row++) {
				if(theirTeam[column][row].isAlive) {
					theirTeam[column][row].isSleeping = true;
					System.out.println("# " + arena.getTeam(this) + "["+ arena.getCoordinateCol(this) +"]["+ arena.getCoordinateRow(this) + "]" + " {"+ this.type + "}" 
							+" lures " + arena.getTeam(theirTeam[column][row]) +  "["+ arena.getCoordinateCol(theirTeam[column][row]) +"][" + arena.getCoordinateRow(theirTeam[column][row]) 
							+ "] {" + theirTeam[column][row].getType() + "}");
				}
			}
		}
	}
	
	public void fixCurse(Player[][] theirTeam) {
		for(int column = 0; column<2;column++) {
			for(int row = 0;row<theirTeam[0].length;row++) {
				if(theirTeam[column][row].isCursed) {
					theirTeam[column][row].isCursed = false;
				}
			}
		}
	}
	
	public void fixCookie(Player[][] theirTeam) {
		for(int column = 0; column<2;column++) {
			for(int row = 0;row<theirTeam[0].length;row++) {
				if(theirTeam[column][row].isSleeping) {
					theirTeam[column][row].isSleeping = false;
				}
			}
		}
	}


	@Override
	
	public String toString() {
		return "["+this.type.toString()+" HP:"+this.currentHP+"/"+this.maxHP+" ATK:"+this.atk+"]["
				+((this.isCursed())?"C":"")
				+((this.isTaunting())?"T":"")
				+((this.isSleeping())?"S":"")
				+"]";
	}
}

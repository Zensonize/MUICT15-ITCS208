import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class Arena {
	public enum Row {Front, Back};
	public enum Team{A,B};
	
	private Player[][] teamA = null;
	private Player[][] teamB = null;
	private int numRowPlayers = 0;
	
	public static final int MAXROUNDS = 10;
	public static final int MAXEACHTYPE = 3;
	private final Path logFile = Paths.get("battle_log.txt");
	
	private int numRounds = 1;
	
	public Arena(int _numRowPlayers) {
		teamA = new Player[2][_numRowPlayers];
		teamB = new Player[2][_numRowPlayers];
		numRowPlayers = _numRowPlayers;
		try {
			Files.deleteIfExists(logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isMemberOf(Player player, Team team) {
		boolean isFound = false; 
		if(team.ordinal() == 0) {
			for(int column=0;column<2;column++) {
				for(int row=0;row<teamA[0].length;row++) {
					if(player.equals(teamA[column][row])) isFound = true;
				}
			}
		}
		return isFound;
	}
	
	public void addPlayer(Team team, Player.PlayerType pType, Row row, int position) {
		if(team == Team.A) {
			if(teamA[row.ordinal()][position-1] == null) {
				teamA[row.ordinal()][position-1] = new Player(pType);
			}
		}
		else {
			if(teamB[row.ordinal()][position-1] == null) {
				teamB[row.ordinal()][position-1] = new Player(pType);
			}
		}
	}
	
	public boolean validatePlayers() {
		int[] playA = {0,0,0,0,0,0};
		int[] playB = {0,0,0,0,0,0};
		for(int i=0;i<numRowPlayers;i++) {
			playA[teamA[0][i].getType().ordinal()]++;
			playA[teamA[1][i].getType().ordinal()]++;
			playB[teamB[0][i].getType().ordinal()]++;
			playB[teamB[1][i].getType().ordinal()]++;
			if(teamA[0][i] == null || teamA[1][i] == null || teamB[0][i] == null || teamB[1][i] == null) {
				return false;
			}
		}
		for(int i=0;i<6;i++) {
			if(playA[i] > MAXEACHTYPE || playB[i] > MAXEACHTYPE) return false;
		}
		return true;
	}
	
	public static double getSumHP(Player[][] team) {
		double sum = 0;
		for(int i=0;i<team[0].length;i++) {
			sum+= team[0][i].getCurrentHP();
			sum+= team[1][i].getCurrentHP();
		}
		return sum;
	}
	
	public Player[][] getWinningTeam(){
		if(aliveCount(teamA)==aliveCount(teamB)) {
			if(getSumHP(teamA)>getSumHP(teamB)) {
				return teamA;
			}
			else return teamB;
		}
		else if(aliveCount(teamA)>aliveCount(teamB)) {
			return teamA;
		}
		else return teamB;
	}
	
	public void startBattle() {
		boolean gameEnd = false;
		while(numRounds < 100) {
			
			System.out.println("\n@ Round " + numRounds + ":");
			for(int column = 0; column<2;column++) {
				for(int row = 0;row<numRowPlayers;row++) {
					if(teamA[column][row].isAlive() && teamA[column][row].isSleeping() == false) {
						teamA[column][row].takeAction(this);
						if(aliveCount(teamB) == 0) {gameEnd = true; break;}
					}
				}
				if(gameEnd)break;
			}
			if(!gameEnd) {
				for(int column = 0; column<2;column++) {
					for(int row = 0;row<numRowPlayers;row++) {
						if(teamB[column][row].isAlive() && !teamB[column][row].isSleeping()) {
							teamB[column][row].takeAction(this);
							if(aliveCount(teamA) == 0) {gameEnd = true; break;}
						}
					}
					
					if(gameEnd)break;
				}
				System.out.println("");
				displayArea(this, true);
			}
			logAfterEachRound();
			numRounds++;
			if(gameEnd) break;
		}
		System.out.println("");
		displayArea(this, true);
		logAfterEachRound();
		
	}

	//# ==== Add-ons ====
	
	public Player[][] getMyTeam(Player player) {
		if(isMemberOf(player, Team.A)) {
			return teamA;
		}
		else return teamB;
	}
	
	public Player[][] getOtherTeam(Player player) {
		if(isMemberOf(player, Team.A)) {
			return teamB;
		}
		else return teamA;
	}
	
	public int getRound() {
		return numRounds;
	}
	
	public int aliveCount(Player[][] team) {
		int alive = 0;
		for(int column = 0; column<2;column++) {
			for(int row = 0;row<team[0].length;row++) {
				if(team[column][row].isAlive()) alive++;
			}
		}
		return alive;
	}
	
	public static void displayArea(Arena arena, boolean verbose) {
		StringBuilder str = new StringBuilder();
		if(verbose)
		{
			str.append(String.format("%43s   %40s","Team A","")+"\t\t"+String.format("%-38s%-40s","","Team B")+"\n");
			str.append(String.format("%43s","BACK ROW")+String.format("%43s","FRONT ROW")+"  |  "+String.format("%-43s","FRONT ROW")+"\t"+String.format("%-43s","BACK ROW")+"\n");
			for(int i = 0; i < arena.numRowPlayers; i++)
			{
				str.append(String.format("%43s",arena.teamA[1][i])+String.format("%43s",arena.teamA[0][i])+"  |  "+String.format("%-43s",arena.teamB[0][i])+String.format("%-43s",arena.teamB[1][i])+"\n");
			}
		}
	
		str.append("@ Total HP of Team A = "+getSumHP(arena.teamA)+". @ Total HP of Team B = "+getSumHP(arena.teamB)+"\n\n");
		System.out.print(str.toString());
	}
	
	private void logAfterEachRound()
	{
		try {
			Files.write(logFile, Arrays.asList(new String[]{numRounds+"\t"+getSumHP(teamA)+"\t"+getSumHP(teamB)}), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Team getTeam(Player player) {
		if(isMemberOf(player, Team.A)) 	return Team.A;
		else return Team.B;
	}
	
	public int getCoordinateRow(Player player) {
		if(isMemberOf(player, Team.A)) {
			for(int column=0;column<2;column++) {
				for(int row=0;row<teamA[0].length;row++) {
					if(player.equals(teamA[column][row])) return row+1;
				}
			}
		}
		else {
			for(int column=0;column<2;column++) {
				for(int row=0;row<teamB[0].length;row++) {
					if(player.equals(teamB[column][row])) return row+1;
				}
			}
		}
		return 0;
	}
	
	public Row getCoordinateCol(Player player) {
		if(isMemberOf(player, Team.A)) {
			for(int column=0;column<2;column++) {
				for(int row=0;row<teamA[0].length;row++) {
					if(player.equals(teamA[column][row])) if(column == 0) return Row.Front; else return Row.Back;
				}
			}
		}
		else {
			for(int column=0;column<2;column++) {
				for(int row=0;row<teamB[0].length;row++) {
					if(player.equals(teamB[column][row])) if(column == 0) return Row.Front; else return Row.Back;
				}
			}
		}
		return Row.Front;
	}
}

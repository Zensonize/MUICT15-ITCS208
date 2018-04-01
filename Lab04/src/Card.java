//# COPYRIGHT KrittametK

public class Card {
	String cardNotation;
	String Desc[] = {"Ace", "Two", "Three", "Four" , "Five", "Jack" , "Queen" ,"King", "Spades"};
	
	public Card() {
		
	}
	public Card(String notation) {
		this.cardNotation = notation;
	}
	
	public void setNotation(String notation) {
		cardNotation = notation;
	}
	public String getDescription() {
		StringBuilder desc = new StringBuilder();
		boolean correct = true;
		if(cardNotation.charAt(0) >= '2' && cardNotation.charAt(0) <= '5') {
			desc.append(Desc[(int) cardNotation.charAt(0)-'1'] + " ");
		}
		else correct = false;
		
		if(correct == true) {
			switch(cardNotation.charAt(1)) {
				case 'A': desc.append(Desc[0] + " "); break;
				case 'J': desc.append(Desc[5] + " "); break;
				case 'Q': desc.append(Desc[6] + " "); break;
				case 'K': desc.append(Desc[7] + " "); break;
				case 'S': desc.append(Desc[8] + " "); break;
				default : correct = false;
			}
		}
		
		if(correct == true) return desc.toString();
		else return "Unkonwn";
	}
}

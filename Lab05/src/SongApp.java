//# COPYRIGHT KrittametK

public class SongApp {

	public static void main(String[] args) {
		System.out.println("test");
		Playlist myPlaylist = new Playlist("My Favourite Songs Playlist");
		
		System.out.println("Welcome to SongApp");
		System.out.println("\nAdd songs --------------------------");
		myPlaylist.addSong(new Song("Perfect", 4.21));
		myPlaylist.addSong(new Song("How Long", 3.30));
		myPlaylist.addSongByIndex(new Song("End Game",4.11), 0);
		myPlaylist.addSongByIndex(new Song("Anywhere",3.35), 2);
		myPlaylist.showPlaylist();
		
		System.out.println("\nRearrange songs ---------------------");
		myPlaylist.moveUp(1);
		myPlaylist.moveDown(2);
		myPlaylist.showPlaylist();
		
		System.out.println("\nRemove songs -----------------------");
		myPlaylist.removeSongByTitle("End Game");
		myPlaylist.removeSongByIndex(2);
		myPlaylist.showPlaylist();
		
		System.out.println("\nCheck error ------------------------");
		myPlaylist.addSongByIndex(new Song("Find you", 3.38), 3);
		myPlaylist.removeSongByTitle("Find you");
		myPlaylist.removeSongByIndex(3);
	}

}

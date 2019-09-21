import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class JukeboxHeroEC {

	public static void main(String[] args) {
		
		ArrayList<Song> songList = new ArrayList<Song>();
		
		
		
		
		Scanner kbd = new Scanner(System.in);
		
		// Starting menu
		System.out.println("***********************");
		System.out.println("*      Program Menu   *");
		System.out.println("***********************");
		System.out.println("(L)oad catalog");
		System.out.println("(S)earch catalog");
		System.out.println("(A)nalyse catalog");
		System.out.println("(P)rint catalog");
		System.out.println("(Q)uit");
		System.out.println("\nPlease enter a command (press 'm' for Menu)");
		
		// String firstInput = kbd.next();
		//  char c = firstInput.charAt(0);
		char c = kbd.next().charAt(0);
		while (c != 'q') {
			int input = c;
			
			switch(input) {
			
			case 76: //L or l, load
			case 108:{
				JukeboxHeroEC.loadCatalog(songList, kbd);
				c = kbd.next().charAt(0); //asks for input again
				input= c; //assigns input for switch to user input
				break;
				
			} //end load
			case 83: //S or s, search
			case 115: {
				JukeboxHeroEC.searchCatalog(songList, kbd);
				c = kbd.next().charAt(0); //asks for input again
				input= c; //assigns input for switch to user input
				break;
			}
			case 65: //A or a, analyse
			case 97: {
				JukeboxHeroEC.analyseCatalog(songList);
				c = kbd.next().charAt(0); //asks for input again
				input= c; //assigns input for switch to user input
				break;
			}
			case 80: //P or p, print
			case 112: {
				JukeboxHeroEC.printCatalog(songList);
				break;
			}
			case 77: //M or m, menu
			case 109: {
				JukeboxHeroEC.displayMenu();
				c = kbd.next().charAt(0); //asks for input again
				input= c; //assigns input for switch to user input
				
				break;
				
			}
			default: //in case of non given input
				System.out.println("Invalid command:");
				System.out.println("Please enter a command (press 'm' for Menu)");
				c = kbd.next().charAt(0);
				input = c;
			}
			
			
		}
		if (c == 'q') {
			System.out.println("Program stopped. Have a nice day! (Or don't.)");
		}
		kbd.close();
	}
private static void displayMenu() {
	System.out.println("");
	System.out.println("***********************");
	System.out.println("*      Program Menu   *");
	System.out.println("***********************");
	System.out.println("(L)oad catalog");
	System.out.println("(S)earch catalog");
	System.out.println("(A)nalyse catalog");
	System.out.println("(P)rint catalog");
	System.out.println("(Q)uit");
	System.out.println("\nPlease enter a command (press 'm' for Menu)");
}
private static void loadCatalog(ArrayList<Song> songList, Scanner kbd) {
	String artist = "";
	String album = "";
	String title = "";
	int duration;
	
	System.out.println("Load catalog. . ." );
	
	System.out.println("Enter filename: ");
	String catalogFile = kbd.next();
//	System.out.println("File is: " + catalogFile);
	if ((catalogFile = kbd.nextLine()).trim() == "") {
//		 while ((catalogFile = kbd.nextLine()).trim() == "") {
//		//	System.out.println("Please enter a filename: ");
//			// catalogFile = kbd.next();
//		}
		}
	File catalogReader = new File(catalogFile);
//	System.out.println("File is read as: " + catalogReader);
	if (catalogReader.exists() && catalogReader.isFile()) {
	try {
		
	//	System.out.println(catalogReader + "exists.");
		songList.clear();
			Scanner catalogScanner = new Scanner(catalogReader);
			while (catalogScanner.hasNextLine()) {
				String songLine = new String(catalogScanner.nextLine());
				Scanner songScan = new Scanner(songLine);
				songScan.useDelimiter(",");
				while (songScan.hasNext()) {
					artist = songScan.next();
					album = songScan.next();
					title = songScan.next();
					duration = Integer.parseInt(songScan.next());
					
					Song song = new Song(title, artist, album, duration);
					
					songList.add(song);
					
				}
			
				
				songScan.close();
			}
			System.out.println(songList.size() + " song(s) succesfully loaded.");
//			c = kbd.next().charAt(0); //asks for input again
//			input= c; //assigns input for switch to user input
			catalogScanner.close();
		
		}
		 catch (FileNotFoundException e) {
			System.out.println("File of " + catalogReader + "not found.");
			e.printStackTrace();
		
		}
	}
		else {
			System.out.println("File of " + catalogReader + " not found.");
		
	
//	System.out.println("test" );
	
}
	System.out.println("\nPlease enter a command (press 'm' for Menu)");
	
	
}
private static void printCatalog(ArrayList<Song> songList) {
	
	System.out.println("Song list contains " + songList.size() + " songs:");
	System.out.println("--------------------------");
	for (Object o: songList) {
		System.out.println(o.toString());
	}
	System.out.println("\nPlease enter a command (press 'm' for Menu)");
	
	
}
private static void searchCatalog(ArrayList<Song> songList, Scanner kbd) {
	ArrayList<Song> searchResults = new ArrayList<Song>();
	
	System.out.println("Search Catalog. . . ");
	System.out.println("Please enter the search query: ");
	String searchQuery = kbd.next();
	
	for (int i = 0; i < songList.size(); i++ ) {
		if (songList.get(i).getTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
			searchResults.add(songList.get(i));
			//System.out.println(songList.get(i));
			
		}
		
		
	}
	System.out.println("Found " + searchResults.size() +  (searchResults.size() == 1? " match.":
		" matches."));
	for (Object o : searchResults) {
		System.out.println(o);
	}
	System.out.println("\nPlease enter a command (press 'm' for Menu)");
	
	
}
private static void analyseCatalog(ArrayList<Song> songList) {
	ArrayList<String> artistList = new ArrayList<String>();
	ArrayList<String> albumList = new ArrayList<String>();
	int totalTime = 0;
	
	System.out.println("Catalog Analysis... ");
	for (int i=0; i< songList.size(); i++) {
		if ( !(albumList.contains(songList.get(i).getAlbum()))){
			String albumAdd = songList.get(i).getAlbum().toString();
			albumList.add(albumAdd);
		}
		
	}
	for (int i=0; i<songList.size(); i++) {
		if (!artistList.contains(songList.get(i).getArtist())) {
			String artistAdd = songList.get(i).getArtist().toString();
			artistList.add(artistAdd);
		}
	}
	System.out.println("\tNumber of Artists: " + artistList.size());
	System.out.println("\tNumber of Albums: " + albumList.size());
	System.out.println("\tNumber of Songs: " + songList.size());
	for (int i=0; i< songList.size(); i++) {
		totalTime += songList.get(i).getPlayTime();
		
	}
	System.out.println("\tCatalog Playtime: " + totalTime);
	
	System.out.println("\nPlease enter a command (press 'm' for Menu)");
	
}
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class JukeboxHero {
	/**
	 * CS 121 Project 2 - Jukebox Hero
	 * 
	 * This project allows the user to input a file containing song information,
	 * and that file is then read. Afterwards the user can search, analyze, or print
	 * the song list.
	 * 
	 * @author Austen Hale
	 */
	public static void main(String[] args) {
		//variables
		ArrayList<Song> songList = new ArrayList<Song>();
		ArrayList<Song> searchResults = new ArrayList<Song>();
		ArrayList<String> artistList = new ArrayList<String>();
		ArrayList<String> albumList = new ArrayList<String>();
		String artist = "";
		String album = "";
		String title = "";
		int duration;
		int totalTime = 0;
		String catalogFile = null;
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
		
//		 String firstInput = kbd.next();
//		  char c = firstInput.charAt(0);
//		 
		char c = kbd.next().charAt(0);
		while (c != 'q' && c != 'Q') {
			int input = c; //converts char entry to int to use for switch
			
			switch(input) {
			
			case 76: //L or l, load
			case 108:{
				
				System.out.println("Load catalog. . ." );
				
				 System.out.println("Enter filename: ");
				 catalogFile = kbd.nextLine();
				if ((catalogFile = kbd.nextLine()).trim() == "") {
//				 while ((catalogFile = kbd.nextLine()).trim() == "") {
//				//	System.out.println("Please enter a filename: ");
//					// catalogFile = kbd.next();
//				}
				}
				
			//	System.out.println("File is: " + catalogFile);
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
//						c = kbd.next().charAt(0); //asks for input again
//						input= c; //assigns input for switch to user input
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
				c = kbd.next().charAt(0); //asks for input again
				input= c; //assigns input for switch to user input
				break;
			}
			case 83: //S or s, search
			case 115: {
				searchResults.clear();
				System.out.println("Search Catalog. . . ");
				System.out.println("Please enter the search query: ");
				String searchQuery = kbd.next();
				
				for (int i = 0; i < songList.size(); i++ ) {
					if (songList.get(i).getTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
						searchResults.add(songList.get(i));
						//System.out.println(searchResults);
						
					}
					
					
				}
				System.out.println("Found " + searchResults.size() +  (searchResults.size() == 1? " match.":
					" matches."));
				System.out.println("----------------------");
				for (Object o : searchResults) {
					System.out.println(o);
				}
				System.out.println("\nPlease enter a command (press 'm' for Menu)");
				c = kbd.next().charAt(0); //asks for input again
				input= c; //assigns input for switch to user input
				break;
			}
			case 65: //A or a, analyse
			case 97: {
				System.out.println("Catalog Analysis... ");
				for (int i=0; i< songList.size(); i++) {
					if ( !(albumList.contains(songList.get(i).getAlbum()))){ //adds album if album is not already added
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
					totalTime += songList.get(i).getPlayTime(); //adds all playtimes together
					
				}
				System.out.println("\tCatalog Playtime: " + totalTime);
				
				System.out.println("\nPlease enter a command (press 'm' for Menu)");
				c = kbd.next().charAt(0); //asks for input again
				input= c; //assigns input for switch to user input
				break;
			}
			case 80: //P or p, print
			case 112: {
				System.out.println("Song list contains " + songList.size() + " songs:");
				System.out.println("--------------------------");
				for (Object o: songList) {
					System.out.println(o.toString());
				}
				System.out.println("\nPlease enter a command (press 'm' for Menu)");
				c = kbd.next().charAt(0); //asks for input again
				input= c; //assigns input for switch to user input
				break;
			}
			case 77: //M or m, menu
			case 109: {
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
				
				c = kbd.next().charAt(0); //asks for input again
				input= c; //assigns input for switch to user input
				
				break;
				
			}
//			case 32: {
//				System.out.println("Invalid command:");
//				System.out.println("Please enter a command (press 'm' for Menu)");
//				c = kbd.next().charAt(0);
//				input = c;
//			}
			default: //in case of non given input
				System.out.println("Invalid command:");
				System.out.println("Please enter a command (press 'm' for Menu)");
				c = kbd.next().charAt(0);
				input = c;
			}
			
			
		}
		if (c == 'q' || c == 'Q') {
			System.out.println("Program stopped. Have a nice day! (Or don't.)");
			kbd.close();
		}
		else {
			System.out.println("Invalid input. Try again");
			c = kbd.next().charAt(0);
		}
		
	}

}


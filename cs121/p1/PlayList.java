import java.text.DecimalFormat;
import java.util.Scanner;

public class PlayList {
	/**
	 * CS 121 Project 1 - Playlist Analyzer
	 * 
	 * This project allows the user to input three songs, listing their song titles, artist,
	 *  file names, and song play time.
	 * 
	 * @author Austen Hale
	 */

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		
		//song title
		System.out.println("Enter your song title: ");
		String songTitle = scan.nextLine();		
		
		
		//song artist
		System.out.println("Enter your song's artist: ");
		String songArtist = scan.nextLine();
		
		//song play time
		System.out.println("Enter your song's play time (mm:ss)");
		String songPlayTime = scan.nextLine();
		
		//song file path
		System.out.println("Enter your song's file path: ");
		String songFilePath = scan.nextLine();
		
		
		//Play Time Conversion
		int colonLength = songPlayTime.indexOf(":");
		String minutes = songPlayTime.substring(0, colonLength);
		String seconds = songPlayTime.substring(colonLength+1);
		
		int mMinutes = Integer.parseInt(minutes); //convert strings to ints
		int sSeconds = Integer.parseInt(seconds); //convert strings to ints
		int playTime = (mMinutes *60) + sSeconds;
		
		//New Song Object
		Song song1 = new Song(songTitle, songArtist, playTime, songFilePath);
		System.out.println(song1.toString());
		
		System.out.println("================================");
		
		//song title2
		System.out.println("Enter your song title: ");
		 songTitle = scan.nextLine();
		
		//song artist2
		System.out.println("Enter your song's artist: ");
		 songArtist = scan.nextLine();
		
		//song play time2
		System.out.println("Enter your song's play time (mm:ss)");
		 songPlayTime = scan.nextLine();
		
		//song file path2
		System.out.println("Enter your song's file path: ");
		 songFilePath = scan.nextLine();
		
		
		//Play Time Conversion
		int colonLength2 = songPlayTime.indexOf(":");
		String minutes2 = songPlayTime.substring(0, colonLength2);
		String seconds2 = songPlayTime.substring(colonLength2+1);
		
		int mMinutes2 = Integer.parseInt(minutes2); //convert strings to ints
		int sSeconds2 = Integer.parseInt(seconds2); //convert strings to ints
		int playTime2 = (mMinutes2 *60) + sSeconds2;
		
		//New Song Object, 2
		Song song2 = new Song(songTitle, songArtist, playTime2, songFilePath);
		System.out.println(song2.toString());
		
		
		System.out.println("================================");
		
		//song title3
		System.out.println("Enter your song title: ");
		String songTitle3 = scan.nextLine();
		
		//song artist3
		System.out.println("Enter your song's artist: ");
		String songArtist3 = scan.nextLine();
		
		//song play time3
		System.out.println("Enter your song's play time (mm:ss)");
		String songPlayTime3 = scan.nextLine();
		
		//song file path3
		System.out.println("Enter your song's file path: ");
		String songFilePath3 = scan.nextLine();
		
		
		//Play Time Conversion
		int colonLength3 = songPlayTime3.indexOf(":");
		String minutes3 = songPlayTime3.substring(0, colonLength3);
		String seconds3 = songPlayTime3.substring(colonLength3+1);
		
		int mMinutes3 = Integer.parseInt(minutes3); //convert strings to ints
		int sSeconds3 = Integer.parseInt(seconds3); //convert strings to ints
		int playTime3 = (mMinutes3 *60) + sSeconds3;
		
		//New Song Object, 3
		Song song3 = new Song(songTitle3, songArtist3, playTime3, songFilePath3);
		System.out.println(song3.toString());
		
		System.out.println("================================");
		
		double averageLength = (double)(song1.getPlayTime() + song2.getPlayTime() + song3.getPlayTime())/3.0;
		DecimalFormat dcf = new DecimalFormat("#.00");
		
		System.out.println("Average play time: " + dcf.format(averageLength));
		
		int closest1 = Math.abs(song1.getPlayTime() - 240);
		int closest2 = Math.abs(song2.getPlayTime() - 240);
		int closest3 = Math.abs(song3.getPlayTime() - 240);
		
		
			
		if (closest1 <= closest2 && closest1 <= closest3) {
			System.out.println("Song with play time closest to 240 seconds is: " + song1.getTitle());
		}
		else if (closest2 <= closest1 && closest2 <= closest3) {
			System.out.println("Song with play time closest to 240 seconds is: " + song2.getTitle());
		}
		else  {
			System.out.println("Song with play time closest to 240 seconds is: " + song3.getTitle());
		}
		
		System.out.println("==============================================================================");
		System.out.println("Title                Artist               File Name                  Play Time");
		System.out.println("==============================================================================");
		
		
		//if song 1 has lowest play time
		if (song1.getPlayTime() <= song2.getPlayTime() && song1.getPlayTime() <= song3.getPlayTime()) {
			System.out.println(song1.toString());
			if (song2.getPlayTime() <= song3.getPlayTime()) { //if song 2 is greater than or equal to song 3 time
				System.out.println(song2);
				System.out.println(song3);
			}else {
				System.out.println(song3);
				System.out.println(song2);
			}
		}
		//if song 2 has lowest play time
		else if (song2.getPlayTime() <= song1.getPlayTime() && song2.getPlayTime() <= song3.getPlayTime() ) {
			System.out.println(song2);
			if (song1.getPlayTime () <= song3.getPlayTime( )) { //if song 1 is greater than or equal to song 3 time
				System.out.println(song1);
				System.out.println(song3);
			}else {
				System.out.println(song3);
				System.out.println(song1);
			}
		}
		//if song 3 has lowest play time
		else   {
			System.out.println(song3);
			if (song1.getPlayTime() <= song2.getPlayTime() ) {
				System.out.println(song1);
				System.out.println(song2);
			}else {
				System.out.println(song2);
				System.out.println(song1);
			}
		}
		
		
		System.out.println("==============================================================================");
		/*
		//testing prints
		System.out.println("Minutes: " + minutes + " Seconds: " + seconds);
		System.out.println(mMinutes);
		System.out.println(songTitle + songArtist + songPlayTime + songFilePath);
		*/
		scan.close();

	}

}

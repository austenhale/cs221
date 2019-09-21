import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Book class, creates book and contains methods
 * @author austenhale
 *
 */

public class Book implements BookInterface, BookReaderInterface{

	//represents a single book
	private String title;
	private String author;
	private String genre;
	private String filename;
	 
	
	
	public Book(String title, String author) {
		this.title = title;
		this.author = author;
		this.genre = null;
		this.filename = null;
		
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename){
		this.filename = filename;
	}
	public String toString() {
		String output = "";
		output += "Title: " + title + ", Author: " + author + ", Genre: " + genre + ", Filename: " + filename;
		return output;
	}
	public boolean isValid() {
		
		if ((title != null && author != null && genre != null && filename != null)) {
			File filenames = new File(filename);
				if (filenames.exists() && filenames.isFile()){
			return true;
		}
				 
			
		
		else {
			return false;
		}
				
		}
		return false;
	}
public String getText() {
	String fileContents = "";
	File filenames = new File(filename);
	
	if (filenames.exists() && filenames.isFile()) {
		try {
			Scanner fileScanner = new Scanner(filenames);
			while (fileScanner.hasNextLine()) {
				fileContents += fileScanner.nextLine() + "\n";

		}
			fileScanner.close();
		}
		catch (FileNotFoundException e) {
			fileContents = "File was unable to be opened because it doesn't exist.";
			e.printStackTrace();
		}
		
	
	}
	else {
		fileContents = "";
	}
	return fileContents;
	
}
@Override
public BufferedReader getReader() {
	File filenames = new File(filename);
	if (filenames.exists() && filenames.isFile()) {
	try {
		
		BufferedReader bfr = new BufferedReader(new FileReader(filenames));
		return bfr;
	} catch (FileNotFoundException e) {

		System.out.println("File does not exist.");
		e.printStackTrace();
		return null;
	}
	}
	
	return null;
}
}
	

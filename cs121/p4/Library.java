import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Library class, creates library and its methods
 * @author austenhale
 *
 */
public class Library implements LibraryInterface{

	private ArrayList<Book> books;
	
	
	public Library() {
		books = new ArrayList<Book>();
		
		
	}
	
	public ArrayList<Book> getBooks() {
		ArrayList<Book>booksCopy = new ArrayList<Book>();
		for (Book b: books) {
			Book newBook = b ;
			booksCopy.add(newBook );
			
		}
		
		
		return booksCopy;
	}
	
	public void addBook(Book book) {
		
		books.add(book);
		
	}
	public void removeBooks() {
		books.removeAll(books);
	}
	public void removeBook(int index) {
		if (index < books.size() && index >= 0) {
			books.remove(index);
		}
		else {
			System.out.println("Index out of bounds.");
		}
	
	}
	public Book getBook(int index){
	
//		ArrayList<Book>booksCopy = new ArrayList<Book>();
//		for (Book b: books)
		if (index < books.size() && index > -1) {
			
			return books.get(index);
			}
		else {
			return null;
			}
		
		
	}
	public String toString() {
		String output = "";
		for (Book b: books) {
			output += b.toString();
		}
		return output;
	}

	public void loadLibraryFromCSV(String csvImportFile) {
		File csvFile = new File(csvImportFile);
		try {
			if (csvFile.exists() && csvFile.isFile()) {
				Scanner csvScanner = new Scanner(csvFile);
				while (csvScanner.hasNextLine()) {
					String csvLine = new String (csvScanner.nextLine());
					Scanner scanCSV = new Scanner(csvLine);
					scanCSV.useDelimiter(",");
					while (scanCSV.hasNext()) {
						String title = scanCSV.next();
						String author = scanCSV.next();
						String genre = scanCSV.next();
						String filepath = scanCSV.next();
						
						Book book = new Book(title, author);
						book.setGenre(genre);
						book.setFilename(filepath);
						books.add(book);
						
					}
					scanCSV.close();
				}
				csvScanner.close();
			}else {
				System.out.println("File of " + csvFile + " not found.");
			}
			
		}catch (FileNotFoundException e) {
			System.out.println("File of " + csvFile + " not found.");
		}
		
	}
	
}

import java.util.ArrayList;

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
}

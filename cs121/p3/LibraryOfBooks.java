import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class LibraryOfBooks {
	/**
	 * CS 121 Project 3 - Library of Books
	 * 
	 * This project allows users to input an author, title, genre, and filepath
	 * for a book, and add it to a library. From there, they can add to the 
	 * library, delete, print, or read a book.
	 * 
	 * @author Austen Hale
	 */

	public static void main(String[] args) {
		//instance variables
		Scanner kbd = new Scanner(System.in);
		int emptyList = 0;
	
		Library booksLibrary = new Library();
		
		
		//menu
		System.out.println("(P)rint the library");
		System.out.println("(A)dd a book");
		System.out.println("(D)elete a book");
		System.out.println("(R)ead a book");
		System.out.println("(Q)uit");
		System.out.println("(M)enu" + "\n");
		System.out.println("Enter a command from the list.");
		
		
		
		char a = kbd.next().charAt(0);
		
		while (a != 'q' && a != 'Q') {
			
			switch(a) {
			//print
			case 'p':
			case 'P': 
//				int index = 0;
//				for (int i = 0; i< bookLibrary.size(); i++) {
//					System.out.println("Books: " + bookLibrary.get(i).getTitle() + " Index: [" + index + "]" );
//					 emptyList ++;
//					 index++;
//				}
				int index = 0;
				ArrayList<Book> bookLibrary = booksLibrary.getBooks();
				
				if (!(bookLibrary.size() == 0))	{
					
					for (int i =0; i< bookLibrary.size(); i++) {
						
				System.out.println("Books: " + bookLibrary.get(i).getTitle() + " Index: [" + index + "]");
				index ++;
					}
					
				}
				
				else {
					System.out.println("No books to print.");
				}
				
				System.out.println("Enter a command from the list ('m' to resee menu).");
				a = kbd.next().charAt(0);
			break;
			
			//add
			case 'a':
			case 'A':
			String author = kbd.nextLine();
				
			//author	
			System.out.println("Enter a book author: ");
			 author = kbd.nextLine();
			
			//title
			System.out.println("Enter a book title: ");
			String title = kbd.nextLine();
			
			//genre
			System.out.println("Enter a book genre: ");
			String genre = kbd.nextLine();
			
			//filename
			System.out.println("Enter a book filepath: ");
			String filename = kbd.nextLine();
			
			Book book = new Book(title, author);
			book.setFilename(filename);
			book.setGenre(genre);
			booksLibrary.addBook(book);
		//	bookLibrary.add(book);
			
			System.out.println("Book of " + book.getTitle() + " added.");
			
			
			System.out.println("Enter a command from the list ('m' to resee menu).");
			a = kbd.next().charAt(0);
			break;
			
			//delete
			case 'd':
			case 'D':
				
				
			if (!(booksLibrary.getBooks().size() == 0))	{
			System.out.println("Enter the index value of the book you would like to delete.");
			try {
			int indexDelete = kbd.nextInt();
			if (indexDelete > -1 && indexDelete <= booksLibrary.getBooks().size()) {
			System.out.println("Book " + booksLibrary.getBook(indexDelete).getTitle() + " removed.");
			booksLibrary.removeBook(indexDelete);
			}
			if (indexDelete < 0 || indexDelete > booksLibrary.getBooks().size()) {
				System.out.println("Input out of bounds.");
			}
			}catch(InputMismatchException e) {
				System.out.println("Unable to accept non number entries.");
				a = kbd.next().charAt(0);
			}
			}else {
				System.out.println("No books to remove.");
			}
			System.out.println("Enter a command from the list ('m' to resee menu).");
			a = kbd.next().charAt(0);
			
			break;
			
			//read
			case 'r':
			case 'R':
				
				if (!(booksLibrary.getBooks().size() == 0)){
				System.out.println("Input the index value of the book you would like to read.");
				try {
				int indexRead = kbd.nextInt();
				if (indexRead > -1 && indexRead <= booksLibrary.getBooks().size()) {
				System.out.println(booksLibrary.getBook(indexRead).getText());
				}
				if (indexRead < 0 || indexRead > booksLibrary.getBooks().size()) {
					System.out.println("Input out of bounds. ");
					
				}
				
				}
				catch (InputMismatchException e) {
					System.out.println("Unable to accept non number entries.");
					a = kbd.next().charAt(0);
				}
				
		
				}else {
					System.out.println("No books available to read.");
				}
				
				
				System.out.println("Enter a command from the list ('m' to resee menu).");
				a = kbd.next().charAt(0);
			
				
			break;
			
			
			//Menu
			case 'm':
			case 'M':
			
				System.out.println("(P)rint the library");
				System.out.println("(A)dd a book");
				System.out.println("(D)elete a book");
				System.out.println("(R)ead a book");
				System.out.println("(Q)uit");
				System.out.println("(M)enu \n");
				System.out.println("Enter a command from the list.");
				
				a = kbd.next().charAt(0);
			break;
				
			default:
				System.out.println("Invalid command. Please try again.");
				a = kbd.next().charAt(0);
			
			}
			
			
		}
		if (a == 'q' || a == 'Q') {
			System.out.println("Program stopped. See ya!");
			kbd.close();
		}
		

	}

}
